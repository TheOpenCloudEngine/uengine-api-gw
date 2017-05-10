package org.opencloudengine.garuda.handler.activity.workflow.script;

import java.util.Map;

/**
 * Created by uengine on 2016. 7. 8..
 */
public class HttpResponse {
    public Integer status;
    public Map headers;
    public String entity;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Map getHeaders() {
        return headers;
    }

    public void setHeaders(Map headers) {
        this.headers = headers;
    }

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }
}
