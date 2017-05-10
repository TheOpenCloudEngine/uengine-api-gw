package org.opencloudengine.garuda.handler.activity.workflow.data;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by uengine on 2016. 7. 7..
 */
public class RequestOutput implements Serializable{
    private String uri;
    private Map pathVarialbe;
    private Map headers;
    private String method;

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public Map getPathVarialbe() {
        return pathVarialbe;
    }

    public void setPathVarialbe(Map pathVarialbe) {
        this.pathVarialbe = pathVarialbe;
    }

    public Map getHeaders() {
        return headers;
    }

    public void setHeaders(Map headers) {
        this.headers = headers;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }
}
