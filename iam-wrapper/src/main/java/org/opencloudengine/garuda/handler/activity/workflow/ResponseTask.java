package org.opencloudengine.garuda.handler.activity.workflow;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.opencloudengine.garuda.handler.activity.workflow.data.ResponseInput;
import org.opencloudengine.garuda.handler.activity.workflow.data.ResponseOutput;
import org.opencloudengine.garuda.handler.activity.workflow.script.HttpResponse;
import org.opencloudengine.garuda.script.ScriptResponse;
import org.opencloudengine.garuda.util.StringUtils;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by uengine on 2016. 7. 5..
 */
public class ResponseTask extends WorkflowInterceptorTask {

    @Override
    public void runTask() throws Exception {
        //인풋 데이터
        ResponseInput input = new ResponseInput();
        input.setScriptData(globalAttributes.getAllTaskOutput(instance));
        inputData = input;

        //스크립트 실행
        String script = params.get("script");
        if (StringUtils.isEmpty(script)) {
            throw new Exception("script required in this task: " + taskName);
        }

        //스크립트 로그 저장
        ScriptResponse response = scriptService.workflowScript(script, input.getScriptData());
        stdout = response.getLog();

        Map result = response.castValue(Map.class);
        ObjectMapper objectMapper = new ObjectMapper();
        HttpResponse httpResponse = objectMapper.convertValue(result, HttpResponse.class);
        if (httpResponse.status == null) {
            throw new Exception("status required in return value in script: " + script);
        }

        if (StringUtils.isEmpty(httpResponse.entity)) {
            throw new Exception("entity required in return value in script: " + script);
        }

        if (httpResponse.headers == null) {
            throw new Exception("headers required in return value in script: " + script);
        }

        //리스폰스 전달
        Set keySet = httpResponse.headers.keySet();
        Iterator iterator = keySet.iterator();
        while (iterator.hasNext()) {
            String headerName = (String) iterator.next();
            String headerValue = (String) httpResponse.headers.get(headerName);
            servletResponse.setHeader(headerName, headerValue);
        }
        servletResponse.setStatus(httpResponse.status);
        servletResponse.getWriter().write(httpResponse.entity);

        //아웃풋 저장
        ResponseOutput output = new ResponseOutput();
        output.setEntity(httpResponse.entity);
        output.setStatus(httpResponse.status);
        output.setHeaders(httpResponse.headers);
        outputData = output;

        //다음단계 선택
        globalAttributes.setTaskNext(instance, taskId, "Response");
    }
}
