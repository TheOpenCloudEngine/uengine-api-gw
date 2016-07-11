package org.opencloudengine.garuda.history;

import org.opencloudengine.garuda.couchdb.CouchDAO;

public class TaskHistory extends CouchDAO {

    private String identifier;
    private String wid;
    private String taskId;
    private String taskName;
    private Long startDate;
    private String humanStartDate;
    private Long endDate;
    private String humanEndDate;
    private Long duration;
    private String humanDuration;
    private String status;
    private String input;
    private String output;
    private String stdout;
    private String stderr;

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

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public String getStdout() {
        return stdout;
    }

    public void setStdout(String stdout) {
        this.stdout = stdout;
    }

    public String getStderr() {
        return stderr;
    }

    public void setStderr(String stderr) {
        this.stderr = stderr;
    }
}
