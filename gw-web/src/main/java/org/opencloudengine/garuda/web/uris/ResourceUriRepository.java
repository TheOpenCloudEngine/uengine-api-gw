package org.opencloudengine.garuda.web.uris;

import org.opencloudengine.garuda.common.repository.PersistentRepository;

import java.util.List;

public interface ResourceUriRepository {

    List<ResourceUri> getCash();

    ResourceUri insert(ResourceUri resourceUri);

    List<ResourceUri> selectAll();

    List<ResourceUri> select(int limit, Long skip);

    ResourceUri selectById(String id);

    ResourceUri selectByOrder(int order);

    List<ResourceUri> selectLikeUri(String uri, int limit, Long skip);

    ResourceUri selectByUri(String uri);

    Long count();

    Long countLikeUri(String uri);

    ResourceUri updateById(ResourceUri resourceUri);

    void deleteById(String id);
}
