package org.opencloudengine.garuda.web.workflow;

import org.opencloudengine.garuda.couchdb.CouchDAO;

import java.util.Date;

/**
 * Created by cloudine on 2015. 6. 3..
 */
public class Workflow extends CouchDAO{

    private String name;
    private String designer_xml;
    private String bpmn_xml;
    private String vars;
    private int steps;
    private String status;
    private Long regDate;
    private Long updDate;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesigner_xml() {
        return designer_xml;
    }

    public void setDesigner_xml(String designer_xml) {
        this.designer_xml = designer_xml;
    }

    public String getBpmn_xml() {
        return bpmn_xml;
    }

    public void setBpmn_xml(String bpmn_xml) {
        this.bpmn_xml = bpmn_xml;
    }

    public String getVars() {
        return vars;
    }

    public void setVars(String vars) {
        this.vars = vars;
    }

    public int getSteps() {
        return steps;
    }

    public void setSteps(int steps) {
        this.steps = steps;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
}
