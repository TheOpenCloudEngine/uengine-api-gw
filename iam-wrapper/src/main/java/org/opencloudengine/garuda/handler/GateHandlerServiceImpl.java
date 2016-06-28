package org.opencloudengine.garuda.handler;

import org.opencloudengine.garuda.gateway.GateException;
import org.opencloudengine.garuda.gateway.GatewayService;
import org.opencloudengine.garuda.gateway.GatewayServlet;
import org.opencloudengine.garuda.handler.activity.policy.PolicyHandler;
import org.opencloudengine.garuda.web.configuration.ConfigurationHelper;
import org.opencloudengine.garuda.web.uris.ResourceUri;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Properties;

@Service
public class GateHandlerServiceImpl implements GateHandlerService {
    @Autowired
    @Qualifier("config")
    private Properties config;

    @Autowired
    ConfigurationHelper configurationHelper;

    @Autowired
    GatewayService gatewayService;

    /**
     * SLF4J Logging
     */
    private Logger logger = LoggerFactory.getLogger(GateHandlerServiceImpl.class);


    @Override
    public void doPolicyHandler(ResourceUri resourceUri, GatewayServlet servlet, HttpServletRequest servletRequest, HttpServletResponse servletResponse) {
        try{
            PolicyHandler policyHandler = new PolicyHandler();
            policyHandler.init(resourceUri, servlet, servletRequest, servletResponse);
            policyHandler.doAction();
        }catch (Exception ex){
            gatewayService.errorResponse(GateException.SERVER_ERROR, servletRequest, servletResponse, null);
        }
    }

    @Override
    public void doClassHandler(ResourceUri resourceUri, GatewayServlet servlet, HttpServletRequest servletRequest, HttpServletResponse servletResponse) {

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
            Method init = act.getMethod("init", ResourceUri.class, GatewayServlet.class, HttpServletRequest.class, HttpServletResponse.class);
            init.setAccessible(true);
            init.invoke(obj, new Object[]{resourceUri, servlet, servletRequest, servletResponse});

            Method doAction = act.getMethod("doAction");
            doAction.setAccessible(true);
            doAction.invoke(obj, new Object[]{});
        } catch (Exception ex) {
            gatewayService.errorResponse(GateException.SERVER_ERROR, servletRequest, servletResponse, null);
        }
    }
}
