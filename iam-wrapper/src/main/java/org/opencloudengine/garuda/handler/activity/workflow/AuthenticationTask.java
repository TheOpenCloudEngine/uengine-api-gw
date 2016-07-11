package org.opencloudengine.garuda.handler.activity.workflow;

import org.opencloudengine.garuda.handler.activity.workflow.data.AuthenticationInput;
import org.opencloudengine.garuda.handler.activity.workflow.data.AuthenticationOutput;
import org.opencloudengine.garuda.model.AuthInformation;

/**
 * Created by uengine on 2016. 7. 5..
 */
public class AuthenticationTask extends WorkflowInterceptorTask {

    @Override
    public void runTask() throws Exception {

        //리퀘스트 인풋 등록.
        AuthenticationInput input = new AuthenticationInput();
        input.setTokenLocation(params.get("location").toString());
        input.setTokenName(params.get("name").toString());
        inputData = input;

        //토큰을 인증한다.
        AuthInformation authInformation = securityService.validateRequest(
                servletRequest,
                input.getTokenName(),
                input.getTokenLocation(),
                null);

        //아웃풋 등록
        AuthenticationOutput output = new AuthenticationOutput();
        output.setTokenName(input.getTokenName());
        output.setTokenLocation(input.getTokenLocation());
        output.setToken(authInformation.getToken());
        output.setOauthClient(authInformation.getOauthClient());
        output.setOauthUser(authInformation.getOauthUser());
        output.setScopes(authInformation.getScopes());
        output.setTokenType(authInformation.getTokenType());
        output.setType(authInformation.getType());
        output.setClaim(authInformation.getClaim());
        outputData = output;

        //인증이 실패한경우의 처리.
        if (authInformation.getError() != null) {
            globalAttributes.setTaskNext(instance, taskId, "NO");
        } else {
            globalAttributes.setTaskNext(instance, taskId, "YES");
        }
    }
}
