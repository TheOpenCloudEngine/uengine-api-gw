package org.opencloudengine.garuda.handler.activity.workflow.data;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by uengine on 2016. 7. 7..
 */
public class ProxyInput implements Serializable {

    private Map<String,Object> scriptData;

    public Map<String, Object> getScriptData() {
        return scriptData;
    }

    public void setScriptData(Map<String, Object> scriptData) {
        this.scriptData = scriptData;
    }
}
