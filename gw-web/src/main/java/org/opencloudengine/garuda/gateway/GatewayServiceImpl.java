package org.opencloudengine.garuda.gateway;

import org.apache.commons.lang.ArrayUtils;
import org.opencloudengine.garuda.handler.GateHandlerService;
import org.opencloudengine.garuda.model.GateResponse;
import org.opencloudengine.garuda.proxy.ProxyService;
import org.opencloudengine.garuda.util.JsonFormatterUtils;
import org.opencloudengine.garuda.util.JsonUtils;
import org.opencloudengine.garuda.web.configuration.ConfigurationHelper;
import org.opencloudengine.garuda.web.uris.ResourceUri;
import org.opencloudengine.garuda.web.uris.ResourceUriRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@Service
public class GatewayServiceImpl implements GatewayService {
    @Autowired
    @Qualifier("config")
    private Properties config;

    @Autowired
    ConfigurationHelper configurationHelper;

    @Autowired
    ProxyService proxyService;

    @Autowired
    GateHandlerService handlerService;

    @Autowired
    ResourceUriRepository uriRepository;

    /**
     * SLF4J Logging
     */
    private Logger logger = LoggerFactory.getLogger(GatewayServiceImpl.class);

    @Override
    public void start(GatewayServlet servlet, HttpServletRequest servletRequest, HttpServletResponse servletResponse) {

        ResourceUri reourceUri = this.getReourceUri(servletRequest.getPathInfo(), servletRequest.getMethod());
        if (reourceUri == null) {
            this.errorResponse(GateException.NO_MAPPING_URI, servletRequest, servletResponse, null);
        } else if (reourceUri.getRunWith().equals(ResourceUri.WORKFLOW)) {
            handlerService.doWorkflowHandler(reourceUri, servlet, servletRequest, servletResponse);
        } else if (reourceUri.getRunWith().equals(ResourceUri.POLICY)) {
            handlerService.doPolicyHandler(reourceUri, servlet, servletRequest, servletResponse);
        } else if (reourceUri.getRunWith().equals(ResourceUri.CLASS)) {
            handlerService.doClassHandler(reourceUri, servlet, servletRequest, servletResponse);
        } else {
            this.errorResponse(GateException.NO_MAPPING_URI, servletRequest, servletResponse, null);
        }
    }

    @Override
    public void errorResponse(String code, HttpServletRequest servletRequest, HttpServletResponse servletResponse, String msg) {
        GateResponse gateResponse = new GateException().getResponse(code, servletRequest, msg);
        gateResponse.setResponse(servletResponse);
        this.processResponse(gateResponse);
    }

    @Override
    public void processResponse(GateResponse gateResponse) {
        try {
            HttpServletResponse response = gateResponse.getResponse();
            if (gateResponse.getError() != null) {
                Map map = new HashMap();
                map.put("error", gateResponse.getError());
                map.put("error_description", gateResponse.getError_description());

                String marshal = JsonUtils.marshal(map);
                String prettyPrint = JsonFormatterUtils.prettyPrint(marshal);

                response.setStatus(400);
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                response.setHeader("Cache-Control", "no-store");
                response.setHeader("Pragma", "no-cache");
                response.getWriter().write(prettyPrint);
            } else {
                response.setStatus(200);
                //response.setHeader("Content-Type", "application/json;charset=UTF-8");
                response.setHeader("Content-Type", gateResponse.getContentType());
                response.setHeader("Cache-Control", "no-store");
                response.setHeader("Pragma", "no-cache");
                response.getWriter().write(gateResponse.getContent());
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private ResourceUri getReourceUri(String pathInfo, String method) {

        ResourceUri matchUri = null;

        /**
         이때 캐쉬는 이미 order 순으로 소트가 되어있다.
         **/
        List<ResourceUri> cash = uriRepository.getCash();

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


        for (ResourceUri resourceUri : cash) {
            String uri = resourceUri.getUri();
            if (uri.startsWith("/")) {
                uri = uri.substring(1);
            }
            if (uri.endsWith("/")) {
                uri = uri.substring(0, uri.length() - 1);
            }
            String[] split1 = uri.split("/");
            List<String> uris = Arrays.asList(split1);

            boolean match = false;

            /**
             uri 가 "/" 이거나 "" 이라면 모든 경우에 통과
             **/
            if (uris.size() == 0) {
                match = true;
            }
            for (int i = 0; i < uris.size(); i++) {
                String uriPart = uris.get(i);

                /**
                 uriPart 가 "*" 이고 해당 순차가 마지막 번호라면 통과
                 **/
                if (uriPart.equals("*") && i == uris.size() - 1) {
                    match = true;
                    break;
                }

                /**
                 uriPart 가 "{}" 이고 paths 와 함께 마지막 번호일경우 통과.
                 **/
                if (uriPart.equals("{}") && i == uris.size() - 1 && uris.size() == paths.size()) {
                    match = true;
                    break;
                }

                /**
                 uriPart 가 string 이고 paths 와 함께 마지막 번호이며, paths 의 값도 동일한 string 일 경우 통과.
                 **/
                if (uriPart.getClass().equals(String.class) && i == uris.size() - 1 && uris.size() == paths.size()) {
                    if (uriPart.equals(paths.get(i))) {
                        match = true;
                        break;
                    }
                }

                /**
                 paths 가 uriPart 의 순차를 따라오지 못할경우 탈락.
                 **/
                if (paths.size() <= i) {
                    match = false;
                    break;
                }

                /**
                 uriPart 가 * 또는 {} 일때를 제외하고 paths 가 일치하지 않으면 탈락.
                 **/

                if (!uriPart.equals("*") && !uriPart.startsWith("{")) {
                    if (!uriPart.equals(paths.get(i))) {
                        match = false;
                        break;
                    }
                }
            }

            if (match) {
                //메소드 매칭 확인 절차
                String[] methods = resourceUri.getMethod().split(",");
                List<String> methodsArray = Arrays.asList(methods);
                if (methodsArray.contains(method.toUpperCase())) {
                    matchUri = resourceUri;
                }
                break;
            }
        }

        return matchUri;
    }
}
