package org.opencloudengine.garuda.web.workflow;

import org.opencloudengine.garuda.common.repository.PersistentRepository;
import org.opencloudengine.garuda.web.policy.Policy;

import java.util.List;

public interface WorkflowRepository {

    List<Workflow> getCash();

    Workflow insert(Workflow workflow);

    List<Workflow> selectAll();

    List<Workflow> select(int limit, Long skip);

    Workflow selectById(String id);

    List<Workflow> selectByIds(List<String> ids);

    List<Workflow> selectLikeName(String name, int limit, Long skip);

    Workflow selectByName(String name);

    Long count();

    Long countLikeName(String name);

    Workflow updateById(Workflow workflow);

    void deleteById(String id);
}
