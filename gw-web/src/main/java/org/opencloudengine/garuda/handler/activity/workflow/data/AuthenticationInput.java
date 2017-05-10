package org.opencloudengine.garuda.handler.activity.workflow.data;

import java.io.Serializable;

/**
 * Created by uengine on 2016. 7. 7..
 */
public class AuthenticationInput implements Serializable {

    private String tokenLocation;
    private String tokenName;

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
}
