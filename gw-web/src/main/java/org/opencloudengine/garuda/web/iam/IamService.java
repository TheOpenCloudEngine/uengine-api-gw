package org.opencloudengine.garuda.web.iam;

import org.opencloudengine.garuda.model.OauthClient;
import org.opencloudengine.garuda.model.OauthUser;

import java.util.List;
import java.util.Map;

public interface IamService {

    Iam insert(Iam iam);

    Iam select();

    void update(Iam iam);

    void update(String host, int port, String managementKey, String managementSecret);

    Map tokenInfo(String token);

    OauthUser getUser(String userId);

    OauthClient getClient(String clientId);
}
