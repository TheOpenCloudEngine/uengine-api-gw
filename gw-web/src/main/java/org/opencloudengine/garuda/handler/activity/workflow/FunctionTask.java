package org.opencloudengine.garuda.handler.activity.workflow;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.opencloudengine.garuda.handler.activity.workflow.data.FunctionInput;
import org.opencloudengine.garuda.handler.activity.workflow.data.FunctionOutput;
import org.opencloudengine.garuda.handler.activity.workflow.script.FunctionResponse;
import org.opencloudengine.garuda.script.ScriptResponse;
import org.opencloudengine.garuda.util.StringUtils;

import java.util.Map;

/**
 * Created by uengine on 2016. 7. 5..
 */
public class FunctionTask extends WorkflowInterceptorTask {

    @Override
    public void runTask() throws Exception {
        //인풋 데이터
        FunctionInput input = new FunctionInput();
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
        FunctionResponse functionResponse = objectMapper.convertValue(result, FunctionResponse.class);
        if (StringUtils.isEmpty(functionResponse.flow)) {
            throw new Exception("flow required in return value in script: " + script);
        }

        if (functionResponse.data == null) {
            throw new Exception("data required in return value in script: " + script);
        }

        //아웃풋 저장
        FunctionOutput output = new FunctionOutput();
        output.setData(functionResponse.data);
        outputData = output;

        //다음단계 선택
        globalAttributes.setTaskNext(instance, taskId, functionResponse.flow);
    }
}
