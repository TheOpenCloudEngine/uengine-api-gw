package org.opencloudengine.garuda.handler.activity.policy.data;

import java.io.Serializable;

/**
 * Created by uengine on 2016. 7. 8..
 */
public class ProxyInput implements Serializable{
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
