package org.opencloudengine.garuda.web.policy;

import com.cloudant.client.api.model.Response;
import com.cloudant.client.api.views.Key;
import com.cloudant.client.api.views.UnpaginatedRequestBuilder;
import com.cloudant.client.api.views.ViewRequestBuilder;
import com.cloudant.client.api.views.ViewResponse;
import org.opencloudengine.garuda.couchdb.CouchServiceFactory;
import org.opencloudengine.garuda.util.JsonUtils;
import org.opencloudengine.garuda.web.uris.ResourceUri;
import org.opencloudengine.garuda.web.uris.ResourceUriRepository;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class PolicyRepositoryImpl implements PolicyRepository, InitializingBean {

    private String NAMESPACE = "policy";

    @Autowired
    CouchServiceFactory serviceFactory;

    List<Policy> cash;

    @Override
    public List<Policy> getCash() {
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
    public Policy insert(Policy policy) {
        long time = new Date().getTime();
        policy.setDocType(NAMESPACE);
        policy.setRegDate(time);
        policy.setUpdDate(time);

        Response response = serviceFactory.getDb().save(policy);
        policy.set_id(response.getId());
        policy.set_rev(response.getRev());

        this.updateCash();
        return policy;
    }

    @Override
    public List<Policy> selectAll() {
        List<Policy> list = new ArrayList<>();
        try {
            ViewRequestBuilder builder = serviceFactory.getDb().getViewRequestBuilder(NAMESPACE, "select");
            //Key.ComplexKey complex = new Key().complex(managementId);
            List<ViewResponse.Row<Key.ComplexKey, Policy>> rows = builder.newRequest(Key.Type.COMPLEX, Policy.class).
                    //keys(complex).
                            build().getResponse().getRows();

            for (ViewResponse.Row<Key.ComplexKey, Policy> row : rows) {
                list.add(row.getValue());
            }
            return list;
        } catch (Exception ex) {
            return list;
        }
    }

    @Override
    public List<Policy> select(int limit, Long skip) {
        List<Policy> list = new ArrayList<>();
        try {
            ViewRequestBuilder builder = serviceFactory.getDb().getViewRequestBuilder(NAMESPACE, "select");
            //Key.ComplexKey complex = new Key().complex(managementId);
            List<ViewResponse.Row<Key.ComplexKey, Policy>> rows = builder.newRequest(Key.Type.COMPLEX, Policy.class).
                    //keys(complex).
                            limit(limit).skip(skip).
                    build().getResponse().getRows();

            for (ViewResponse.Row<Key.ComplexKey, Policy> row : rows) {
                list.add(row.getValue());
            }
            return list;

        } catch (Exception ex) {
            return list;
        }
    }

    @Override
    public Policy selectById(String id) {
        try {
            ViewRequestBuilder builder = serviceFactory.getDb().getViewRequestBuilder(NAMESPACE, "selectById");
            Key.ComplexKey complex = new Key().complex(id);
            return builder.newRequest(Key.Type.COMPLEX, Policy.class).
                    keys(complex).
                    build().getResponse().getRows().get(0).getValue();
        } catch (Exception ex) {
            return null;
        }
    }

    @Override
    public List<Policy> selectByIds(List<String> ids) {
        List<Policy> list = new ArrayList<>();
        try {
            ArrayList<Key.ComplexKey> complexKeyArrayList = new ArrayList<>();
            for (String id : ids) {
                complexKeyArrayList.add(new Key().complex(id));
            }
            Key.ComplexKey[] complexKeys = complexKeyArrayList.toArray(new Key.ComplexKey[complexKeyArrayList.size()]);

            ViewRequestBuilder builder = serviceFactory.getDb().getViewRequestBuilder(NAMESPACE, "selectById");
            List<ViewResponse.Row<Key.ComplexKey, Policy>> rows = builder.newRequest(Key.Type.COMPLEX, Policy.class).
                    keys(complexKeys).build().getResponse().getRows();

            for (ViewResponse.Row<Key.ComplexKey, Policy> row : rows) {
                list.add(row.getValue());
            }
            return list;
        } catch (Exception ex) {
            return list;
        }
    }

    @Override
    public List<Policy> selectLikeName(String name, int limit, Long skip) {
        List<Policy> list = new ArrayList<>();
        Key.ComplexKey startKey;
        Key.ComplexKey endKey;

        try {
            ViewRequestBuilder builder = serviceFactory.getDb().getViewRequestBuilder(NAMESPACE, "selectLikeName");
            startKey = new Key().complex(name);
            endKey = new Key().complex(name + "Z");
            List<ViewResponse.Row<Key.ComplexKey, Policy>> rows = builder.newRequest(Key.Type.COMPLEX, Policy.class).
                    startKey(startKey).endKey(endKey).limit(limit).skip(skip).
                    build().getResponse().getRows();

            for (ViewResponse.Row<Key.ComplexKey, Policy> row : rows) {
                list.add(row.getValue());
            }
            return list;
        } catch (Exception ex) {
            return list;
        }
    }

    @Override
    public Policy selectByName(String name) {
        try {
            ViewRequestBuilder builder = serviceFactory.getDb().getViewRequestBuilder(NAMESPACE, "selectByName");
            Key.ComplexKey complex = new Key().complex(name);
            return builder.newRequest(Key.Type.COMPLEX, Policy.class).
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
    public Policy updateById(Policy policy) {
        Policy existPolicy = this.selectById(policy.get_id());

        existPolicy = (Policy) JsonUtils.merge(existPolicy, policy);
        long time = new Date().getTime();
        existPolicy.setUpdDate(time);

        Response update = serviceFactory.getDb().update(existPolicy);
        existPolicy.set_rev(update.getRev());

        this.updateCash();
        return existPolicy;
    }

    @Override
    public void deleteById(String id) {
        Policy policy = this.selectById(id);
        serviceFactory.getDb().remove(policy);

        this.updateCash();
    }

}