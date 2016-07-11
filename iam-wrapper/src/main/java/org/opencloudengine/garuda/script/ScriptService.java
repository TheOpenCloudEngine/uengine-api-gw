package org.opencloudengine.garuda.script;

import org.opencloudengine.garuda.handler.activity.policy.data.BeforeInput;
import java.util.Map;

public interface ScriptService {

    ScriptResponse beforeUseScript(String script, BeforeInput beforeInput) throws Exception;

    ScriptResponse afterUseScript(String script, BeforeInput beforeInput) throws Exception;

    ScriptResponse workflowScript(String script, Map<String, Object> taskOutputData) throws Exception;
}
