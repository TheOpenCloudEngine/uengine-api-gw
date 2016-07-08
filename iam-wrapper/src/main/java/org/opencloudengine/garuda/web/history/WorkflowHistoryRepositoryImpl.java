package org.opencloudengine.garuda.web.history;

import com.cloudant.client.api.model.Response;
import com.cloudant.client.api.views.Key;
import com.cloudant.client.api.views.ViewRequestBuilder;
import com.cloudant.client.api.views.ViewResponse;
import org.opencloudengine.garuda.couchdb.CouchServiceFactory;
import org.opencloudengine.garuda.util.JsonUtils;
import org.opencloudengine.garuda.web.policy.Policy;
import org.opencloudengine.garuda.web.policy.PolicyRepository;
import org.opencloudengine.garuda.web.uris.ResourceUri;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class WorkflowHistoryRepositoryImpl implements WorkflowHistoryRepository {

    private String NAMESPACE = "workflow_history";

    @Autowired
    CouchServiceFactory serviceFactory;

    @Override
    public WorkflowHistory insert(WorkflowHistory history) {
        long time = new Date().getTime();
        history.setDocType(NAMESPACE);
        history.setStartDate(time);

        Response response = serviceFactory.getDb().save(history);
        history.set_id(response.getId());
        history.set_rev(response.getRev());

        return history;
    }

    @Override
    public List<WorkflowHistory> select(int limit, Long skip) {
        List<WorkflowHistory> list = new ArrayList<>();
        try {
            ViewRequestBuilder builder = serviceFactory.getDb().getViewRequestBuilder(NAMESPACE, "select");
            List<ViewResponse.Row<Key.ComplexKey, WorkflowHistory>> rows = builder.newRequest(Key.Type.COMPLEX, WorkflowHistory.class).
                    //keys(complex).
                            limit(limit).skip(skip).
                    build().getResponse().getRows();

            for (ViewResponse.Row<Key.ComplexKey, WorkflowHistory> row : rows) {
                list.add(row.getValue());
            }
            list = this.sortStartDate(list);
            return list;

        } catch (Exception ex) {
            return list;
        }
    }

    @Override
    public WorkflowHistory updateCurrentStep(WorkflowHistory history, String taskId, String taskName) {
        history.setCurrentTaskId(taskId);
        history.setCurrentTaskName(taskName);
        return this.updateById(history);
    }

    @Override
    public WorkflowHistory updateAsFailed(WorkflowHistory history) {
        long time = new Date().getTime();
        history.setEndDate(time);
        history.setDuration(time - history.getStartDate());
        history.setStatus("FAILED");
        return this.updateById(history);
    }

    @Override
    public WorkflowHistory updateAsFinished(WorkflowHistory history) {
        long time = new Date().getTime();
        history.setEndDate(time);
        history.setDuration(time - history.getStartDate());
        history.setStatus("FINISHED");
        return this.updateById(history);
    }

    @Override
    public WorkflowHistory selectByIdentifier(String identifier) {
        try {
            ViewRequestBuilder builder = serviceFactory.getDb().getViewRequestBuilder(NAMESPACE, "selectByIdentifier");
            Key.ComplexKey complex = new Key().complex(identifier);
            return builder.newRequest(Key.Type.COMPLEX, WorkflowHistory.class).
                    keys(complex).
                    build().getResponse().getRows().get(0).getValue();
        } catch (Exception ex) {
            return null;
        }
    }

    @Override
    public WorkflowHistory selectById(String id) {
        try {
            ViewRequestBuilder builder = serviceFactory.getDb().getViewRequestBuilder(NAMESPACE, "selectById");
            Key.ComplexKey complex = new Key().complex(id);
            return builder.newRequest(Key.Type.COMPLEX, WorkflowHistory.class).
                    keys(complex).
                    build().getResponse().getRows().get(0).getValue();
        } catch (Exception ex) {
            return null;
        }
    }

    @Override
    public List<WorkflowHistory> selectLikeName(String name, int limit, Long skip) {
        List<WorkflowHistory> list = new ArrayList<>();
        Key.ComplexKey startKey;
        Key.ComplexKey endKey;

        try {
            ViewRequestBuilder builder = serviceFactory.getDb().getViewRequestBuilder(NAMESPACE, "selectLikeName");
            startKey = new Key().complex(name);
            endKey = new Key().complex(name + "Z");
            List<ViewResponse.Row<Key.ComplexKey, WorkflowHistory>> rows = builder.newRequest(Key.Type.COMPLEX, WorkflowHistory.class).
                    startKey(startKey).endKey(endKey).limit(limit).skip(skip).
                    build().getResponse().getRows();

            for (ViewResponse.Row<Key.ComplexKey, WorkflowHistory> row : rows) {
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
    public Long countLikeName(String name) {
        Long count = null;
        Key.ComplexKey startKey;
        Key.ComplexKey endKey;

        try {
            ViewRequestBuilder builder = serviceFactory.getDb().getViewRequestBuilder(NAMESPACE, "countLikeName");
            startKey = new Key().complex(name);
            endKey = new Key().complex(name + "Z");
            count = builder.newRequest(Key.Type.COMPLEX, Long.class).
                    startKey(startKey).endKey(endKey).reduce(true).
                    build().getResponse().getRows().get(0).getValue();

        } catch (Exception ex) {
            return new Long(0);
        }
        return count == null ? new Long(0) : count;
    }

    @Override
    public WorkflowHistory updateById(WorkflowHistory history) {
        WorkflowHistory existHistory = this.selectById(history.get_id());

        existHistory = (WorkflowHistory) JsonUtils.merge(existHistory, history);

        Response update = serviceFactory.getDb().update(existHistory);
        existHistory.set_rev(update.getRev());

        return existHistory;
    }

    @Override
    public void deleteById(String id) {
        WorkflowHistory history = this.selectById(id);
        serviceFactory.getDb().remove(history);
    }

    private List<WorkflowHistory> sortStartDate(List<WorkflowHistory> histories) {
        Collections.sort(histories, new Comparator<WorkflowHistory>() {
            public int compare(WorkflowHistory o1, WorkflowHistory o2) {
                if (o1.getStartDate() == o2.getStartDate())
                    return 0;
                return o1.getStartDate() > o2.getStartDate() ? -1 : 1;
            }
        });
        return histories;
    }

}