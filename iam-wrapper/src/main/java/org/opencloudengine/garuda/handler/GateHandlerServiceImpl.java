package org.opencloudengine.garuda.handler;

import org.opencloudengine.garuda.gateway.GateException;
import org.opencloudengine.garuda.gateway.GatewayService;
import org.opencloudengine.garuda.gateway.GatewayServlet;
import org.opencloudengine.garuda.handler.activity.policy.PolicyHandler;
import org.opencloudengine.garuda.history.TaskHistory;
import org.opencloudengine.garuda.history.TransactionHistory;
import org.opencloudengine.garuda.history.TransactionHistoryRepository;
import org.opencloudengine.garuda.util.JsonUtils;
import org.opencloudengine.garuda.web.configuration.ConfigurationHelper;
import org.opencloudengine.garuda.web.policy.Policy;
import org.opencloudengine.garuda.web.policy.PolicyService;
import org.opencloudengine.garuda.web.uris.ResourceUri;
import org.opencloudengine.garuda.web.workflow.Workflow;
import org.opencloudengine.garuda.web.workflow.WorkflowService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.uengine.kernel.*;
import org.uengine.processpublisher.BPMNUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
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
    PolicyService policyService;

    @Autowired
    TransactionHistoryRepository transactionHistoryRepository;

    /**
     * SLF4J Logging
     */
    private Logger logger = LoggerFactory.getLogger(GateHandlerServiceImpl.class);

    @Override
    public void doWorkflowHandler(ResourceUri resourceUri, GatewayServlet servlet, HttpServletRequest servletRequest, HttpServletResponse servletResponse) {

        String wid = resourceUri.getWid();
        Workflow workflow = workflowService.cashById(wid);
        if (workflow == null) {
            gatewayService.errorResponse(GateException.WORKFLOW_NOT_FOUND, servletRequest, servletResponse, null);
            return;
        }

        String identifier = UUID.randomUUID().toString();
        Date currentDate = new Date();

        /**
         * 트랜잭션 히스토리를 인서트한다.
         */
        TransactionHistory history = new TransactionHistory();
        history.setIdentifier(identifier);
        history.setUri(servletRequest.getPathInfo());
        history.setMethod(servletRequest.getMethod());
        history.setRunWith(TransactionHistory.WORKFLOW);
        history.setWid(wid);
        history.setWorkflowName(workflow.getName());
        history.setVars(workflow.getVars());
        history.setStartDate(currentDate.getTime());
        history.setStatus("RUNNING");

        /**
         * 타스크 히스토리 객체
         */
        List<TaskHistory> taskHistories = new ArrayList<>();


        /**
         * 임시저장소에 통신관련 객체들을 저장한다.
         */
        globalAttributes.setHttpObjects(identifier, servlet, servletRequest, servletResponse, resourceUri);

        /**
         * 프로세스 객체
         */
        org.uengine.kernel.ProcessInstance instance = null;

        /**
         * 프로세스를 시작한다.
         */
        try {
            org.uengine.kernel.ProcessInstance.USE_CLASS = DefaultProcessInstance.class;
            ByteArrayInputStream bis = new ByteArrayInputStream(workflow.getBpmn_xml().getBytes());
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

            instance = processDefinition.createInstance();
            instance.setInstanceId(identifier);
            instance.set("workflow", workflow);
            instance.set("transactionHistory", history);
            instance.set("vars", JsonUtils.unmarshal(workflow.getVars()));

            //워크플로우 각 타스크 결과물들을 저장할 임시저장소를 등록한다.
            globalAttributes.registJobResultMap(instance);

            //워크플로우를 실행한다.
            instance.execute();

            /**
             * 워크플로우 히스토리와 타스크 히스토리의 결과물을 얻는다.
             */
            taskHistories = globalAttributes.getAllTaskHistories(instance);
            history = (TransactionHistory) instance.get("transactionHistory");

            if (instance.get("finishEvent") == null) {
                gatewayService.errorResponse(GateException.WORKFLOW_FAILED, servletRequest, servletResponse, null);
                this.updateAsFailed(history, taskHistories);
            } else {
                this.updateAsFinished(history, taskHistories);
            }

        } catch (Exception ex) {
            gatewayService.errorResponse(GateException.WORKFLOW_FAILED, servletRequest, servletResponse, null);
            this.updateAsFailed(history, taskHistories);
        } finally {
            //임시저장소에 통신관련 객체들을 삭제한다.
            globalAttributes.removeHttpObjects(identifier);
        }
    }

    @Override
    public void doPolicyHandler(ResourceUri resourceUri, GatewayServlet servlet, HttpServletRequest servletRequest, HttpServletResponse servletResponse) {
        Policy policy = policyService.cashById(resourceUri.getPolicyId());
        if (policy == null) {
            gatewayService.errorResponse(GateException.POLICY_NOT_FOUND, servletRequest, servletResponse, null);
            return;
        }
        String identifier = UUID.randomUUID().toString();
        Date currentDate = new Date();

        /**
         * 타스크 히스토리 객체
         */
        List<TaskHistory> taskHistories = new ArrayList<>();

        /**
         * 트랜잭션 히스토리를 인서트한다.
         */
        TransactionHistory history = new TransactionHistory();
        history.setIdentifier(identifier);
        history.setUri(servletRequest.getPathInfo());
        history.setMethod(servletRequest.getMethod());
        history.setRunWith(TransactionHistory.POLICY);
        history.setPolicyId(policy.get_id());
        history.setPolicyName(policy.getName());
        history.setStartDate(currentDate.getTime());
        history.setStatus("RUNNING");

        PolicyHandler policyHandler = new PolicyHandler();
        policyHandler.transactionHistory = history;

        try {
            policyHandler.init(resourceUri, servlet, servletRequest, servletResponse, identifier, policy);
            policyHandler.doAction();
            /**
             * 워크플로우 히스토리와 타스크 히스토리의 결과물을 얻는다.
             */
            history = policyHandler.transactionHistory;
            taskHistories = policyHandler.getTaskHistories();
            boolean succeeded = policyHandler.transactionSucceeded;
            if(succeeded){
                this.updateAsFinished(history, taskHistories);
            }else{
                this.updateAsFailed(history, taskHistories);
            }

        } catch (Exception ex) {
            gatewayService.errorResponse(GateException.SERVER_ERROR, servletRequest, servletResponse, null);
            /**
             * 워크플로우 히스토리와 타스크 히스토리의 결과물을 얻는다.
             */
            history = policyHandler.transactionHistory;
            taskHistories = policyHandler.getTaskHistories();
            this.updateAsFailed(history, taskHistories);
        }
    }

    @Override
    public void doClassHandler(ResourceUri resourceUri, GatewayServlet servlet, HttpServletRequest servletRequest, HttpServletResponse servletResponse) {

        String identifier = UUID.randomUUID().toString();

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
            Method init = act.getMethod("init", ResourceUri.class, GatewayServlet.class, HttpServletRequest.class, HttpServletResponse.class, String.class);
            init.setAccessible(true);
            init.invoke(obj, new Object[]{resourceUri, servlet, servletRequest, servletResponse,identifier});

            Method doAction = act.getMethod("doAction");
            doAction.setAccessible(true);
            doAction.invoke(obj, new Object[]{});
        } catch (Exception ex) {
            gatewayService.errorResponse(GateException.SERVER_ERROR, servletRequest, servletResponse, null);
        }
    }

    private void updateAsFailed(TransactionHistory history, List<TaskHistory> taskHistories) {
        long time = new Date().getTime();
        history.setEndDate(time);
        history.setDuration(time - history.getStartDate());
        history.setStatus("FAILED");

        transactionHistoryRepository.bulk(history, taskHistories);
    }

    private void updateAsFinished(TransactionHistory history, List<TaskHistory> taskHistories) {
        long time = new Date().getTime();
        history.setEndDate(time);
        history.setDuration(time - history.getStartDate());
        history.setStatus("FINISHED");

        transactionHistoryRepository.bulk(history, taskHistories);
    }
}
