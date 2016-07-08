package org.opencloudengine.garuda.handler.activity.workflow.script;

import java.util.Map;

/**
 * Created by uengine on 2016. 7. 8..
 */
public class HttpRequest {
    public String uri;
    public String method;
    public Map headers;
    public String body;

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Map getHeaders() {
        return headers;
    }

    public void setHeaders(Map headers) {
        this.headers = headers;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
