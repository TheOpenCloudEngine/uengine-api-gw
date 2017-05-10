package org.opencloudengine.garuda.web.policy;

import org.opencloudengine.garuda.web.uris.ResourceUri;

import java.util.List;

public interface PolicyRepository {

    List<Policy> getCash();

    Policy insert(Policy policy);

    List<Policy> selectAll();

    List<Policy> select(int limit, Long skip);

    Policy selectById(String id);

    List<Policy> selectByIds(List<String> ids);

    List<Policy> selectLikeName(String name, int limit, Long skip);

    Policy selectByName(String name);

    Long count();

    Long countLikeName(String name);

    Policy updateById(Policy policy);

    void deleteById(String id);
}
