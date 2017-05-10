package org.opencloudengine.garuda.handler.activity.workflow;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.opencloudengine.garuda.handler.activity.workflow.data.ApiInput;
import org.opencloudengine.garuda.handler.activity.workflow.data.ApiOutput;
import org.opencloudengine.garuda.handler.activity.workflow.script.HttpRequest;
import org.opencloudengine.garuda.script.ScriptResponse;
import org.opencloudengine.garuda.util.HttpUtils;
import org.opencloudengine.garuda.util.StringUtils;
import org.uengine.kernel.ActivityInstanceContext;
import org.uengine.kernel.ProcessDefinition;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by uengine on 2016. 7. 5..
 */
public class ApiTask extends WorkflowInterceptorTask {

    @Override
    public void runTask() throws Exception {
        //인풋 데이터
        ApiInput input = new ApiInput();
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
        HttpRequest httpRequest = objectMapper.convertValue(result, HttpRequest.class);
        if (StringUtils.isEmpty(httpRequest.uri)) {
            throw new Exception("uri required in return value in script: " + script);
        }

        if (StringUtils.isEmpty(httpRequest.method)) {
            throw new Exception("method required in return value in script: " + script);
        }

        if (StringUtils.isEmpty(httpRequest.body)) {
            httpRequest.body = "";
        }

        if (httpRequest.headers == null) {
            throw new Exception("headers required in return value in script: " + script);
        }

        //Api 실행
        HttpUtils httpUtils = new HttpUtils();
        HttpResponse httpResponse = httpUtils.makeRequest(httpRequest.method, httpRequest.uri, httpRequest.body, httpRequest.headers);

        //리스폰스 엔티티
        String entity = EntityUtils.toString(httpResponse.getEntity());

        //리스폰스 스테이터스 코드
        int statusCode = httpResponse.getStatusLine().getStatusCode();

        //리스폰스 헤더
        Map headerMap = new HashMap();
        Header[] headers = httpResponse.getAllHeaders();
        for (int i = 0; i < headers.length; i++) {
            Header header = headers[i];
            headerMap.put(header.getName(), header.getValue());
        }

        //아웃풋 저장
        ApiOutput output = new ApiOutput();
        output.setEntity(entity);
        output.setStatus(statusCode);
        output.setHeaders(headerMap);
        outputData = output;

        //다음단계 선택
        globalAttributes.setTaskNext(instance, taskId, "Response");
    }
}
