package org.opencloudengine.garuda.handler.activity.policy.data;

import org.opencloudengine.garuda.handler.activity.workflow.data.RequestInput;
import org.opencloudengine.garuda.model.AuthInformation;
import org.opencloudengine.garuda.util.JsonUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by uengine on 2016. 7. 7..
 */
public class BeforeInput implements Serializable {

    public BeforeInput(RequestInput requestInput, AuthInformation authInformation) {
        Map<String, Object> map = new HashMap();
        try {
            map.put("Request", JsonUtils.convertClassToMap(requestInput));
            map.put("client", JsonUtils.convertClassToMap(authInformation.getOauthClient()));
            map.put("user", JsonUtils.convertClassToMap(authInformation.getOauthUser()));
            map.put("scope", authInformation.getScopes());
            map.put("token_type", authInformation.getTokenType());
            map.put("claim", authInformation.getClaim());
            map.put("type", authInformation.getType());
        } catch (Exception ex) {

        } finally {
            this.scriptData = map;
        }
    }

    private Map<String, Object> scriptData;

    public Map<String, Object> getScriptData() {
        return scriptData;
    }

    public void setScriptData(Map<String, Object> scriptData) {
        this.scriptData = scriptData;
    }
}
