package org.opencloudengine.garuda.web.iam;

import org.opencloudengine.garuda.couchdb.CouchDAO;

/**
 * Created by uengine on 2016. 6. 1..
 */
public class Iam extends CouchDAO {

    private String host;
    private int port;
    private String managementKey;
    private String managementSecret;
    private Long regDate;
    private Long updDate;

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

    public Long getRegDate() {
        return regDate;
    }

    public void setRegDate(Long regDate) {
        this.regDate = regDate;
    }

    public Long getUpdDate() {
        return updDate;
    }

    public void setUpdDate(Long updDate) {
        this.updDate = updDate;
    }

    @Override
    public String toString() {
        return "Iam{" +
                "host='" + host + '\'' +
                ", port=" + port +
                ", managementKey='" + managementKey + '\'' +
                ", managementSecret='" + managementSecret + '\'' +
                ", regDate=" + regDate +
                ", updDate=" + updDate +
                '}';
    }
}
