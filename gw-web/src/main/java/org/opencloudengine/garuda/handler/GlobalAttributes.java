package org.opencloudengine.garuda.handler;

import org.opencloudengine.garuda.gateway.GatewayServlet;
import org.opencloudengine.garuda.history.TaskHistory;
import org.opencloudengine.garuda.model.HttpObjectSet;
import org.opencloudengine.garuda.web.uris.ResourceUri;
import org.uengine.kernel.ProcessInstance;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface GlobalAttributes {

    public static final String NAMESPACE = GlobalAttributes.class.getName();

    void registJobResultMap(ProcessInstance instance) throws Exception;

    Map getJobMap(ProcessInstance instance) throws Exception;

    void setJobMap(ProcessInstance instance, Map jobMap) throws Exception;

    String getTaskStatus(ProcessInstance instance, String taskId) throws Exception;

    void setTaskStatus(ProcessInstance instance, String taskId, String status) throws Exception;

    Object getTaskInput(ProcessInstance instance, String taskId) throws Exception;

    void setTaskInput(ProcessInstance instance, String taskId, Object data) throws Exception;

    Object getTaskOutput(ProcessInstance instance, String taskId) throws Exception;

    void setTaskOutput(ProcessInstance instance, String taskId, Object data) throws Exception;

    String getTaskNext(ProcessInstance instance, String taskId) throws Exception;

    void setTaskNext(ProcessInstance instance, String taskId, String next) throws Exception;

    Map<String, String> getTaskProperties(ProcessInstance instance, String taskId) throws Exception;

    String getTaskName(ProcessInstance instance, String taskId) throws Exception;

    String getTaskIdByName(ProcessInstance instance, String label) throws Exception;

    void setHttpObjects(String identifier, GatewayServlet servlet, HttpServletRequest servletRequest, HttpServletResponse servletResponse, ResourceUri resourceUri);

    HttpObjectSet getHttpObjects(String identifier);

    void removeHttpObjects(String identifier);

    Map<String, Object> getAllTaskOutput(ProcessInstance instance) throws Exception;

    void setTaskHistory(ProcessInstance instance, String taskId, TaskHistory history) throws Exception;

    TaskHistory getTaskHistory(ProcessInstance instance, String taskId) throws Exception;

    List<TaskHistory> getAllTaskHistories(ProcessInstance instance) throws Exception;
}
