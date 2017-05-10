package org.opencloudengine.garuda.handler.activity.workflow;

import org.opencloudengine.garuda.handler.activity.workflow.data.RequestInput;
import org.opencloudengine.garuda.handler.activity.workflow.data.RequestOutput;
import java.util.Map;

/**
 * Created by uengine on 2016. 7. 5..
 */
public class RequestTask extends WorkflowInterceptorTask {

    @Override
    public void runTask() throws Exception {

        Map headers = this.getHeaders();

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
}
