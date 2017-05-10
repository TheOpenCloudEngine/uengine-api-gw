package org.opencloudengine.garuda.web.uris;

import com.cloudant.client.api.model.Response;
import com.cloudant.client.api.views.Key;
import com.cloudant.client.api.views.ViewRequestBuilder;
import com.cloudant.client.api.views.ViewResponse;
import org.mybatis.spring.SqlSessionTemplate;
import org.opencloudengine.garuda.common.repository.PersistentRepositoryImpl;
import org.opencloudengine.garuda.couchdb.CouchServiceFactory;
import org.opencloudengine.garuda.util.JsonUtils;
import org.opencloudengine.garuda.web.iam.Iam;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class ResourceUriRepositoryImpl implements ResourceUriRepository, InitializingBean {

    private String NAMESPACE = "uris";

    @Autowired
    CouchServiceFactory serviceFactory;

    List<ResourceUri> cash;

    @Override
    public List<ResourceUri> getCash() {
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
    public ResourceUri insert(ResourceUri resourceUri) {
        long time = new Date().getTime();
        resourceUri.setDocType(NAMESPACE);
        resourceUri.setRegDate(time);
        resourceUri.setUpdDate(time);

        Response response = serviceFactory.getDb().save(resourceUri);
        resourceUri.set_id(response.getId());
        resourceUri.set_rev(response.getRev());

        this.updateCash();
        return resourceUri;
    }

    @Override
    public List<ResourceUri> selectAll() {
        List<ResourceUri> list = new ArrayList<>();
        try {
            ViewRequestBuilder builder = serviceFactory.getDb().getViewRequestBuilder(NAMESPACE, "select");
            //Key.ComplexKey complex = new Key().complex(managementId);
            List<ViewResponse.Row<Key.ComplexKey, ResourceUri>> rows = builder.newRequest(Key.Type.COMPLEX, ResourceUri.class).
                    //keys(complex).
                            build().getResponse().getRows();

            for (ViewResponse.Row<Key.ComplexKey, ResourceUri> row : rows) {
                list.add(row.getValue());
            }
            list = this.sortUris(list);
            return list;
        } catch (Exception ex) {
            return list;
        }
    }

    @Override
    public List<ResourceUri> select(int limit, Long skip) {
        List<ResourceUri> list = new ArrayList<>();
        try {
            ViewRequestBuilder builder = serviceFactory.getDb().getViewRequestBuilder(NAMESPACE, "select");
            //Key.ComplexKey complex = new Key().complex(managementId);
            List<ViewResponse.Row<Key.ComplexKey, ResourceUri>> rows = builder.newRequest(Key.Type.COMPLEX, ResourceUri.class).
                    //keys(complex).
                            limit(limit).skip(skip).
                    build().getResponse().getRows();

            for (ViewResponse.Row<Key.ComplexKey, ResourceUri> row : rows) {
                list.add(row.getValue());
            }
            list = this.sortUris(list);
            return list;

        } catch (Exception ex) {
            return list;
        }
    }

    @Override
    public ResourceUri selectById(String id) {
        try {
            ViewRequestBuilder builder = serviceFactory.getDb().getViewRequestBuilder(NAMESPACE, "selectById");
            Key.ComplexKey complex = new Key().complex(id);
            return builder.newRequest(Key.Type.COMPLEX, ResourceUri.class).
                    keys(complex).
                    build().getResponse().getRows().get(0).getValue();
        } catch (Exception ex) {
            return null;
        }
    }

    @Override
    public ResourceUri selectByOrder(int order) {
        try {
            ViewRequestBuilder builder = serviceFactory.getDb().getViewRequestBuilder(NAMESPACE, "selectByOrder");
            Key.ComplexKey complex = new Key().complex(order);
            return builder.newRequest(Key.Type.COMPLEX, ResourceUri.class).
                    keys(complex).
                    build().getResponse().getRows().get(0).getValue();
        } catch (Exception ex) {
            return null;
        }
    }

    @Override
    public List<ResourceUri> selectLikeUri(String uri, int limit, Long skip) {
        List<ResourceUri> list = new ArrayList<>();
        Key.ComplexKey startKey;
        Key.ComplexKey endKey;

        try {
            ViewRequestBuilder builder = serviceFactory.getDb().getViewRequestBuilder(NAMESPACE, "selectLikeUri");
            startKey = new Key().complex(uri);
            endKey = new Key().complex(uri + "Z");
            List<ViewResponse.Row<Key.ComplexKey, ResourceUri>> rows = builder.newRequest(Key.Type.COMPLEX, ResourceUri.class).
                    startKey(startKey).endKey(endKey).limit(limit).skip(skip).
                    build().getResponse().getRows();

            for (ViewResponse.Row<Key.ComplexKey, ResourceUri> row : rows) {
                list.add(row.getValue());
            }
            list = this.sortUris(list);
            return list;
        } catch (Exception ex) {
            return list;
        }
    }

    @Override
    public ResourceUri selectByUri(String uri) {
        try {
            ViewRequestBuilder builder = serviceFactory.getDb().getViewRequestBuilder(NAMESPACE, "selectByUri");
            Key.ComplexKey complex = new Key().complex(uri);
            return builder.newRequest(Key.Type.COMPLEX, ResourceUri.class).
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
    public Long countLikeUri(String uri) {
        Long count = null;
        Key.ComplexKey startKey;
        Key.ComplexKey endKey;

        try {
            ViewRequestBuilder builder = serviceFactory.getDb().getViewRequestBuilder(NAMESPACE, "countLikeUri");
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
    public ResourceUri updateById(ResourceUri resourceUri) {
        ResourceUri existUri = this.selectById(resourceUri.get_id());

        existUri = (ResourceUri) JsonUtils.merge(existUri, resourceUri);
        long time = new Date().getTime();
        existUri.setUpdDate(time);

        Response update = serviceFactory.getDb().update(existUri);
        existUri.set_rev(update.getRev());

        this.updateCash();
        return existUri;
    }

    @Override
    public void deleteById(String id) {
        ResourceUri resourceUri = this.selectById(id);
        serviceFactory.getDb().remove(resourceUri);

        this.updateCash();
    }

    private List<ResourceUri> sortUris(List<ResourceUri> resourceUris){
        Collections.sort(resourceUris, new Comparator<ResourceUri>() {
            public int compare(ResourceUri o1, ResourceUri o2) {
                if (o1.getOrder() == o2.getOrder())
                    return 0;
                return o1.getOrder() < o2.getOrder() ? -1 : 1;
            }
        });
        return resourceUris;
    }
}