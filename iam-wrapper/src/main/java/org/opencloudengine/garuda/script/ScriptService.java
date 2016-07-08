package org.opencloudengine.garuda.script;

import org.opencloudengine.garuda.model.AuthInformation;
import org.opencloudengine.garuda.model.OauthClient;
import org.opencloudengine.garuda.model.OauthUser;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public interface ScriptService {

    boolean beforeUseScript(String script, AuthInformation authInformation) throws Exception;

    void afterUseScript(String script, AuthInformation authInformation) throws Exception;

    ScriptResponse workflowScript(String script, Map<String,Object> taskOutputData) throws Exception;
}
