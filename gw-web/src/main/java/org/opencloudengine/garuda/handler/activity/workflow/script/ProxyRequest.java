package org.opencloudengine.garuda.handler.activity.workflow.script;

import java.util.Map;

/**
 * Created by uengine on 2016. 7. 8..
 */
public class ProxyRequest {
    public String host;
    public String path;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
