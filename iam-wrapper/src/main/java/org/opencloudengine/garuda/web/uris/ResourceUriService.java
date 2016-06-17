package org.opencloudengine.garuda.web.uris;

import org.opencloudengine.garuda.web.iam.Iam;

import java.util.List;

public interface ResourceUriService {

    ResourceUri createResourceUri(int order,
                                  String uri,
                                  String method,
                                  String runWith,
                                  String wid,
                                  String className);

    List<ResourceUri> selectAll();

    List<ResourceUri> select(int limit, Long skip);

    ResourceUri selectById(String id);

    List<ResourceUri> selectLikeUri(String uri, int limit, Long skip);

    ResourceUri selectByUri(String uri);

    Long count();

    Long countLikeUri(String uri);

    ResourceUri updateById(ResourceUri resourceUri);

    ResourceUri updateById(String id,
                           int order,
                           String uri,
                           String method,
                           String runWith,
                           String wid,
                           String className);

    void deleteById(String id);
}
