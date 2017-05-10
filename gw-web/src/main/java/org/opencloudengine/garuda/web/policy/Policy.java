package org.opencloudengine.garuda.web.policy;

import org.opencloudengine.garuda.couchdb.CouchDAO;

/**
 * Created by uengine on 2016. 6. 21..
 */
public class Policy extends CouchDAO {

    private String name;
    private String authentication;
    private String tokenLocation;
    private String tokenName;
    private String proxyUri;
    private String prefixUri;
    private String beforeUse;
    private String afterUse;
    private Long regDate;
    private Long updDate;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthentication() {
        return authentication;
    }

    public void setAuthentication(String authentication) {
        this.authentication = authentication;
    }

    public String getTokenLocation() {
        return tokenLocation;
    }

    public void setTokenLocation(String tokenLocation) {
        this.tokenLocation = tokenLocation;
    }

    public String getTokenName() {
        return tokenName;
    }

    public void setTokenName(String tokenName) {
        this.tokenName = tokenName;
    }

    public String getProxyUri() {
        return proxyUri;
    }

    public void setProxyUri(String proxyUri) {
        this.proxyUri = proxyUri;
    }

    public String getPrefixUri() {
        return prefixUri;
    }

    public void setPrefixUri(String prefixUri) {
        this.prefixUri = prefixUri;
    }

    public String getBeforeUse() {
        return beforeUse;
    }

    public void setBeforeUse(String beforeUse) {
        this.beforeUse = beforeUse;
    }

    public String getAfterUse() {
        return afterUse;
    }

    public void setAfterUse(String afterUse) {
        this.afterUse = afterUse;
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
