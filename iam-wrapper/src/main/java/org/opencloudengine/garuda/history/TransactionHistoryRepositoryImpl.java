package org.opencloudengine.garuda.history;

import com.cloudant.client.api.model.Response;
import com.cloudant.client.api.views.Key;
import com.cloudant.client.api.views.ViewRequestBuilder;
import com.cloudant.client.api.views.ViewResponse;
import org.opencloudengine.garuda.couchdb.CouchServiceFactory;
import org.opencloudengine.garuda.util.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class TransactionHistoryRepositoryImpl implements TransactionHistoryRepository {

    private String NAMESPACE = "transaction_history";

    @Autowired
    CouchServiceFactory serviceFactory;

    @Override
    public void bulk(TransactionHistory workflowHistory, List<TaskHistory> taskHistories) {
        workflowHistory.setDocType(NAMESPACE);
        for (TaskHistory taskHistory : taskHistories) {
            taskHistory.setDocType("task_history");
        }

        List<Object> list = new ArrayList<>();
        list.add(workflowHistory);
        list.addAll(taskHistories);
        serviceFactory.getDb().bulk(list);
    }

    @Override
    public TransactionHistory insert(TransactionHistory history) {
        long time = new Date().getTime();
        history.setDocType(NAMESPACE);
        history.setStartDate(time);

        Response response = serviceFactory.getDb().save(history);
        history.set_id(response.getId());
        history.set_rev(response.getRev());

        return history;
    }

    @Override
    public List<TransactionHistory> select(int limit, Long skip) {
        List<TransactionHistory> list = new ArrayList<>();
        try {
            ViewRequestBuilder builder = serviceFactory.getDb().getViewRequestBuilder(NAMESPACE, "select");
            List<ViewResponse.Row<Key.ComplexKey, TransactionHistory>> rows = builder.newRequest(Key.Type.COMPLEX, TransactionHistory.class).
                    //keys(complex).
                            limit(limit).skip(skip).descending(true).
                    build().getResponse().getRows();

            for (ViewResponse.Row<Key.ComplexKey, TransactionHistory> row : rows) {
                list.add(row.getValue());
            }
            list = this.sortStartDate(list);
            return list;

        } catch (Exception ex) {
            return list;
        }
    }

    @Override
    public TransactionHistory updateCurrentStep(TransactionHistory history, String taskId, String taskName) {
        history.setCurrentTaskId(taskId);
        history.setCurrentTaskName(taskName);
        return this.updateById(history);
    }

    @Override
    public TransactionHistory updateAsFailed(TransactionHistory history) {
        long time = new Date().getTime();
        history.setEndDate(time);
        history.setDuration(time - history.getStartDate());
        history.setStatus("FAILED");
        return this.updateById(history);
    }

    @Override
    public TransactionHistory updateAsFinished(TransactionHistory history) {
        long time = new Date().getTime();
        history.setEndDate(time);
        history.setDuration(time - history.getStartDate());
        history.setStatus("FINISHED");
        return this.updateById(history);
    }

    @Override
    public TransactionHistory selectByIdentifier(String identifier) {
        try {
            ViewRequestBuilder builder = serviceFactory.getDb().getViewRequestBuilder(NAMESPACE, "selectByIdentifier");
            Key.ComplexKey complex = new Key().complex(identifier);
            return builder.newRequest(Key.Type.COMPLEX, TransactionHistory.class).
                    keys(complex).
                    build().getResponse().getRows().get(0).getValue();
        } catch (Exception ex) {
            return null;
        }
    }

    @Override
    public TransactionHistory selectById(String id) {
        try {
            ViewRequestBuilder builder = serviceFactory.getDb().getViewRequestBuilder(NAMESPACE, "selectById");
            Key.ComplexKey complex = new Key().complex(id);
            return builder.newRequest(Key.Type.COMPLEX, TransactionHistory.class).
                    keys(complex).
                    build().getResponse().getRows().get(0).getValue();
        } catch (Exception ex) {
            return null;
        }
    }

    @Override
    public List<TransactionHistory> selectLikeUri(String uri, int limit, Long skip) {
        List<TransactionHistory> list = new ArrayList<>();
        Key.ComplexKey startKey;
        Key.ComplexKey endKey;

        try {
            ViewRequestBuilder builder = serviceFactory.getDb().getViewRequestBuilder(NAMESPACE, "selectLikeUri");
            startKey = new Key().complex(uri);
            endKey = new Key().complex(uri + "Z");
            List<ViewResponse.Row<Key.ComplexKey, TransactionHistory>> rows = builder.newRequest(Key.Type.COMPLEX, TransactionHistory.class).
                    startKey(startKey).endKey(endKey).limit(limit).skip(skip).
                    build().getResponse().getRows();

            for (ViewResponse.Row<Key.ComplexKey, TransactionHistory> row : rows) {
                list.add(row.getValue());
            }
            list = this.sortStartDate(list);
            return list;
        } catch (Exception ex) {
            return list;
        }
    }

    @Override
    public Long count() {
        Long count = null;
        Key.ComplexKey complex;

        try {
            ViewRequestBuilder builder = serviceFactory.getDb().getViewRequestBuilder(NAMESPACE, "count");

            count = builder.newRequest(Key.Type.COMPLEX, Long.class).
                    reduce(true).
                    build().getResponse().getRows().get(0).getValue();

        } catch (Exception ex) {
            return new Long(0);
        }
        return count == null ? new Long(0) : count;
    }

    @Override
    public Long countLikeUri(String uri) {
        Long count = null;
        Key.ComplexKey startKey;
        Key.ComplexKey endKey;

        try {
            ViewRequestBuilder builder = serviceFactory.getDb().getViewRequestBuilder(NAMESPACE, "countLikeName");
            startKey = new Key().complex(uri);
            endKey = new Key().complex(uri + "Z");
            count = builder.newRequest(Key.Type.COMPLEX, Long.class).
                    startKey(startKey).endKey(endKey).reduce(true).
                    build().getResponse().getRows().get(0).getValue();

        } catch (Exception ex) {
            return new Long(0);
        }
        return count == null ? new Long(0) : count;
    }

    @Override
    public TransactionHistory updateById(TransactionHistory history) {
        TransactionHistory existHistory = this.selectById(history.get_id());

        existHistory = (TransactionHistory) JsonUtils.merge(existHistory, history);

        Response update = serviceFactory.getDb().update(existHistory);
        existHistory.set_rev(update.getRev());

        return existHistory;
    }

    @Override
    public void deleteById(String id) {
        TransactionHistory history = this.selectById(id);
        serviceFactory.getDb().remove(history);
    }

    private List<TransactionHistory> sortStartDate(List<TransactionHistory> histories) {
        Collections.sort(histories, new Comparator<TransactionHistory>() {
            public int compare(TransactionHistory o1, TransactionHistory o2) {
                if (o1.getStartDate() == o2.getStartDate())
                    return 0;
                return o1.getStartDate() > o2.getStartDate() ? -1 : 1;
            }
        });
        return histories;
    }

}