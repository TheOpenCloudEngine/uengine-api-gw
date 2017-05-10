package org.opencloudengine.garuda.handler;

import org.opencloudengine.garuda.gateway.GatewayServlet;
import org.opencloudengine.garuda.model.HttpObjectSet;
import org.opencloudengine.garuda.history.TaskHistory;
import org.opencloudengine.garuda.web.uris.ResourceUri;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.uengine.kernel.ProcessDefinition;
import org.uengine.kernel.ProcessInstance;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.util.*;

/**
 * Created by cloudine on 2015. 2. 26..
 */
@Service
public class GlobalAttributesImpl implements GlobalAttributes {
    /**
     * SLF4J Logging
     */
    private Logger logger = LoggerFactory.getLogger(GlobalAttributes.class);

    @Autowired
    @Qualifier("config")
    private Properties config;

    private Map httpRepo = new HashMap();

    @Override
    public void registJobResultMap(ProcessInstance instance) throws Exception {
        instance.set("jobMap", new HashMap());
    }

    @Override
    public Map getJobMap(ProcessInstance instance) throws Exception {
        if (instance.get("jobMap") != null) {
            return (Map) instance.get("jobMap");
        } else {
            this.registJobResultMap(instance);
            return new HashMap();
        }
    }

    @Override
    public void setJobMap(ProcessInstance instance, Map jobMap) throws Exception {
        instance.set("jobMap", jobMap);
    }

    private void setTaskParam(ProcessInstance instance, String taskId, String key, Object param) throws Exception {
        Map jobMap = this.getJobMap(instance);

        if (!jobMap.containsKey(taskId)) {
            jobMap.put(taskId, new HashMap<>());
        }

        Map taskMap = (Map) jobMap.get(taskId);
        taskMap.put(key, param);
        jobMap.put(taskId, taskMap);
        this.setJobMap(instance, jobMap);
    }

    private Object getTaskParam(ProcessInstance instance, String taskId, String key, Object defaultValue) throws Exception {
        Map jobMap = this.getJobMap(instance);

        if (!jobMap.containsKey(taskId)) {
            jobMap.put(taskId, new HashMap<>());
        }

        Map taskMap = (Map) jobMap.get(taskId);
        if (!taskMap.containsKey(key)) {
            taskMap.put(key, defaultValue);
            return defaultValue;
        } else {
            return taskMap.get(key);
        }
    }

    @Override
    public String getTaskStatus(ProcessInstance instance, String taskId) throws Exception {
        return (String) this.getTaskParam(instance, taskId, "status", "STANDBY");
    }

    @Override
    public void setTaskStatus(ProcessInstance instance, String taskId, String status) throws Exception {
        this.setTaskParam(instance, taskId, "status", status);
    }

    @Override
    public Object getTaskInput(ProcessInstance instance, String taskId) throws Exception {
        return this.getTaskParam(instance, taskId, "input", null);
    }

    @Override
    public void setTaskInput(ProcessInstance instance, String taskId, Object data) throws Exception {
        this.setTaskParam(instance, taskId, "input", data);
    }

    @Override
    public Object getTaskOutput(ProcessInstance instance, String taskId) throws Exception {
        return this.getTaskParam(instance, taskId, "output", null);
    }

    @Override
    public void setTaskOutput(ProcessInstance instance, String taskId, Object data) throws Exception {
        this.setTaskParam(instance, taskId, "output", data);
    }

    @Override
    public String getTaskNext(ProcessInstance instance, String taskId) throws Exception {
        return (String) this.getTaskParam(instance, taskId, "next", null);
    }

    @Override
    public void setTaskNext(ProcessInstance instance, String taskId, String next) throws Exception {
        this.setTaskParam(instance, taskId, "next", next);
    }

    @Override
    public Map<String, String> getTaskProperties(ProcessInstance instance, String taskId) throws Exception {
        Map vars = (Map) instance.get("vars");
        if (vars.containsKey(taskId)) {
            return (Map<String, String>) vars.get(taskId);
        } else {
            return null;
        }
    }

    @Override
    public String getTaskName(ProcessInstance instance, String taskId) throws Exception {
        Map<String, String> properties = this.getTaskProperties(instance, taskId);
        return properties.get("label").toString();
    }

    @Override
    public String getTaskIdByName(ProcessInstance instance, String label) throws Exception {
        String selectedTaskId = null;
        Map vars = (Map) instance.get("vars");
        Set set = vars.keySet();
        Iterator iterator = set.iterator();
        while (iterator.hasNext()) {
            String taskId = iterator.next().toString();
            Map<String, String> properties = this.getTaskProperties(instance, taskId);
            if (properties.containsKey("label")) {
                selectedTaskId = taskId;
            }
        }
        return selectedTaskId;
    }

    @Override
    public void setHttpObjects(String identifier, GatewayServlet servlet, HttpServletRequest servletRequest, HttpServletResponse servletResponse, ResourceUri resourceUri) {
        HttpObjectSet httpObjectSet = new HttpObjectSet(servlet, servletRequest, servletResponse, resourceUri);
        this.httpRepo.put(identifier, httpObjectSet);
    }

    @Override
    public HttpObjectSet getHttpObjects(String identifier) {
        if (this.httpRepo.containsKey(identifier)) {
            return (HttpObjectSet) this.httpRepo.get(identifier);
        } else {
            return null;
        }
    }

    @Override
    public void removeHttpObjects(String identifier) {
        this.httpRepo.remove(identifier);
    }

    @Override
    public Map<String, Object> getAllTaskOutput(ProcessInstance instance) throws Exception {
        Map<String, Object> outputMap = new HashMap<>();
        Map jobMap = this.getJobMap(instance);
        Set set = jobMap.keySet();
        Iterator iterator = set.iterator();
        while (iterator.hasNext()) {
            String taskId = (String) iterator.next();
            String taskName = this.getTaskName(instance, taskId);
            outputMap.put(taskName, this.getTaskOutput(instance, taskId));
        }
        return outputMap;
    }

    @Override
    public void setTaskHistory(ProcessInstance instance, String taskId, TaskHistory history) throws Exception {
        this.setTaskParam(instance, taskId, "history", history);
    }

    @Override
    public TaskHistory getTaskHistory(ProcessInstance instance, String taskId) throws Exception {
        return (TaskHistory) this.getTaskParam(instance, taskId, "history", null);
    }

    @Override
    public List<TaskHistory> getAllTaskHistories(ProcessInstance instance) throws Exception {
        List<TaskHistory> taskHistories = new ArrayList<>();
        Map jobMap = this.getJobMap(instance);
        Set set = jobMap.keySet();
        Iterator iterator = set.iterator();
        while (iterator.hasNext()) {
            String taskId = (String) iterator.next();
            TaskHistory taskHistory = this.getTaskHistory(instance, taskId);
            if (taskHistory != null) {
                taskHistories.add(taskHistory);
            }
        }
        return taskHistories;
    }
}
