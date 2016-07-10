package org.opencloudengine.garuda.web.history;


import java.util.List;

public interface WorkflowHistoryRepository {

    WorkflowHistory updateCurrentStep(WorkflowHistory history, String taskId, String taskName);

    WorkflowHistory updateAsFailed(WorkflowHistory history);

    WorkflowHistory updateAsFinished(WorkflowHistory history);

    WorkflowHistory selectByIdentifier(String identifier);

    WorkflowHistory selectById(String id);

    WorkflowHistory insert(WorkflowHistory history);

    List<WorkflowHistory> select(int limit, Long skip);

    List<WorkflowHistory> selectLikeName(String name, int limit, Long skip);

    Long count();

    Long countLikeName(String name);

    WorkflowHistory updateById(WorkflowHistory history);

    void deleteById(String id);

    void bulk(WorkflowHistory workflowHistory, List<TaskHistory> taskHistories);

}
