package org.opencloudengine.garuda.handler.activity.workflow;

import org.apache.http.HttpHeaders;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.opencloudengine.garuda.handler.activity.workflow.data.RequestInput;
import org.opencloudengine.garuda.handler.activity.workflow.data.RequestOutput;
import org.opencloudengine.garuda.util.StringUtils;
import org.uengine.kernel.ProcessInstance;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by uengine on 2016. 7. 5..
 */
public class RequestTask extends InterceptorAbstractTask {

    @Override
    public void runTask() throws Exception {

        Map headers = new HashMap();
        Enumeration enumerationOfHeaderNames = servletRequest.getHeaderNames();
        while (enumerationOfHeaderNames.hasMoreElements()) {
            String headerName = (String) enumerationOfHeaderNames.nextElement();
            String headerValue = getRequestHeader(headerName);
            headers.put(headerName, headerValue);
        }

        //인풋데이터 등록.
        RequestInput input = new RequestInput();
        input.setHeaders(headers);
        input.setMethod(servletRequest.getMethod());
        input.setPathVarialbe(pathVarialbe);
        input.setUri(servletRequest.getPathInfo());
        inputData = input;

        //아웃풋데이터 등록.
        RequestOutput output = new RequestOutput();
        output.setHeaders(headers);
        output.setMethod(servletRequest.getMethod());
        output.setPathVarialbe(pathVarialbe);
        output.setUri(servletRequest.getPathInfo());
        outputData = output;

        //넥스트 시퀀스 등록.
        globalAttributes.setTaskNext(instance, taskId, "Request");
    }

    private String getRequestHeader(String headerName) {
        String headerValues = null;
        Enumeration headers = servletRequest.getHeaders(headerName);
        while (headers.hasMoreElements()) {//sometimes more than one value
            String headerValue = (String) headers.nextElement();
            if (StringUtils.isEmpty(headerValues)) {
                headerValues = headerValue;
            } else {
                headerValues = headerValues + "," + headerValue;
            }
        }
        return headerValues;
    }
}
