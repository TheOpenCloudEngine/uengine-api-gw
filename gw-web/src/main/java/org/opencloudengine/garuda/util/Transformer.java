package org.opencloudengine.garuda.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.*;
import org.activiti.bpmn.model.Process;
import org.activiti.engine.impl.util.io.InputStreamSource;
import org.apache.commons.lang.StringEscapeUtils;
import org.opencloudengine.garuda.common.exception.ServiceException;
import org.opencloudengine.garuda.jaxb.opengraph.Opengraph;
import org.opencloudengine.garuda.web.workflow.WorkflowRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.MessageFormatter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.*;

import static org.apache.commons.lang.StringUtils.isEmpty;
import static org.apache.commons.lang.StringUtils.splitPreserveAllTokens;

public class Transformer implements InitializingBean {

    public String JAXB_PACKAGE = "org.opencloudengine.garuda.jaxb.opengraph";
    @Autowired
    @Qualifier("taskProps")
    Properties taskProps;
    /**
     * SLF4J Logging
     */
    private Logger logger = LoggerFactory.getLogger(Transformer.class);
    private ObjectMapper objectMapper;
    private BpmnXMLConverter converter;
    private String defaultTask;
    @Autowired
    private WorkflowRepository workflowRepository;

    @Override
    public void afterPropertiesSet() throws Exception {
        this.converter = new BpmnXMLConverter();
        this.objectMapper = new ObjectMapper();
    }

    public Map<String, Map<String, String>> getLocalVariables(String xml) throws Exception {
        Map<String, Map<String, String>> localVariables = new HashMap();
        Opengraph opengraph = (Opengraph) JaxbUtils.unmarshal(JAXB_PACKAGE, xml);
        List<Opengraph.Cell> cells = opengraph.getCell();
        Map<String, Opengraph.Cell> cellMap = new HashMap<String, Opengraph.Cell>();
        for (Opengraph.Cell cell : cells) {
            cellMap.put(cell.getId(), cell);
        }
        for (Opengraph.Cell cell : cells) {
            if (!StringUtils.isEmpty(cell.getData())) {
                Map actionParams = objectMapper.readValue(StringUtils.unescape(cell.getData()), Map.class);
                Map<String, String> properties = filter((Map) actionParams.get("properties"));
                if ("IMAGE".equals(cell.getShapeType())) {
                    properties.put("shapeId", cell.getShapeId());
                    String id = cell.getId();
                    localVariables.put(id, properties);
                }
            }
        }
        return localVariables;
    }

    public BpmnModel unmarshall(String xml, String processId, String processName) throws Exception {
        Opengraph opengraph = (Opengraph) JaxbUtils.unmarshal(JAXB_PACKAGE, xml);
        List<Opengraph.Cell> cells = opengraph.getCell();

        BpmnModel model = createBpmnModel(processId, processName);

        /**
         * 시작 노드를 추가한다.
         */
        StartEvent startEvent = getStart(model.getMainProcess(), "ST" + JVMIDUtils.generateUUID());
        model.getMainProcess().addFlowElement(startEvent);

        /**
         * 종료 노드를 추가한다.
         */
        EndEvent endEvent = getEnd(model.getMainProcess(), "EN" + JVMIDUtils.generateUUID());
        model.getMainProcess().addFlowElement(endEvent);

        for (Opengraph.Cell cell : cells) {
            /**
             * 시퀀스 플로우를 처리한다.
             */
            if ("EDGE".equals(cell.getShapeType())) {
                String from = cell.getFrom();
                String to = cell.getTo();
                from = from.substring(0, from.indexOf("_TERMINAL"));
                to = to.substring(0, to.indexOf("_TERMINAL"));
                String label = StringUtils.unescape(cell.getLabel());

                // 다음 컨디셔널 시퀀스플로우를 처리한다.
                Map expressionMap = new HashMap();
                expressionMap.put("prevTaskId", from);
                expressionMap.put("label", label);
                String conditionsString = JsonUtils.marshal(expressionMap);

                bindConditionalSequenceFlow(model.getMainProcess(), from, to, conditionsString, label);
                continue;
            }

            if (StringUtils.isEmpty(cell.getData())) {
                continue;
            }

            Map actionParams = objectMapper.readValue(StringUtils.unescape(cell.getData()), Map.class);
            Map metadata = (Map) actionParams.get("metadata");
            Map<String, String> properties = filter((Map) actionParams.get("properties"));

            /**
             * 타스크 뒤에 플로우가 없다면 종료이벤트로 연결한다.
             */
            if ("IMAGE".equals(cell.getShapeType())) {
                String id = cell.getId();
                String to = cell.getTo();
                if (StringUtils.isEmpty(to)) {
                    bindSequenceFlow(model.getMainProcess(), id, endEvent.getId(), "END");
                }
            }

            /**
             * 리퀘스트 타스크를 처리한다.
             * 리퀘스트 이전 타스크에 시작 이벤트를 추가한다.
             */
            if ("OG.shape.router.Request".equals(cell.getShapeId())) {
                String id = cell.getId();

                // Task Class를 처리한다.
                bindServiceTask(model.getMainProcess(), id, cell.getLabel(), properties, cell.getShapeId());

                //시작이벤트에서 Request 로 시퀀스를 추가한다.
                bindSequenceFlow(model.getMainProcess(), startEvent.getId(), id, "START");
            }

            /**
             * 타스크를 처리한다.
             */
            else if ("IMAGE".equals(cell.getShapeType())) {
                String id = cell.getId();

                bindServiceTask(model.getMainProcess(), id, cell.getLabel(), properties, cell.getShapeId());
            }

        }
        return model;
    }

    private Opengraph.Cell getCellById(String cellId, List cells) {
        Opengraph.Cell targetcell = null;
        for (int i = 0; i < cells.size(); i++) {
            Opengraph.Cell cell = (Opengraph.Cell) cells.get(i);
            if (cell.getId().equals(cellId)) {
                targetcell = cell;
            }
        }
        return targetcell;
    }

    private Map<String, String> filter(Map properties) {
        Map<String, String> filteredProps = new HashMap<>();
        if (properties != null) {
            Set set = properties.keySet();
            for (Object obj : set) {
                String key = (String) obj;
                String value = (String) properties.get(key);

                if (!in(key)) {
                    filteredProps.put(key, value);
                }
            }
        }
        return filteredProps;
    }

    private boolean in(String key) {
        boolean in = false;
        String[] excludes = new String[]{"numberfield", "Theme"};
        for (String exclude : excludes) {
            if (key.startsWith(exclude)) {
                in = true;
                break;
            }
        }
        return in;
    }

    public String createBpmnXML(BpmnModel bpmnModel) throws Exception {
        byte[] xml = new BpmnXMLConverter().convertToXML(bpmnModel);
        String str = new String(xml);
        String unescapeXml = StringEscapeUtils.unescapeXml(str);
        logger.info("BPMN XML은 다음과 같습니다.\n{}", unescapeXml);
        converter.validateModel(new InputStreamSource(new ByteArrayInputStream(unescapeXml.getBytes())));
        return unescapeXml;
    }

    public String convertUengineBpmnXml(String bpmnXML) {
        String activityString = "activiti:class=\"";
        String uengineString = "implementation=\"java:";
        bpmnXML = bpmnXML.replaceAll(activityString, uengineString);
        return bpmnXML;
    }

    private BpmnModel createBpmnModel(String id, String name) {
        BpmnModel model = new BpmnModel();
        Process process = new Process();
        process.setId("W" + id);
        process.setName(StringUtils.unescape(name));
        model.addProcess(process);
        return model;
    }

    private EndEvent getEnd(Process process, String id) {
        EndEvent event = new EndEvent();
        event.setId(id);
        event.setName("End");
        return event;
    }

    private StartEvent getStart(Process process, String id) {
        StartEvent event = new StartEvent();
        event.setId(id);
        event.setName("Start");
        return event;
    }

    private void bindSequenceFlow(Process process, String source, String target,String name) {
        SequenceFlow flow = new SequenceFlow();
        flow.setId("SF" + JVMIDUtils.generateUUID());
        flow.setSourceRef(source);
        flow.setTargetRef(target);
        flow.setName(name);

        flow.setConditionExpression("");
        process.addFlowElement(flow);
    }

    private void bindConditionalSequenceFlow(Process process, String source, String target, String script, String label) {
        SequenceFlow flow = new SequenceFlow();
        flow.setId("SF" + JVMIDUtils.generateUUID());
        flow.setSourceRef(source);
        flow.setTargetRef(target);
        flow.setConditionExpression(script);
        flow.setName(label);

        process.addFlowElement(flow);
    }

    private void bindServiceTask(Process process, String id, String name, Map<String, String> properties, String shapeId) {
        ServiceTask task = new ServiceTask();
        task.setImplementation(getTaskClass(shapeId));
        task.setImplementationType("class");

        task.setId(id);
        task.setName(StringUtils.unescape(name));
        process.addFlowElement(task);
    }

    private String getTaskClass(String fullyQualifiedName) {
        //String className = this.getClassName(fullyQualifiedName);
        String taskClassName = this.taskProps.getProperty(fullyQualifiedName);
        if (isEmpty(taskClassName)) {
            throw new ServiceException(MessageFormatter.format("{}의 Task Class가 존재하지 않습니다.", fullyQualifiedName).getMessage());
        }
        return taskClassName;
    }

    private String getClassName(String fullyQualifiedName) {
        String[] tokens = splitPreserveAllTokens(fullyQualifiedName, ".");
        return tokens[tokens.length - 1];
    }

    public void setDefaultTask(String defaultTask) {
        this.defaultTask = defaultTask;
    }
}
