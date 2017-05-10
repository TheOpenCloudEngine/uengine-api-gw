package org.opencloudengine.garuda.proxy;

import org.opencloudengine.garuda.gateway.GatewayServlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by uengine on 2016. 6. 15..
 */
public class ProxyRequest {

    private GatewayServlet proxyServlet;

    private HttpServletRequest request;

    private HttpServletResponse response;

    private String host;

    private String path;

    public GatewayServlet getProxyServlet() {
        return proxyServlet;
    }

    public void setProxyServlet(GatewayServlet proxyServlet) {
        this.proxyServlet = proxyServlet;
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    public HttpServletResponse getResponse() {
        return response;
    }

    public void setResponse(HttpServletResponse response) {
        this.response = response;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
