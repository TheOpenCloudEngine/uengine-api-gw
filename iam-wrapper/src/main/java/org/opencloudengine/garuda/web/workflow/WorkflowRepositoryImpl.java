package org.opencloudengine.garuda.web.workflow;

import com.cloudant.client.api.model.Response;
import com.cloudant.client.api.views.Key;
import com.cloudant.client.api.views.ViewRequestBuilder;
import com.cloudant.client.api.views.ViewResponse;
import org.mybatis.spring.SqlSessionTemplate;
import org.opencloudengine.garuda.common.repository.PersistentRepositoryImpl;
import org.opencloudengine.garuda.couchdb.CouchServiceFactory;
import org.opencloudengine.garuda.util.JsonUtils;
import org.opencloudengine.garuda.web.policy.Policy;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
public class WorkflowRepositoryImpl implements WorkflowRepository, InitializingBean {

    private String NAMESPACE = "workflow";

    @Autowired
    CouchServiceFactory serviceFactory;

    List<Workflow> cash;

    @Override
    public List<Workflow> getCash() {
        if (cash == null) {
            cash = this.selectAll();
        }
        return cash;
    }

    private void updateCash() {
        cash = this.selectAll();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.updateCash();
    }

    @Override
    public Workflow insert(Workflow workflow) {
        long time = new Date().getTime();
        workflow.setDocType(NAMESPACE);
        workflow.setRegDate(time);
        workflow.setUpdDate(time);

        Response response = serviceFactory.getDb().save(workflow);
        workflow.set_id(response.getId());
        workflow.set_rev(response.getRev());

        this.updateCash();
        return workflow;
    }

    @Override
    public List<Workflow> selectAll() {
        List<Workflow> list = new ArrayList<>();
        try {
            ViewRequestBuilder builder = serviceFactory.getDb().getViewRequestBuilder(NAMESPACE, "select");
            //Key.ComplexKey complex = new Key().complex(managementId);
            List<ViewResponse.Row<Key.ComplexKey, Workflow>> rows = builder.newRequest(Key.Type.COMPLEX, Workflow.class).
                    //keys(complex).
                            build().getResponse().getRows();

            for (ViewResponse.Row<Key.ComplexKey, Workflow> row : rows) {
                list.add(row.getValue());
            }
            return list;
        } catch (Exception ex) {
            return list;
        }
    }

    @Override
    public List<Workflow> select(int limit, Long skip) {
        List<Workflow> list = new ArrayList<>();
        try {
            ViewRequestBuilder builder = serviceFactory.getDb().getViewRequestBuilder(NAMESPACE, "select");
            //Key.ComplexKey complex = new Key().complex(managementId);
            List<ViewResponse.Row<Key.ComplexKey, Workflow>> rows = builder.newRequest(Key.Type.COMPLEX, Workflow.class).
                    //keys(complex).
                            limit(limit).skip(skip).
                    build().getResponse().getRows();

            for (ViewResponse.Row<Key.ComplexKey, Workflow> row : rows) {
                list.add(row.getValue());
            }
            return list;

        } catch (Exception ex) {
            return list;
        }
    }

    @Override
    public Workflow selectById(String id) {
        try {
            ViewRequestBuilder builder = serviceFactory.getDb().getViewRequestBuilder(NAMESPACE, "selectById");
            Key.ComplexKey complex = new Key().complex(id);
            return builder.newRequest(Key.Type.COMPLEX, Workflow.class).
                    keys(complex).
                    build().getResponse().getRows().get(0).getValue();
        } catch (Exception ex) {
            return null;
        }
    }

    @Override
    public List<Workflow> selectByIds(List<String> ids) {
        List<Workflow> list = new ArrayList<>();
        try {
            ArrayList<Key.ComplexKey> complexKeyArrayList = new ArrayList<>();
            for (String id : ids) {
                complexKeyArrayList.add(new Key().complex(id));
            }
            Key.ComplexKey[] complexKeys = complexKeyArrayList.toArray(new Key.ComplexKey[complexKeyArrayList.size()]);

            ViewRequestBuilder builder = serviceFactory.getDb().getViewRequestBuilder(NAMESPACE, "selectById");
            List<ViewResponse.Row<Key.ComplexKey, Workflow>> rows = builder.newRequest(Key.Type.COMPLEX, Workflow.class).
                    keys(complexKeys).build().getResponse().getRows();

            for (ViewResponse.Row<Key.ComplexKey, Workflow> row : rows) {
                list.add(row.getValue());
            }
            return list;
        } catch (Exception ex) {
            return list;
        }
    }

    @Override
    public List<Workflow> selectLikeName(String name, int limit, Long skip) {
        List<Workflow> list = new ArrayList<>();
        Key.ComplexKey startKey;
        Key.ComplexKey endKey;

        try {
            ViewRequestBuilder builder = serviceFactory.getDb().getViewRequestBuilder(NAMESPACE, "selectLikeName");
            startKey = new Key().complex(name);
            endKey = new Key().complex(name + "Z");
            List<ViewResponse.Row<Key.ComplexKey, Workflow>> rows = builder.newRequest(Key.Type.COMPLEX, Workflow.class).
                    startKey(startKey).endKey(endKey).limit(limit).skip(skip).
                    build().getResponse().getRows();

            for (ViewResponse.Row<Key.ComplexKey, Workflow> row : rows) {
                list.add(row.getValue());
            }
            return list;
        } catch (Exception ex) {
            return list;
        }
    }

    @Override
    public Workflow selectByName(String name) {
        try {
            ViewRequestBuilder builder = serviceFactory.getDb().getViewRequestBuilder(NAMESPACE, "selectByName");
            Key.ComplexKey complex = new Key().complex(name);
            return builder.newRequest(Key.Type.COMPLEX, Workflow.class).
                    keys(complex).
                    build().getResponse().getRows().get(0).getValue();

        } catch (Exception ex) {
            return null;
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
    public Workflow updateById(Workflow workflow) {
        Workflow existWorkflow = this.selectById(workflow.get_id());

        existWorkflow = (Workflow) JsonUtils.merge(existWorkflow, workflow);
        long time = new Date().getTime();
        existWorkflow.setUpdDate(time);

        Response update = serviceFactory.getDb().update(existWorkflow);
        existWorkflow.set_rev(update.getRev());

        this.updateCash();
        return existWorkflow;
    }

    @Override
    public void deleteById(String id) {
        Workflow workflow = this.selectById(id);
        serviceFactory.getDb().remove(workflow);

        this.updateCash();
    }
}