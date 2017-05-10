package org.opencloudengine.garuda.script;

import org.apache.velocity.app.VelocityEngine;
import org.opencloudengine.garuda.handler.activity.policy.data.BeforeInput;
import org.opencloudengine.garuda.handler.activity.workflow.data.RequestInput;
import org.opencloudengine.garuda.model.AuthInformation;
import org.opencloudengine.garuda.util.JsonUtils;
import org.opencloudengine.garuda.web.configuration.ConfigurationHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

@Service
public class ScriptServiceImpl implements ScriptService {
    @Autowired
    @Qualifier("config")
    private Properties config;

    @Autowired
    ConfigurationHelper configurationHelper;

    @Autowired
    private VelocityEngine velocityEngine;

    /**
     * SLF4J Logging
     */
    private Logger logger = LoggerFactory.getLogger(ScriptServiceImpl.class);

    @Override
    public ScriptResponse beforeUseScript(String script, BeforeInput beforeInput) throws Exception {
        ScriptRequest request = new ScriptRequest();
        Map<String, Object> scriptData = beforeInput.getScriptData();

        Set<String> keySet = scriptData.keySet();
        Iterator<String> iterator = keySet.iterator();
        while (iterator.hasNext()) {
            String taskName = iterator.next();
            Object data = scriptData.get(taskName);
            request = request.embed(taskName, data);
        }
        return request.setScript(script).build();
    }

    @Override
    public ScriptResponse afterUseScript(String script, BeforeInput beforeInput) throws Exception {
        ScriptRequest request = new ScriptRequest();
        Map<String, Object> scriptData = beforeInput.getScriptData();

        Set<String> keySet = scriptData.keySet();
        Iterator<String> iterator = keySet.iterator();
        while (iterator.hasNext()) {
            String taskName = iterator.next();
            Object data = scriptData.get(taskName);
            request = request.embed(taskName, data);
        }
        return request.setScript(script).build();
    }

    @Override
    public ScriptResponse workflowScript(String script, Map<String, Object> taskOutputData) throws Exception {
        ScriptRequest request = new ScriptRequest();

        Set<String> keySet = taskOutputData.keySet();
        Iterator<String> iterator = keySet.iterator();
        while (iterator.hasNext()) {
            String taskName = iterator.next();
            Object data = taskOutputData.get(taskName);
            request = request.embed(taskName, JsonUtils.convertClassToMap(data));
        }
        return request.setScript(script).build();
    }
}
