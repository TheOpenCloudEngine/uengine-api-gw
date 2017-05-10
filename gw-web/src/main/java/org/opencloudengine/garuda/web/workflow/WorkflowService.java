package org.opencloudengine.garuda.web.workflow;

import org.opencloudengine.garuda.web.policy.Policy;

import java.util.List;

public interface WorkflowService {


    Workflow createWorkflow(String name,
                            String designer_xml) throws Exception;

    List<Workflow> selectAll();

    List<Workflow> select(int limit, Long skip);

    Workflow selectById(String id);

    Workflow cashById(String id);

    List<Workflow> selectLikeName(String name, int limit, Long skip);

    Workflow selectByName(String name);

    Long count();

    Long countLikeName(String name);

    Workflow updateById(Workflow workflow) throws Exception;

    Workflow updateById(String id,
                      String name,
                      String designer_xml) throws Exception;

    void deleteById(String id);
}
