package org.opencloudengine.garuda.handler.activity.workflow.data;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by uengine on 2016. 7. 7..
 */
public class ApiOutput implements Serializable {

    private int status;
    private Map headers;
    private String entity;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
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
