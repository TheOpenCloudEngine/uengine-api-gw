package org.opencloudengine.garuda.gateway;

import org.opencloudengine.garuda.model.GateResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface GatewayService {

    void start(GatewayServlet servlet, HttpServletRequest servletRequest, HttpServletResponse servletResponse);

    void processResponse(GateResponse gateResponse);

    void errorResponse(String code, HttpServletRequest servletRequest, HttpServletResponse servletResponse, String msg);
}
