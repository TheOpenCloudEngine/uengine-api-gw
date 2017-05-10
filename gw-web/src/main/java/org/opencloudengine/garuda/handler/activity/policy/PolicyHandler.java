package org.opencloudengine.garuda.handler.activity.policy;

import org.opencloudengine.garuda.gateway.GateException;
import org.opencloudengine.garuda.handler.activity.policy.data.BeforeInput;
import org.opencloudengine.garuda.handler.activity.policy.data.BeforeOutput;
import org.opencloudengine.garuda.handler.activity.policy.data.ProxyInput;
import org.opencloudengine.garuda.handler.activity.policy.data.ProxyOutput;
import org.opencloudengine.garuda.handler.activity.workflow.data.AuthenticationInput;
import org.opencloudengine.garuda.handler.activity.workflow.data.RequestInput;
import org.opencloudengine.garuda.model.AuthInformation;
import org.opencloudengine.garuda.proxy.ProxyRequest;
import org.opencloudengine.garuda.script.ScriptResponse;
import org.opencloudengine.garuda.util.ExceptionUtils;
import org.opencloudengine.garuda.util.StringUtils;


/**
 * Created by uengine on 2016. 6. 16..
 */
public class PolicyHandler extends PolicyInterceptorHandler {

    @Override
    public void doAction() {

        String taskName = "Authentication";
        AuthInformation authInformation = new AuthInformation();
        String authentication = policy.getAuthentication();
        if (authentication.equals("Y")) {
            //타스크 히스토리 등록
            this.initTaskHistory(taskName);
            //인풋 등록
            AuthenticationInput authenticationInput = new AuthenticationInput();
            authenticationInput.setTokenName(policy.getTokenName());
            authenticationInput.setTokenLocation(policy.getTokenLocation());
            this.updateTaskInputData(taskName, authenticationInput);

            //토큰을 인증한다.
            authInformation = securityService.validateRequest(
                    servletRequest,
                    policy.getTokenName(),
                    policy.getTokenLocation(),
                    null);

            this.updateTaskOutputData(taskName, authInformation);

            //인증이 실패한경우의 처리.
            if (authInformation.getError() != null) {
                gatewayService.errorResponse(
                        GateException.AUTHENTICATION_FAIL,
                        servletRequest,
                        servletResponse,
                        null);

                this.updateTaskStderr(taskName, authInformation.getError_description());
                this.updateTaskHistoryAsFailed(taskName);
                return;
            }
            //인증이 성공한경우의 처리
            else {
                this.updateTaskHistoryAsFinished(taskName);
            }
        }

        //리퀘스트 객체
        RequestInput requestInput = new RequestInput();
        requestInput.setHeaders(this.getHeaders());
        requestInput.setMethod(servletRequest.getMethod());
        requestInput.setPathVarialbe(pathVarialbe);
        requestInput.setUri(servletRequest.getPathInfo());

        if (!StringUtils.isEmpty(policy.getBeforeUse())) {
            boolean continueProxy = false;
            //인풋 데이터
            taskName = "BeforeScript";
            this.initTaskHistory(taskName);
            BeforeInput beforeInput = new BeforeInput(requestInput, authInformation);
            this.updateTaskInputData(taskName, beforeInput);


            //아웃풋 데이터
            BeforeOutput beforeOutput = new BeforeOutput();

            try {
                ScriptResponse response = scriptService.beforeUseScript(policy.getBeforeUse(), beforeInput);
                //로그 저장
                this.updateTaskStdout(taskName, response.getLog());

                //boolean 캐스트
                continueProxy = response.castValue(Boolean.class);

                //아웃풋 저장
                beforeOutput.setContinueProxy(continueProxy);
                this.updateTaskOutputData(taskName, beforeOutput);

                if (!continueProxy) {
                    //실패 로그 저장
                    this.updateTaskStderr(taskName, "continueProxy false: Stop by before use script.");

                    //실패 히스토리 저장
                    this.updateTaskHistoryAsFailed(taskName);

                    gatewayService.errorResponse(GateException.BEFORE_USE_SCRIPT, servletRequest, servletResponse, "continueProxy false: Stop by before use script.");
                    return;
                }else{
                    //성공 저장
                    this.updateTaskHistoryAsFinished(taskName);
                }

            } catch (Exception ex) {

                //실패 로그 저장
                this.updateTaskStderr(taskName, ExceptionUtils.getFullStackTrace(ex));

                //아웃풋 저장
                beforeOutput.setContinueProxy(continueProxy);
                this.updateTaskOutputData(taskName, beforeOutput);

                //실패 히스토리 저장
                this.updateTaskHistoryAsFailed(taskName);

                gatewayService.errorResponse(GateException.BEFORE_USE_SCRIPT, servletRequest, servletResponse, ex.getCause().toString());
                return;
            }
        }

        ProxyRequest proxyRequest = new ProxyRequest();
        proxyRequest.setProxyServlet(gatewayServlet);
        proxyRequest.setRequest(servletRequest);
        proxyRequest.setResponse(servletResponse);

        //프록시 동작
        String prefixUri = policy.getPrefixUri();
        String[] split = prefixUri.split(",");
        String from = split[0];
        String to = split[1];
        String path = servletRequest.getPathInfo().replaceFirst(from, to);
        proxyRequest.setHost(policy.getProxyUri());
        proxyRequest.setPath(path);

        taskName = "ProxyAction";
        this.initTaskHistory(taskName);
        ProxyInput proxyInput = new ProxyInput();
        proxyInput.setHost(policy.getProxyUri());
        proxyInput.setPath(path);
        this.updateTaskInputData(taskName, proxyInput);

        ProxyOutput proxyOutput = new ProxyOutput();
        proxyOutput.setHost(policy.getProxyUri());
        proxyOutput.setPath(path);

        try {
            proxyService.doProxy(proxyRequest);
            proxyOutput.setStatus(ProxyOutput.SUCCEEDED);
            this.updateTaskOutputData(taskName, proxyOutput);
            this.updateTaskHistoryAsFinished(taskName);
        } catch (Exception ex) {
            gatewayService.errorResponse(GateException.PROXY_FAILED, servletRequest, servletResponse, null);
            proxyOutput.setStatus(ProxyOutput.FAILED);
            this.updateTaskOutputData(taskName, proxyOutput);
            this.updateTaskStderr(taskName, ExceptionUtils.getFullStackTrace(ex));
            this.updateTaskHistoryAsFailed(taskName);
            return;
        }


        if (!StringUtils.isEmpty(policy.getAfterUse())) {
            //인풋 데이터
            taskName = "AfterScript";
            this.initTaskHistory(taskName);
            BeforeInput beforeInput = new BeforeInput(requestInput, authInformation);
            this.updateTaskInputData(taskName, beforeInput);

            try {
                ScriptResponse response = scriptService.beforeUseScript(policy.getAfterUse(), beforeInput);
                //로그 저장
                this.updateTaskStdout(taskName, response.getLog());

                //성공 저장
                this.updateTaskHistoryAsFinished(taskName);

            } catch (Exception ex) {
                gatewayService.errorResponse(GateException.BEFORE_USE_SCRIPT, servletRequest, servletResponse, ex.getCause().toString());

                //로그 저장
                this.updateTaskStderr(taskName, ExceptionUtils.getFullStackTrace(ex));

                //실패 저장
                this.updateTaskHistoryAsFailed(taskName);
            }
        }
    }
}
