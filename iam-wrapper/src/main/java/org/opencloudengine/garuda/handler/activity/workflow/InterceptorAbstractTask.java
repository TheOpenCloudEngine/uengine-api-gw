package org.opencloudengine.garuda.handler.activity.workflow;

import org.opencloudengine.garuda.util.ExceptionUtils;
import org.opencloudengine.garuda.util.JsonUtils;
import org.opencloudengine.garuda.web.history.TaskHistory;
import org.opencloudengine.garuda.web.history.WorkflowHistory;

import java.util.Date;

public abstract class InterceptorAbstractTask extends AbstractTask {

    @Override
    public void doExecute() throws Exception {

        preRun();
        try {
            runTask();
            updateTaskHistoryData();
            updateTaskHistoryAsFinished();
            fireComplete(instance);
        } catch (Exception ex) {
            taskHistory.setStderr(ExceptionUtils.getFullStackTrace(ex));
            //taskHistory.setStderr(ex.getCause().toString());
            updateTaskHistoryData();
            updateTaskHistoryAsFailed();
        }
    }

    protected void preRun() throws Exception {

        /**
         * 워크플로우 히스토리를 업데이트한다.
         */
        this.workflowHistory = updateCurrentStep();

        /**
         * 타스크 히스토리를 인서트한다.
         */
        this.insertTaskHistory();

        /**
         * globalAttributes 저장소에 타스크의 시작 시그널을 남긴다.
         */
        globalAttributes.setTaskStatus(instance, taskId, "RUNNING");

    }

    public abstract void runTask() throws Exception;

    private void insertTaskHistory() throws Exception {
        taskHistory = new TaskHistory();
        taskHistory.setTaskId(taskId);
        taskHistory.setTaskName(taskName);
        taskHistory.setStartDate(new Date().getTime());
        taskHistory.setStatus("RUNNING");
        taskHistory.setIdentifier(identifier);
        taskHistory.setWid(wid);

        taskHistory = taskHistoryRepository.insert(taskHistory);
    }

    private WorkflowHistory updateCurrentStep() throws Exception {
        return workflowHistoryRepository.updateCurrentStep(workflowHistory, taskId, taskName);
    }

    private void updateTaskHistoryAsFinished() throws Exception {

        taskHistoryRepository.updateAsFinished(taskHistory);

        //globalAttributes 저장소에 타스크의 성공 시그널을 남긴다.
        globalAttributes.setTaskStatus(instance, taskId, "FINISHED");

    }

    public void updateTaskHistoryAsFailed() throws Exception {
        taskHistoryRepository.updateAsFinished(taskHistory);

        //globalAttributes 저장소에 타스크의 성공 시그널을 남긴다.
        globalAttributes.setTaskStatus(instance, taskId, "FAILED");
    }

    private void updateTaskHistoryData() throws Exception {
        taskHistory.setStdout(stdout);
        taskHistory.setInput(JsonUtils.marshal(inputData));
        taskHistory.setOutput(JsonUtils.marshal(outputData));

        if(inputData != null){
            globalAttributes.setTaskInput(instance,taskId,inputData);
        }
        if(outputData != null){
            globalAttributes.setTaskOutput(instance,taskId,outputData);
        }
    }
}
