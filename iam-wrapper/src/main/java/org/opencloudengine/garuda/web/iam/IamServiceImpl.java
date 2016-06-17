package org.opencloudengine.garuda.web.iam;

import org.opencloudengine.garuda.common.exception.ServiceException;
import org.opencloudengine.garuda.model.OauthClient;
import org.opencloudengine.garuda.model.OauthUser;
import org.opencloudengine.garuda.web.configuration.ConfigurationHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

@Service
public class IamServiceImpl implements IamService {
    @Autowired
    @Qualifier("config")
    private Properties config;

    @Autowired
    private IamRepository iamRepository;

    @Autowired
    ConfigurationHelper configurationHelper;

    @Autowired
    private IamServiceFactory serviceFactory;

    @Override
    public Iam insert(Iam iam) {
        return iamRepository.insert(iam);
    }

    @Override
    public Iam select() {
        return iamRepository.select();
    }

    @Override
    public void update(Iam iam) {
        iamRepository.update(iam);
    }

    @Override
    public void update(String host, int port, String managementKey, String managementSecret) {
        Iam iam = new Iam();
        iam.setHost(host);
        iam.setPort(port);
        iam.setManagementKey(managementKey);
        iam.setManagementSecret(managementSecret);
        iamRepository.update(iam);
    }

    @Override
    public Map tokenInfo(String token) {
        IamApi iamApi = serviceFactory.create();
        return iamApi.tokenInfo(token);
    }

    @Override
    public OauthUser getUser(String userId) {
        IamApi iamApi = serviceFactory.create();
        return iamApi.getUser(userId);
    }

    @Override
    public OauthClient getClient(String clientId) {
        IamApi iamApi = serviceFactory.create();
        return iamApi.getClient(clientId);
    }
}
