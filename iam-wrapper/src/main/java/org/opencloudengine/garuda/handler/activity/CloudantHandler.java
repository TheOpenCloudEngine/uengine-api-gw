package org.opencloudengine.garuda.handler.activity;

import org.opencloudengine.garuda.gateway.GateException;
import org.opencloudengine.garuda.handler.AbstractHandler;
import org.opencloudengine.garuda.model.AuthInformation;
import org.opencloudengine.garuda.proxy.ProxyRequest;

/**
 * Created by uengine on 2016. 6. 16..
 */
public class CloudantHandler extends AbstractHandler {

    @Override
    public void doAction() {

        //토큰을 인증한다.
        AuthInformation authInformation = securityService.validateRequest(
                servletRequest,
                "ACCESS-TOKEN",
                AuthInformation.LOCATION_HEADER,
                AuthInformation.TOKEN_TYPE_JWT);

        //인증이 실패한경우의 처리.
        if (authInformation.getError() != null) {
            gatewayService.errorResponse(
                    GateException.AUTHENTICATION_FAIL,
                    servletRequest,
                    servletResponse,
                    null);
            return;
        }

        //인증이 성공할 경우 프록시 처리
        ProxyRequest proxyRequest = new ProxyRequest();
        proxyRequest.setProxyServlet(gatewayServlet);
        proxyRequest.setRequest(servletRequest);
        proxyRequest.setResponse(servletResponse);
        proxyRequest.setHost("http://52.79.164.208:5984");

        proxyRequest.setPath(servletRequest.getPathInfo().replace("/cloudant", ""));
        proxyService.doProxy(proxyRequest);
    }
}
