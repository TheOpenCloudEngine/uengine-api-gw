package org.opencloudengine.garuda.web.workflow;

import org.opencloudengine.garuda.web.policy.Policy;

import java.util.List;

public interface WorkflowService {


    Workflow createWorkflow(String name,
                            String designer_xml,
                            String vars,
                            String status
    );

    List<Workflow> selectAll();

    List<Workflow> select(int limit, Long skip);

    Workflow selectById(String id);

    List<Workflow> selectLikeName(String name, int limit, Long skip);

    Workflow selectByName(String name);

    Long count();

    Long countLikeName(String name);

    Workflow updateById(Workflow workflow);

    Workflow updateById(String id,
                      String name,
                      String designer_xml,
                      String vars,
                      String status);

    void deleteById(String id);
}
