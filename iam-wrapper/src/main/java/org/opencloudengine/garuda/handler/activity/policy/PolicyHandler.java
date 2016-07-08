package org.opencloudengine.garuda.handler.activity.policy;

import org.opencloudengine.garuda.gateway.GateException;
import org.opencloudengine.garuda.handler.AbstractHandler;
import org.opencloudengine.garuda.model.AuthInformation;
import org.opencloudengine.garuda.model.User;
import org.opencloudengine.garuda.proxy.ProxyRequest;
import org.opencloudengine.garuda.script.ScriptRequest;
import org.opencloudengine.garuda.script.ScriptResponse;
import org.opencloudengine.garuda.util.StringUtils;
import org.opencloudengine.garuda.web.policy.Policy;

import java.util.List;

/**
 * Created by uengine on 2016. 6. 16..
 */
public class PolicyHandler extends AbstractHandler {

    @Override
    public void doAction() {

        Policy policy = policyService.cashById(resourceUri.getPolicyId());
        if (policy == null) {
            gatewayService.errorResponse(GateException.POLICY_NOT_FOUND, servletRequest, servletResponse, null);
            return;
        }

        AuthInformation authInformation = new AuthInformation();
        String authentication = policy.getAuthentication();
        if (authentication.equals("Y")) {
            //토큰을 인증한다.
            authInformation = securityService.validateRequest(
                    servletRequest,
                    policy.getTokenName(),
                    policy.getTokenLocation(),
                    null);

            //인증이 실패한경우의 처리.
            if (authInformation.getError() != null) {
                gatewayService.errorResponse(
                        GateException.AUTHENTICATION_FAIL,
                        servletRequest,
                        servletResponse,
                        null);
                return;
            }
        }

        boolean continueProxy = false;
        if (!StringUtils.isEmpty(policy.getBeforeUse())) {
            try {
                continueProxy = scriptService.beforeUseScript(policy.getBeforeUse(), authInformation);
            } catch (Exception ex) {
                gatewayService.errorResponse(GateException.BEFORE_USE_SCRIPT, servletRequest, servletResponse, ex.getCause().toString());
                return;
            }
        }

        if (!continueProxy) {
            gatewayService.errorResponse(GateException.BEFORE_USE_SCRIPT, servletRequest, servletResponse, "Stop by before use script.");
            return;
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
        proxyRequest.setHost(policy.getProxyUri());
        proxyRequest.setPath(this.getServletRequest().getPathInfo());

        proxyRequest.setPath(servletRequest.getPathInfo().replaceFirst(from, to));
        proxyService.doProxy(proxyRequest);


        if (!StringUtils.isEmpty(policy.getAfterUse())) {
            try {
                scriptService.afterUseScript(policy.getAfterUse(), authInformation);
            } catch (Exception ex) {
                //별다른 수행을 하지 않는다.
                //TODO 히스토리에 로그 남기기
            }
        }
    }
}
