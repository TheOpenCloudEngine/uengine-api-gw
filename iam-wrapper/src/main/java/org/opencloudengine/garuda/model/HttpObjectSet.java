package org.opencloudengine.garuda.model;

import org.opencloudengine.garuda.gateway.GatewayServlet;
import org.opencloudengine.garuda.web.uris.ResourceUri;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by uengine on 2016. 7. 7..
 */
public class HttpObjectSet {

    private GatewayServlet servlet;

    private HttpServletRequest servletRequest;

    private HttpServletResponse servletResponse;

    private ResourceUri resourceUri;

    public HttpObjectSet(GatewayServlet servlet, HttpServletRequest servletRequest, HttpServletResponse servletResponse, ResourceUri resourceUri) {
        this.servlet = servlet;
        this.servletRequest = servletRequest;
        this.servletResponse = servletResponse;
        this.resourceUri = resourceUri;
    }

    public GatewayServlet getServlet() {
        return servlet;
    }

    public void setServlet(GatewayServlet servlet) {
        this.servlet = servlet;
    }

    public HttpServletRequest getServletRequest() {
        return servletRequest;
    }

    public void setServletRequest(HttpServletRequest servletRequest) {
        this.servletRequest = servletRequest;
    }

    public HttpServletResponse getServletResponse() {
        return servletResponse;
    }

    public void setServletResponse(HttpServletResponse servletResponse) {
        this.servletResponse = servletResponse;
    }

    public ResourceUri getResourceUri() {
        return resourceUri;
    }

    public void setResourceUri(ResourceUri resourceUri) {
        this.resourceUri = resourceUri;
    }
}
