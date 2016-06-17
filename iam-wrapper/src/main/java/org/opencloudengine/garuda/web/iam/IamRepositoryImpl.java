package org.opencloudengine.garuda.web.iam;

import com.cloudant.client.api.model.Response;
import com.cloudant.client.api.views.Key;
import com.cloudant.client.api.views.ViewRequestBuilder;
import org.opencloudengine.garuda.couchdb.CouchServiceFactory;
import org.opencloudengine.garuda.util.JsonUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public class IamRepositoryImpl implements IamRepository, InitializingBean {

    private String NAMESPACE = "iam";

    @Autowired
    CouchServiceFactory serviceFactory;

    Iam cash;

    @Override
    public Iam getCash() {
        if (cash == null) {
            cash = this.select();
        }
        return cash;
    }

    private void updateCash() {
        cash = this.select();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.updateCash();
    }

    @Override
    public Iam insert(Iam iam) {
        long time = new Date().getTime();
        iam.setDocType(NAMESPACE);
        iam.setRegDate(time);
        iam.setUpdDate(time);

        Response response = serviceFactory.getDb().save(iam);
        iam.set_id(response.getId());
        iam.set_rev(response.getRev());

        this.updateCash();
        return iam;
    }

    @Override
    public Iam select() {
        try {
            ViewRequestBuilder builder = serviceFactory.getDb().getViewRequestBuilder(NAMESPACE, "select");
            //Key.ComplexKey complex = new Key().complex(managementId).add(name);
            return builder.newRequest(Key.Type.COMPLEX, Iam.class).
                    //      keys(complex).
                            build().getResponse().getRows().get(0).getValue();
        } catch (Exception ex) {
            return null;
        }
    }

    @Override
    public Iam update(Iam iam) {
        Iam existIam = this.select();

        existIam = (Iam) JsonUtils.merge(existIam, iam);
        long time = new Date().getTime();
        existIam.setUpdDate(time);

        Response update = serviceFactory.getDb().update(existIam);
        existIam.set_rev(update.getRev());

        this.updateCash();
        return existIam;
    }
}