/*
 * Copyright (C) 2015 Bahamas Project (http://www.opencloudengine.org).
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.opencloudengine.garuda.handler.activity.workflow;

import org.opencloudengine.garuda.handler.AbstractHandler;
import org.opencloudengine.garuda.handler.GlobalAttributes;
import org.opencloudengine.garuda.util.ApplicationContextRegistry;
import org.opencloudengine.garuda.web.history.TaskHistory;
import org.opencloudengine.garuda.web.history.TaskHistoryRepository;
import org.opencloudengine.garuda.web.history.WorkflowHistory;
import org.opencloudengine.garuda.web.history.WorkflowHistoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.uengine.kernel.ProcessInstance;

import java.util.*;

public abstract class AbstractTask extends AbstractHandler {

    /**
     * SLF4J Logging
     */
    private Logger logger = LoggerFactory.getLogger(AbstractTask.class);

    /**
     * Task가 실행하는 시점에서 Task의 각종 파라미터를 Key Value넣은 자료형.
     */
    protected Map<String, String> params;

    /**
     * 실행중인 워크플로우 객체
     */
    ProcessInstance instance;

    /**
     * 실행중인 워크플로우 identifier
     */
    String identifier;

    /**
     * 실행중인 워크플로우 ID
     */
    String wid;

    String taskId;

    String taskName;

    WorkflowHistoryRepository workflowHistoryRepository;

    TaskHistoryRepository taskHistoryRepository;

    TaskHistory taskHistory;

    WorkflowHistory workflowHistory;

    GlobalAttributes globalAttributes;

    String stdout;

    String stderr;

    Object inputData;

    Object outputData;

    @Override
    protected void executeActivity(final ProcessInstance instance) throws Exception {

        ApplicationContext context = ApplicationContextRegistry.getApplicationContext();
        globalAttributes = context.getBean(GlobalAttributes.class);
        workflowHistoryRepository = context.getBean(WorkflowHistoryRepository.class);
        taskHistoryRepository = context.getBean(TaskHistoryRepository.class);

        this.instance = instance;
        this.workflowHistory = (WorkflowHistory) instance.get("wh");
        this.identifier = workflowHistory.getIdentifier();
        this.wid = workflowHistory.getWid();
        this.taskId = getTracingTag();
        this.taskName = getName();

        this.params = globalAttributes.getTaskProperties(instance, taskId);

        /**
         * httpObject init
         */
        this.init(globalAttributes.getHttpObjects(identifier));

        if (logger.isDebugEnabled()) {
            Set<String> names = params.keySet();
            for (String name : names) {

                logger.debug("[Local Variable] {} = {}", name, params.get(name));
            }
        }
        this.doExecute();
    }

    abstract public void doExecute() throws Exception;

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }
}