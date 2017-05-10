package org.opencloudengine.garuda.handler.activity.workflow;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.opencloudengine.garuda.gateway.GateException;
import org.opencloudengine.garuda.handler.activity.workflow.data.ProxyInput;
import org.opencloudengine.garuda.handler.activity.workflow.data.ProxyOutput;
import org.opencloudengine.garuda.handler.activity.workflow.script.ProxyRequest;
import org.opencloudengine.garuda.script.ScriptResponse;
import org.opencloudengine.garuda.util.ExceptionUtils;
import org.opencloudengine.garuda.util.StringUtils;

import java.util.Map;

/**
 * Created by uengine on 2016. 7. 5..
 */
public class ProxyTask extends WorkflowInterceptorTask {

    @Override
    public void runTask() throws Exception {
        //인풋 데이터
        ProxyInput input = new ProxyInput();
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
        ProxyRequest proxyRequest = objectMapper.convertValue(result, ProxyRequest.class);

        if (StringUtils.isEmpty(proxyRequest.host)) {
            throw new Exception("uri required in return value in script: " + script);
        }

        if (StringUtils.isEmpty(proxyRequest.path)) {
            throw new Exception("path required in return value in script: " + script);
        }

        String status = null;
        //Proxy 실행
        org.opencloudengine.garuda.proxy.ProxyRequest _proxyRequest = new org.opencloudengine.garuda.proxy.ProxyRequest();
        _proxyRequest.setProxyServlet(gatewayServlet);
        _proxyRequest.setRequest(servletRequest);
        _proxyRequest.setResponse(servletResponse);
        _proxyRequest.setHost(proxyRequest.getHost());
        _proxyRequest.setPath(proxyRequest.getPath());
        try {
            proxyService.doProxy(_proxyRequest);
            status = ProxyOutput.SUCCEEDED;
        } catch (Exception ex) {
            //TODO 여기서 바로 리스폰스를 보내지 말고 실패하였을 경우 실패원인을 파악하여 다음 타스크에 전달해주어야 한다.
            //TODO 그러기 위해서는 FAILED 뒤에는 Response 가 허용이 되고, SUCCEED 뒤에는 허용될 수 없다.
            //TODO 현재는 PROXY 객체 뒤에 Response 가 올 수 없는 구조로 일단 설계한다.
            gatewayService.errorResponse(GateException.PROXY_FAILED, servletRequest, servletResponse, null);
            stderr = ExceptionUtils.getFullStackTrace(ex);
            status = ProxyOutput.FAILED;
        }

        //아웃풋 저장
        ProxyOutput output = new ProxyOutput();
        output.setStatus(status);
        output.setHost(proxyRequest.getHost());
        output.setPath(proxyRequest.getPath());
        outputData = output;

        //다음단계 선택
        globalAttributes.setTaskNext(instance, taskId, status);
    }
}
