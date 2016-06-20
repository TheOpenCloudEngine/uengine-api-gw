package org.opencloudengine.garuda.handler;

import org.opencloudengine.garuda.gateway.GatewayServlet;
import org.opencloudengine.garuda.web.uris.ResourceUri;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface HandlerServicea {

    void doStart(ResourceUri resourceUri, GatewayServlet servlet, HttpServletRequest servletRequest, HttpServletResponse servletResponse);
}