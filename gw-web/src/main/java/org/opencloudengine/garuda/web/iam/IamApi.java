package org.opencloudengine.garuda.web.iam;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.opencloudengine.garuda.model.OauthClient;
import org.opencloudengine.garuda.model.OauthUser;
import org.opencloudengine.garuda.util.HttpUtils;
import org.opencloudengine.garuda.util.JsonUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by uengine on 2016. 6. 16..
 */
public class IamApi {

    private HttpUtils httpUtils;
    private String host;
    private int port;
    private String managementKey;
    private String managementSecret;

    public HttpUtils getHttpUtils() {
        return httpUtils;
    }

    public void setHttpUtils(HttpUtils httpUtils) {
        this.httpUtils = httpUtils;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getManagementKey() {
        return managementKey;
    }

    public void setManagementKey(String managementKey) {
        this.managementKey = managementKey;
    }

    public String getManagementSecret() {
        return managementSecret;
    }

    public void setManagementSecret(String managementSecret) {
        this.managementSecret = managementSecret;
    }

    public IamApi(String host, int port, String managementKey, String managementSecret) {
        this.host = host;
        this.port = port;
        this.managementKey = managementKey;
        this.managementSecret = managementSecret;

        this.httpUtils = new HttpUtils();
    }

    public Map tokenInfo(String token) {
        String method = "GET";
        String path = "/oauth/token_info";

        Map params = new HashMap();
        params.put("access_token", token);

        String getQueryString = HttpUtils.createGETQueryString(params);

        Map headers = new HashMap();
        try {
            return this.apiRequest(method, path + getQueryString, null, headers);
        } catch (IOException ex) {
            return null;
        }
    }

    public OauthUser getUser(String userId) {
        String method = "GET";
        String path = "/rest/v1/user/" + userId;

        Map headers = new HashMap();
        try {
            Map map = this.apiRequest(method, path, null, headers);
            OauthUser oauthUser = new OauthUser();
            oauthUser.putAll(map);
            return oauthUser;
        } catch (IOException ex) {
            return null;
        }
    }

    public OauthClient getClient(String clientId) {
        String method = "GET";
        String path = "/rest/v1/client/" + clientId;

        Map headers = new HashMap();
        try {
            Map map = this.apiRequest(method, path, null, headers);

            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.convertValue(map, OauthClient.class);
        } catch (IOException ex) {
            return null;
        }
    }

    private Map apiRequest(String method, String path, String data, Map headers) throws IOException {
        headers.put("management-key", this.managementKey);
        headers.put("management-secret", this.managementSecret);

        String url = "http://" + this.host + ":" + this.port + path;

        HttpResponse httpResponse = httpUtils.makeRequest(method, url, data, headers);
        HttpEntity entity = httpResponse.getEntity();
        String json = EntityUtils.toString(entity);
        return JsonUtils.marshal(json);
    }
}
