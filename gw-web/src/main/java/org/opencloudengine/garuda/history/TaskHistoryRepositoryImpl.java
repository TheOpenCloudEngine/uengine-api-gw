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
public class TaskHistoryRepositoryImpl implements TaskHistoryRepository {

    private String NAMESPACE = "task_history";

    @Autowired
    CouchServiceFactory serviceFactory;

    @Override
    public TaskHistory insert(TaskHistory history) {
        long time = new Date().getTime();
        history.setDocType(NAMESPACE);
        history.setStartDate(time);

        Response response = serviceFactory.getDb().save(history);
        history.set_id(response.getId());
        history.set_rev(response.getRev());

        return history;
    }

    @Override
    public TaskHistory updateAsFailed(TaskHistory history) {
        long time = new Date().getTime();
        history.setEndDate(time);
        history.setDuration(time - history.getStartDate());
        history.setStatus("FAILED");
        return this.updateById(history);
    }

    @Override
    public TaskHistory updateAsFinished(TaskHistory history) {
        long time = new Date().getTime();
        history.setEndDate(time);
        history.setDuration(time - history.getStartDate());
        history.setStatus("FINISHED");
        return this.updateById(history);
    }

    @Override
    public List<TaskHistory> selectByIdentifier(String identifier) {
        List<TaskHistory> list = new ArrayList<>();
        try {
            ViewRequestBuilder builder = serviceFactory.getDb().getViewRequestBuilder(NAMESPACE, "selectByIdentifier");
            Key.ComplexKey complex = new Key().complex(identifier);
            List<ViewResponse.Row<Key.ComplexKey, TaskHistory>> rows = builder.newRequest(Key.Type.COMPLEX, TaskHistory.class).
                    keys(complex).
                    build().getResponse().getRows();

            for (ViewResponse.Row<Key.ComplexKey, TaskHistory> row : rows) {
                list.add(row.getValue());
            }
            list = this.sortStartDate(list);
            return list;
        } catch (Exception ex) {
            return list;
        }
    }

    @Override
    public TaskHistory selectById(String id) {
        try {
            ViewRequestBuilder builder = serviceFactory.getDb().getViewRequestBuilder(NAMESPACE, "selectById");
            Key.ComplexKey complex = new Key().complex(id);
            return builder.newRequest(Key.Type.COMPLEX, TaskHistory.class).
                    keys(complex).
                    build().getResponse().getRows().get(0).getValue();
        } catch (Exception ex) {
            return null;
        }
    }

    @Override
    public TaskHistory updateById(TaskHistory history) {
        TaskHistory existHistory = this.selectById(history.get_id());

        existHistory = (TaskHistory) JsonUtils.merge(existHistory, history);

        Response update = serviceFactory.getDb().update(existHistory);
        existHistory.set_rev(update.getRev());

        return existHistory;
    }

    @Override
    public void deleteById(String id) {
        TaskHistory history = this.selectById(id);
        serviceFactory.getDb().remove(history);
    }

    @Override
    public void deleteByIdentifier(String identifier) {
        List<TaskHistory> taskHistories = this.selectByIdentifier(identifier);
        for (TaskHistory taskHistory : taskHistories) {
            serviceFactory.getDb().remove(taskHistory);
        }
    }

    private List<TaskHistory> sortStartDate(List<TaskHistory> histories) {
        Collections.sort(histories, new Comparator<TaskHistory>() {
            public int compare(TaskHistory o1, TaskHistory o2) {
                if (o1.getStartDate() == o2.getStartDate())
                    return 0;
                return o1.getStartDate() < o2.getStartDate() ? -1 : 1;
            }
        });
        return histories;
    }

}