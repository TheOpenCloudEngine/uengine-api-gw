package org.opencloudengine.garuda.web.workflow;

import org.activiti.bpmn.model.BpmnModel;
import org.opencloudengine.garuda.util.DateUtils;
import org.opencloudengine.garuda.util.JVMIDUtils;
import org.opencloudengine.garuda.util.JsonUtils;
import org.opencloudengine.garuda.util.Transformer;
import org.opencloudengine.garuda.web.configuration.ConfigurationHelper;
import org.opencloudengine.garuda.web.policy.Policy;
import org.opencloudengine.garuda.web.policy.PolicyRepository;
import org.opencloudengine.garuda.web.policy.PolicyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

@Service
public class WorkflowServiceImpl implements WorkflowService {
    @Autowired
    @Qualifier("config")
    private Properties config;

    @Autowired
    private WorkflowRepository workflowRepository;

    @Autowired
    ConfigurationHelper configurationHelper;

    @Autowired
    Transformer transformer;

    /**
     * SLF4J Logging
     */
    private Logger logger = LoggerFactory.getLogger(WorkflowServiceImpl.class);

    @Override
    public Workflow createWorkflow(String name,
                                   String designer_xml) throws Exception {

        Workflow workflow = new Workflow();
        workflow.setName(name);
        workflow.setDesigner_xml(designer_xml);

        this.addBpmnXml(workflow);
        return workflowRepository.insert(workflow);
    }

    private void addBpmnXml(Workflow workflow) throws Exception {
        String name = workflow.getName();
        String designer_xml = workflow.getDesigner_xml();

        // 신규 프로세스이므로 새로운 Process ID를 생성한다.
        String newProcessId = DateUtils.getCurrentDateTime() + "_" + JVMIDUtils.generateUUID();

        //  BPMN 모델을 생성한다.
        BpmnModel model = transformer.unmarshall(designer_xml, newProcessId, name);
        String processName = model.getMainProcess().getName();
        Map<String, Map<String, String>> localVariables = transformer.getLocalVariables(designer_xml);

        String bpmnXML = transformer.convertUengineBpmnXml(transformer.createBpmnXML(model));

        logger.info("프로세스를 생성했습니다. 프로세스 ID = {}, 프로세스명 = {}", newProcessId, model.getMainProcess().getName());

        workflow.setBpmn_xml(bpmnXML);
        workflow.setVars(JsonUtils.marshal(localVariables));
    }

    @Override
    public List<Workflow> selectAll() {
        return workflowRepository.selectAll();
    }

    @Override
    public List<Workflow> select(int limit, Long skip) {
        return workflowRepository.select(limit, skip);
    }

    @Override
    public Workflow selectById(String id) {
        return workflowRepository.selectById(id);
    }

    @Override
    public Workflow cashById(String id) {
        Workflow cashWorkflow = null;
        List<Workflow> cash = workflowRepository.getCash();
        for (Workflow workflow : cash) {
            if(workflow.get_id().equals(id)){
                cashWorkflow = workflow;
            }
        }
        return cashWorkflow;
    }

    @Override
    public List<Workflow> selectLikeName(String name, int limit, Long skip) {
        return workflowRepository.selectLikeName(name, limit, skip);
    }

    @Override
    public Workflow selectByName(String name) {
        return workflowRepository.selectByName(name);
    }

    @Override
    public Long count() {
        return workflowRepository.count();
    }

    @Override
    public Long countLikeName(String name) {
        return workflowRepository.countLikeName(name);
    }

    @Override
    public Workflow updateById(Workflow workflow) throws Exception {
        this.addBpmnXml(workflow);
        return workflowRepository.updateById(workflow);
    }

    @Override
    public Workflow updateById(String id,
                               String name,
                               String designer_xml) throws Exception {
        Workflow workflow = new Workflow();
        workflow.set_id(id);
        workflow.setName(name);
        workflow.setDesigner_xml(designer_xml);
        this.addBpmnXml(workflow);

        return workflowRepository.updateById(workflow);
    }

    @Override
    public void deleteById(String id) {
        workflowRepository.deleteById(id);
    }
}
