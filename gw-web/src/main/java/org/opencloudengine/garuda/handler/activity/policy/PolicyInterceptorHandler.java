package org.opencloudengine.garuda.handler.activity.policy;

import org.opencloudengine.garuda.handler.AbstractHandler;
import org.opencloudengine.garuda.history.TaskHistory;
import org.opencloudengine.garuda.util.ExceptionUtils;
import org.opencloudengine.garuda.util.JsonUtils;

import java.util.Date;

public class PolicyInterceptorHandler extends AbstractHandler {

    public void initTaskHistory(String taskName) {
        taskHistory = new TaskHistory();
        taskHistory.setTaskId(taskName);
        taskHistory.setTaskName(taskName);
        taskHistory.setStartDate(new Date().getTime());
        taskHistory.setStatus("RUNNING");
        taskHistory.setIdentifier(identifier);

        //taskMap 에 타스크 히스토리를 등록한다.
        taskMap.put(taskName, taskHistory);

        this.updateCurrentStep(taskName);
    }

    private void updateCurrentStep(String taskName) {
        transactionHistory.setCurrentTaskId(taskName);
        transactionHistory.setCurrentTaskName(taskName);
    }

    public void updateTaskHistoryAsFinished(String taskName) {
        taskHistory = taskMap.get(taskName);
        if (taskHistory == null) {
            return;
        }
        long time = new Date().getTime();
        this.taskHistory.setEndDate(time);
        this.taskHistory.setDuration(time - this.taskHistory.getStartDate());
        this.taskHistory.setStatus("FINISHED");

        //taskMap 에 타스크 히스토리를 등록한다.
        taskMap.put(taskName, this.taskHistory);
    }

    public void updateTaskHistoryAsFailed(String taskName) {
        taskHistory = taskMap.get(taskName);
        if (taskHistory == null) {
            return;
        }
        long time = new Date().getTime();
        taskHistory.setEndDate(time);
        taskHistory.setDuration(time - taskHistory.getStartDate());
        taskHistory.setStatus("FAILED");

        //taskMap 에 타스크 히스토리를 등록한다.
        taskMap.put(taskName, this.taskHistory);

        //GateHandler 에서 실패처리를 할 수 있도록 한다.
        transactionSucceeded = false;
    }

    public void updateTaskStdout(String taskName, String stdout) {
        taskHistory = taskMap.get(taskName);
        if (taskHistory == null) {
            return;
        }
        taskHistory.setStdout(stdout);

        //taskMap 에 타스크 히스토리를 등록한다.
        taskMap.put(taskName, this.taskHistory);
    }

    public void updateTaskStderr(String taskName, String stderr) {
        taskHistory = taskMap.get(taskName);
        if (taskHistory == null) {
            return;
        }
        taskHistory.setStderr(stderr);

        //taskMap 에 타스크 히스토리를 등록한다.
        taskMap.put(taskName, this.taskHistory);
    }

    public void updateTaskInputData(String taskName, Object inputData) {
        taskHistory = taskMap.get(taskName);
        if (taskHistory == null) {
            return;
        }
        taskHistory.setInput(this.convertData(inputData));

        //taskMap 에 타스크 히스토리를 등록한다.
        taskMap.put(taskName, this.taskHistory);
    }

    public void updateTaskOutputData(String taskName, Object outputData) {
        taskHistory = taskMap.get(taskName);
        if (taskHistory == null) {
            return;
        }
        taskHistory.setOutput(this.convertData(outputData));

        //taskMap 에 타스크 히스토리를 등록한다.
        taskMap.put(taskName, this.taskHistory);
    }

    private String convertData(Object data) {
        String result = null;
        try {
            result = JsonUtils.marshal(data);
        } catch (Exception ex) {
            result = ex.getCause().toString();
        }
        return result;
    }
}
