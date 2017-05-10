package org.opencloudengine.garuda.handler.activity.workflow.script;

/**
 * Created by uengine on 2016. 7. 8..
 */
public class FunctionResponse {
    public String flow;
    public Object data;

    public String getFlow() {
        return flow;
    }

    public void setFlow(String flow) {
        this.flow = flow;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
