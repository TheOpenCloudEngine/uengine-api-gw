package org.opencloudengine.garuda.handler.activity.workflow;

/**
 * Created by uengine on 2016. 7. 5..
 */
public class OauthTask extends WorkflowInterceptorTask {

    @Override
    public void runTask() throws Exception {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            System.out.println(e.getMessage()); //sleep 메소드가 발생하는 InterruptedException
        }
    }
}
