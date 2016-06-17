package org.opencloudengine.garuda.web.uris;

import org.opencloudengine.garuda.couchdb.CouchDAO;

/**
 * Created by uengine on 2016. 5. 30..
 */
public class ResourceUri extends CouchDAO {

    private int order;
    private String uri;
    private String method;
    private String runWith;
    private String wid;
    private String wName;
    private String className;
    private Long regDate;
    private Long updDate;

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

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

    public String getRunWith() {
        return runWith;
    }

    public void setRunWith(String runWith) {
        this.runWith = runWith;
    }

    public String getWid() {
        return wid;
    }

    public void setWid(String wid) {
        this.wid = wid;
    }

    public String getwName() {
        return wName;
    }

    public void setwName(String wName) {
        this.wName = wName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Long getRegDate() {
        return regDate;
    }

    public void setRegDate(Long regDate) {
        this.regDate = regDate;
    }

    public Long getUpdDate() {
        return updDate;
    }

    public void setUpdDate(Long updDate) {
        this.updDate = updDate;
    }
}
