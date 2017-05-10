package org.opencloudengine.garuda.handler.activity.policy.data;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by uengine on 2016. 7. 7..
 */
public class BeforeOutput implements Serializable {

    private boolean continueProxy;

    public boolean getContinueProxy() {
        return continueProxy;
    }

    public void setContinueProxy(boolean continueProxy) {
        this.continueProxy = continueProxy;
    }
}
