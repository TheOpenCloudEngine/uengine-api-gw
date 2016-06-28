package org.opencloudengine.garuda.web.uris;

import org.opencloudengine.garuda.web.iam.Iam;

import java.util.List;

public interface ResourceUriService {

    ResourceUri createResourceUri(int order,
                                  String uri,
                                  String method,
                                  String runWith,
                                  String wid,
                                  String className,
                                  String policyId);

    List<ResourceUri> selectAll();

    List<ResourceUri> select(int limit, Long skip);

    ResourceUri selectById(String id);

    ResourceUri selectByOrder(int order);

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
                           String className,
                           String policyId);

    void deleteById(String id);
}
