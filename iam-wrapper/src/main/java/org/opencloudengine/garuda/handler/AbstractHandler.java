package org.opencloudengine.garuda.handler;

import org.opencloudengine.garuda.gateway.GatewayService;
import org.opencloudengine.garuda.gateway.GatewayServlet;
import org.opencloudengine.garuda.proxy.ProxyService;
import org.opencloudengine.garuda.authentication.AuthenticationService;
import org.opencloudengine.garuda.script.ScriptService;
import org.opencloudengine.garuda.util.ApplicationContextRegistry;
import org.opencloudengine.garuda.web.policy.PolicyService;
import org.opencloudengine.garuda.web.uris.ResourceUri;
import org.springframework.context.ApplicationContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by uengine on 2016. 6. 16..
 */
public class AbstractHandler {

    public ResourceUri resourceUri;
    public HttpServletRequest servletRequest;
    public HttpServletResponse servletResponse;
    public GatewayServlet gatewayServlet;

    public ProxyService proxyService;
    public AuthenticationService securityService;
    public GatewayService gatewayService;
    public PolicyService policyService;
    public ScriptService scriptService;

    public Map pathVarialbe;

    public ResourceUri getResourceUri() {
        return resourceUri;
    }

    public void setResourceUri(ResourceUri resourceUri) {
        this.resourceUri = resourceUri;
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

    public GatewayServlet getGatewayServlet() {
        return gatewayServlet;
    }

    public void setGatewayServlet(GatewayServlet gatewayServlet) {
        this.gatewayServlet = gatewayServlet;
    }

    public Map getPathVarialbe() {
        return pathVarialbe;
    }

    public void setPathVarialbe(Map pathVarialbe) {
        this.pathVarialbe = pathVarialbe;
    }

    public void init(ResourceUri resourceUri, GatewayServlet gatewayServlet, HttpServletRequest servletRequest, HttpServletResponse servletResponse) {
        this.resourceUri = resourceUri;
        this.servletRequest = servletRequest;
        this.servletResponse = servletResponse;
        this.gatewayServlet = gatewayServlet;

        this.pathVarialbe = this.createPathVarialbe(resourceUri, servletRequest.getPathInfo());

        ApplicationContext context = ApplicationContextRegistry.getApplicationContext();
        proxyService = context.getBean(ProxyService.class);
        securityService = context.getBean(AuthenticationService.class);
        gatewayService = context.getBean(GatewayService.class);
        policyService = context.getBean(PolicyService.class);
        scriptService = context.getBean(ScriptService.class);

    }

    public void doAction() {

    }

    private Map createPathVarialbe(ResourceUri resourceUri, String pathInfo) {

        Map pathVariable = new HashMap<String, String>();
        /**
         pathInfo 를 처음 "/" 과 마지막 "?,#" 를 정리한다.
         정리한 후에 마지막이 "/" 이라면 정리한다.
         **/
        if (pathInfo.startsWith("/")) {
            pathInfo = pathInfo.substring(1);
        }
        if (pathInfo.contains("?")) {
            pathInfo = pathInfo.substring(0, pathInfo.indexOf("?"));
        }
        if (pathInfo.contains("#")) {
            pathInfo = pathInfo.substring(0, pathInfo.indexOf("#"));
        }
        if (pathInfo.endsWith("/")) {
            pathInfo = pathInfo.substring(0, pathInfo.length() - 1);
        }

        /**
         pathInfo 를 "/" 순차로 정리 : paths
         **/
        String[] split = pathInfo.split("/");
        List<String> paths = Arrays.asList(split);


        /**
         uri 를 처음 "/" 과 마지막 "/" 를 정리한다.
         **/
        String uri = resourceUri.getUri();
        if (uri.startsWith("/")) {
            uri = uri.substring(1);
        }
        if (uri.endsWith("/")) {
            uri = uri.substring(0, uri.length() - 1);
        }
        String[] split1 = uri.split("/");
        List<String> uris = Arrays.asList(split1);


        for (int i = 0; i < uris.size(); i++) {
            String uriPart = uris.get(i);

            if (uriPart.startsWith("{") && uriPart.endsWith("}")) {
                String pathValue = paths.get(i);
                String pathKey = uriPart.replace("{", "").replace("}", "");
                pathVariable.put(pathKey, pathValue);
            }
        }
        return pathVariable;
    }
}
