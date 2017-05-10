package org.opencloudengine.garuda.history;

import org.opencloudengine.garuda.couchdb.CouchDAO;

/**
 * Created by cloudine on 2015. 6. 3..
 */
public class TransactionHistory extends CouchDAO {

    public static String WORKFLOW = "workflow";
    public static String CLASS = "class";
    public static String POLICY = "policy";

    private String request;
    private String response;
    private String uri;
    private String method;
    private String runWith;
    private String wid;
    private String identifier;
    private String workflowName;
    private String className;
    private String policyId;
    private String policyName;
    private String vars;
    private Long startDate;
    private String humanStartDate;
    private Long endDate;
    private String humanEndDate;
    private Long duration;
    private String humanDuration;
    private String currentTaskId;
    private String currentTaskName;
    private String status;

    public TransactionHistory() {

    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getRunWith() {
        return runWith;
    }

    public void setRunWith(String runWith) {
        this.runWith = runWith;
    }

    public String getWid() {
        return wid;
    }

    public void setWid(String wid) {
        this.wid = wid;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getWorkflowName() {
        return workflowName;
    }

    public void setWorkflowName(String workflowName) {
        this.workflowName = workflowName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getPolicyId() {
        return policyId;
    }

    public void setPolicyId(String policyId) {
        this.policyId = policyId;
    }

    public String getPolicyName() {
        return policyName;
    }

    public void setPolicyName(String policyName) {
        this.policyName = policyName;
    }

    public String getVars() {
        return vars;
    }

    public void setVars(String vars) {
        this.vars = vars;
    }

    public Long getStartDate() {
        return startDate;
    }

    public void setStartDate(Long startDate) {
        this.startDate = startDate;
    }

    public String getHumanStartDate() {
        return humanStartDate;
    }

    public void setHumanStartDate(String humanStartDate) {
        this.humanStartDate = humanStartDate;
    }

    public Long getEndDate() {
        return endDate;
    }

    public void setEndDate(Long endDate) {
        this.endDate = endDate;
    }

    public String getHumanEndDate() {
        return humanEndDate;
    }

    public void setHumanEndDate(String humanEndDate) {
        this.humanEndDate = humanEndDate;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public String getHumanDuration() {
        return humanDuration;
    }

    public void setHumanDuration(String humanDuration) {
        this.humanDuration = humanDuration;
    }

    public String getCurrentTaskId() {
        return currentTaskId;
    }

    public void setCurrentTaskId(String currentTaskId) {
        this.currentTaskId = currentTaskId;
    }

    public String getCurrentTaskName() {
        return currentTaskName;
    }

    public void setCurrentTaskName(String currentTaskName) {
        this.currentTaskName = currentTaskName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
