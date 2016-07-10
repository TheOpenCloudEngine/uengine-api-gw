package org.opencloudengine.garuda.script;

import org.apache.velocity.app.VelocityEngine;
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
    public boolean beforeUseScript(String script, AuthInformation authInformation) throws Exception {
        ScriptRequest request = new ScriptRequest();

        Map<String, Object> map = JsonUtils.convertClassToMap(null);

        Boolean value = request.embed("client", JsonUtils.convertClassToMap(authInformation.getOauthClient())).
                embed("user", JsonUtils.convertClassToMap(authInformation.getOauthUser())).
                embed("scope", authInformation.getScopes()).
                embed("token_type", authInformation.getTokenType()).
                embed("claim", authInformation.getClaim()).
                embed("type", authInformation.getType()).
                setScript(script).
                build().castValue(Boolean.class);

        return value;
    }

    @Override
    public void afterUseScript(String script, AuthInformation authInformation) throws Exception {
        ScriptRequest request = new ScriptRequest();

        request.embed("client", JsonUtils.convertClassToMap(authInformation.getOauthClient())).
                embed("user", JsonUtils.convertClassToMap(authInformation.getOauthUser())).
                embed("scope", authInformation.getScopes()).
                embed("token_type", authInformation.getTokenType()).
                embed("claim", authInformation.getClaim()).
                embed("type", authInformation.getType()).
                setScript(script).
                build();
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
