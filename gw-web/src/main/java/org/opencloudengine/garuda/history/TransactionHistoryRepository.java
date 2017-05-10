package org.opencloudengine.garuda.history;


import java.util.List;

public interface TransactionHistoryRepository {

    TransactionHistory updateCurrentStep(TransactionHistory history, String taskId, String taskName);

    TransactionHistory updateAsFailed(TransactionHistory history);

    TransactionHistory updateAsFinished(TransactionHistory history);

    TransactionHistory selectByIdentifier(String identifier);

    TransactionHistory selectById(String id);

    TransactionHistory insert(TransactionHistory history);

    List<TransactionHistory> select(int limit, Long skip);

    List<TransactionHistory> selectLikeUri(String uri, int limit, Long skip);

    Long count();

    Long countLikeUri(String uri);

    TransactionHistory updateById(TransactionHistory history);

    void deleteById(String id);

    void bulk(TransactionHistory workflowHistory, List<TaskHistory> taskHistories);

}
