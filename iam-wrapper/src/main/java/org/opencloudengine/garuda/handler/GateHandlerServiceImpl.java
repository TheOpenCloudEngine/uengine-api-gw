package org.opencloudengine.garuda.handler;

import org.opencloudengine.garuda.common.exception.ServiceException;
import org.opencloudengine.garuda.gateway.GateException;
import org.opencloudengine.garuda.gateway.GatewayService;
import org.opencloudengine.garuda.gateway.GatewayServlet;
import org.opencloudengine.garuda.handler.activity.policy.PolicyHandler;
import org.opencloudengine.garuda.util.ApplicationContextRegistry;
import org.opencloudengine.garuda.util.JsonUtils;
import org.opencloudengine.garuda.web.configuration.ConfigurationHelper;
import org.opencloudengine.garuda.web.history.WorkflowHistory;
import org.opencloudengine.garuda.web.history.WorkflowHistoryRepository;
import org.opencloudengine.garuda.web.uris.ResourceUri;
import org.opencloudengine.garuda.web.workflow.Workflow;
import org.opencloudengine.garuda.web.workflow.WorkflowService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.uengine.kernel.*;
import org.uengine.processpublisher.BPMNUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.*;

@Service
public class GateHandlerServiceImpl implements GateHandlerService {
    @Autowired
    @Qualifier("config")
    private Properties config;

    @Autowired
    ConfigurationHelper configurationHelper;

    @Autowired
    GatewayService gatewayService;

    @Autowired
    WorkflowService workflowService;

    @Autowired
    GlobalAttributes globalAttributes;

    @Autowired
    WorkflowHistoryRepository workflowHistoryRepository;

    /**
     * SLF4J Logging
     */
    private Logger logger = LoggerFactory.getLogger(GateHandlerServiceImpl.class);

    @Override
    public void doWorkflowHandler(ResourceUri resourceUri, GatewayServlet servlet, HttpServletRequest servletRequest, HttpServletResponse servletResponse) {

        String wid = resourceUri.getWid();
        Workflow workflow = workflowService.cashById(wid);

        String identifier = UUID.randomUUID().toString();
        final String name = workflow.getName();
        String bpmn_xml = workflow.getBpmn_xml();
        Date currentDate = new Date();

        /**
         * 워크플로우 히스토리를 인서트한다.
         */
        //TODO request 꾸미기
        WorkflowHistory history = new WorkflowHistory();
        history.setIdentifier(identifier);
        history.setWid(wid);
        history.setName(name);
        history.setVars(workflow.getVars());
        history.setStartDate(currentDate.getTime());
        history.setStatus("RUNNING");
        history = workflowHistoryRepository.insert(history);


        /**
         * 임시저장소에 통신관련 객체들을 저장한다.
         */
        globalAttributes.setHttpObjects(identifier, servlet, servletRequest, servletResponse, resourceUri);

        /**
         * 프로세스를 시작한다.
         */
        try {
            org.uengine.kernel.ProcessInstance.USE_CLASS = DefaultProcessInstance.class;
            ByteArrayInputStream bis = new ByteArrayInputStream(bpmn_xml.getBytes());
            org.uengine.kernel.ProcessDefinition processDefinition = BPMNUtil.adapt(bis);
            processDefinition.afterDeserialization();

            processDefinition.setActivityFilters(new ActivityFilter[]{
                    new SensitiveActivityFilter() {

                        @Override
                        public void beforeExecute(Activity activity, org.uengine.kernel.ProcessInstance processInstance) throws Exception {

                        }

                        @Override
                        public void afterExecute(Activity activity, org.uengine.kernel.ProcessInstance processInstance) throws Exception {

                        }

                        @Override
                        public void afterComplete(Activity activity, org.uengine.kernel.ProcessInstance processInstance) throws Exception {

                        }

                        @Override
                        public void onPropertyChange(Activity activity, org.uengine.kernel.ProcessInstance processInstance, String s, Object o) throws Exception {

                        }

                        @Override
                        public void onDeploy(org.uengine.kernel.ProcessDefinition processDefinition) throws Exception {

                        }

                        @Override
                        public void onEvent(Activity activity, org.uengine.kernel.ProcessInstance processInstance, String s, Object o) throws Exception {
                            if (activity instanceof EndActivity && Activity.ACTIVITY_STOPPED.equals(s)) {
                                System.out.println(processInstance.getActivityCompletionHistory());

                                processInstance.set("finishEvent", true);
                                System.out.println("workflow end with SUCCESS");
                            }
                        }
                    }
            });

            org.uengine.kernel.ProcessInstance instance = processDefinition.createInstance();
            instance.setInstanceId(identifier);
            instance.set("workflow", workflow);
            instance.set("wh", history);
            instance.set("vars", JsonUtils.unmarshal(workflow.getVars()));

            //워크플로우 각 타스크 결과물들을 저장할 임시저장소를 등록한다.
            globalAttributes.registJobResultMap(instance);

            //워크플로우를 실행한다.
            instance.execute();

            if (instance.get("finishEvent") == null) {
                workflowHistoryRepository.updateAsFailed(history);
                gatewayService.errorResponse(GateException.WORKFLOW_FAILED, servletRequest, servletResponse, null);
            } else {
                workflowHistoryRepository.updateAsFinished(history);
            }

        } catch (Exception ex) {
            workflowHistoryRepository.updateAsFailed(history);
            gatewayService.errorResponse(GateException.WORKFLOW_FAILED, servletRequest, servletResponse, null);
        } finally {
            //임시저장소에 통신관련 객체들을 삭제한다.
            globalAttributes.removeHttpObjects(identifier);
        }
    }

    @Override
    public void doPolicyHandler(ResourceUri resourceUri, GatewayServlet servlet, HttpServletRequest servletRequest, HttpServletResponse servletResponse) {
        try {
            PolicyHandler policyHandler = new PolicyHandler();
            policyHandler.init(resourceUri, servlet, servletRequest, servletResponse);
            policyHandler.doAction();
        } catch (Exception ex) {
            gatewayService.errorResponse(GateException.SERVER_ERROR, servletRequest, servletResponse, null);
        }
    }

    @Override
    public void doClassHandler(ResourceUri resourceUri, GatewayServlet servlet, HttpServletRequest servletRequest, HttpServletResponse servletResponse) {

        String className = "org.opencloudengine.garuda.handler.activity.classhandler." + resourceUri.getClassName();
        Class<?> act = null;
        try {
            act = Class.forName(className);
        } catch (ClassNotFoundException ex) {
            gatewayService.errorResponse(GateException.CLASS_NOT_FOUND, servletRequest, servletResponse, null);
            return;
        }

        try {
            Object obj = act.newInstance();
            Method init = act.getMethod("init", ResourceUri.class, GatewayServlet.class, HttpServletRequest.class, HttpServletResponse.class);
            init.setAccessible(true);
            init.invoke(obj, new Object[]{resourceUri, servlet, servletRequest, servletResponse});

            Method doAction = act.getMethod("doAction");
            doAction.setAccessible(true);
            doAction.invoke(obj, new Object[]{});
        } catch (Exception ex) {
            gatewayService.errorResponse(GateException.SERVER_ERROR, servletRequest, servletResponse, null);
        }
    }
}
