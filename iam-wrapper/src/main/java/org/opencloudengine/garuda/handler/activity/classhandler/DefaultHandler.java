package org.opencloudengine.garuda.handler.activity.classhandler;

import org.opencloudengine.garuda.gateway.GateException;
import org.opencloudengine.garuda.handler.AbstractHandler;
import org.opencloudengine.garuda.proxy.ProxyRequest;

/**
 * Created by uengine on 2016. 6. 16..
 */
public class DefaultHandler extends AbstractHandler {

    @Override
    public void doAction() {


        ProxyRequest proxyRequest = new ProxyRequest();
        proxyRequest.setProxyServlet(this.getGatewayServlet());
        proxyRequest.setRequest(this.getServletRequest());
        proxyRequest.setResponse(this.getServletResponse());

        //에센시아 사이트로 이동
        proxyRequest.setHost("http://www.essencia.live");

        proxyRequest.setPath(this.getServletRequest().getPathInfo());
        try {
            proxyService.doProxy(proxyRequest);
        } catch (Exception ex) {
            gatewayService.errorResponse(GateException.PROXY_FAILED, servletRequest, servletResponse, null);
        }
    }



}
