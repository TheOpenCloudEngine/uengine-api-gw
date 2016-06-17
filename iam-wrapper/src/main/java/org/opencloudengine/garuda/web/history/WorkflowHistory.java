package org.opencloudengine.garuda.web.history;

import org.opencloudengine.garuda.couchdb.CouchDAO;

import java.util.Date;

/**
 * Created by cloudine on 2015. 6. 3..
 */
public class WorkflowHistory extends CouchDAO {

    private String request;
    private String response;
    private String identifier;
    private String wid;
    private String name;
    private String vars;
    private Long startDate;
    private Long endDate;
    private Long duration;
    private int steps;
    private int current;
    private String task_id;
    private String task_name;
    private String status;
    private String sf_parent_identifier;
    private String sf_root_identifier;
    private int sf_depth;
    private String sf_task_id;

    public WorkflowHistory() {

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

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getWid() {
        return wid;
    }

    public void setWid(String wid) {
        this.wid = wid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Long getEndDate() {
        return endDate;
    }

    public void setEndDate(Long endDate) {
        this.endDate = endDate;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public int getSteps() {
        return steps;
    }

    public void setSteps(int steps) {
        this.steps = steps;
    }

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public String getTask_id() {
        return task_id;
    }

    public void setTask_id(String task_id) {
        this.task_id = task_id;
    }

    public String getTask_name() {
        return task_name;
    }

    public void setTask_name(String task_name) {
        this.task_name = task_name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSf_parent_identifier() {
        return sf_parent_identifier;
    }

    public void setSf_parent_identifier(String sf_parent_identifier) {
        this.sf_parent_identifier = sf_parent_identifier;
    }

    public String getSf_root_identifier() {
        return sf_root_identifier;
    }

    public void setSf_root_identifier(String sf_root_identifier) {
        this.sf_root_identifier = sf_root_identifier;
    }

    public int getSf_depth() {
        return sf_depth;
    }

    public void setSf_depth(int sf_depth) {
        this.sf_depth = sf_depth;
    }

    public String getSf_task_id() {
        return sf_task_id;
    }

    public void setSf_task_id(String sf_task_id) {
        this.sf_task_id = sf_task_id;
    }
}
