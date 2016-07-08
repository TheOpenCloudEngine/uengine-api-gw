package org.opencloudengine.garuda.script;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.velocity.app.VelocityEngine;
import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.tools.shell.Global;
import org.mozilla.javascript.tools.shell.Main;
import org.opencloudengine.garuda.authentication.AuthenticationService;
import org.opencloudengine.garuda.handler.GateHandlerService;
import org.opencloudengine.garuda.model.AuthInformation;
import org.opencloudengine.garuda.model.OauthClient;
import org.opencloudengine.garuda.model.OauthUser;
import org.opencloudengine.garuda.model.User;
import org.opencloudengine.garuda.proxy.ProxyService;
import org.opencloudengine.garuda.util.JsonUtils;
import org.opencloudengine.garuda.util.ResourceUtils;
import org.opencloudengine.garuda.web.configuration.ConfigurationHelper;
import org.opencloudengine.garuda.web.iam.IamService;
import org.opencloudengine.garuda.web.uris.ResourceUriRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.ui.velocity.VelocityEngineUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentMap;
import java.util.function.BiConsumer;

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

        Boolean value = request.embed("client", authInformation.getOauthClient()).
                embed("user", authInformation.getOauthUser()).
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

        request.embed("client", authInformation.getOauthClient()).
                embed("user", authInformation.getOauthUser()).
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
            request = request.embed(taskName, data);
        }
        return request.setScript(script).build();
    }
}
