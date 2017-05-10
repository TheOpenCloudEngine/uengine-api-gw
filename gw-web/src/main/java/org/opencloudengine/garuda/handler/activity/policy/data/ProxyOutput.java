package org.opencloudengine.garuda.handler.activity.policy.data;

import java.io.Serializable;

/**
 * Created by uengine on 2016. 7. 7..
 */
public class ProxyOutput implements Serializable {

    public static String SUCCEEDED = "succeeded";
    public static String FAILED = "failed";

    private String status;

    private String host;

    private String path;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

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
