package org.opencloudengine.garuda.handler.activity.policy;

import org.opencloudengine.garuda.gateway.GateException;
import org.opencloudengine.garuda.handler.AbstractHandler;
import org.opencloudengine.garuda.model.AuthInformation;
import org.opencloudengine.garuda.proxy.ProxyRequest;
import org.opencloudengine.garuda.web.policy.Policy;

/**
 * Created by uengine on 2016. 6. 16..
 */
public class PolicyHandler extends AbstractHandler {

    @Override
    public void doAction() {

        Policy policy = policyService.selectById(resourceUri.getPolicyId());
        if (policy == null) {
            gatewayService.errorResponse(GateException.POLICY_NOT_FOUND, servletRequest, servletResponse, null);
            return;
        }

        AuthInformation authInformation;
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

        //TODO before 스크립트 실행하기. 이 스크립트는 리턴값이 필요.

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

        //TODO after 스크립트 실행하기. 이 스크립트는 리턴값이 불필요.
    }


}
