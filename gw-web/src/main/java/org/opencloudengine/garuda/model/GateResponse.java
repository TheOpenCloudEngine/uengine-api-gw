package org.opencloudengine.garuda.model;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by uengine on 2016. 6. 16..
 */
public class GateResponse {

    private HttpServletResponse response;

    private String contentType;
    private String content;

    private String error;
    private String error_description;

    public HttpServletResponse getResponse() {
        return response;
    }

    public void setResponse(HttpServletResponse response) {
        this.response = response;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getError_description() {
        return error_description;
    }

    public void setError_description(String error_description) {
        this.error_description = error_description;
    }
}
