package org.opencloudengine.garuda.web.policy;

import org.opencloudengine.garuda.web.uris.ResourceUri;

import java.util.List;

public interface PolicyService {


    Policy createPolicy(String name,
                        String authentication,
                        String tokenLocation,
                        String tokenName,
                        String proxyUri,
                        String prefixUri,
                        String beforeUse,
                        String afterUse
    );

    List<Policy> selectAll();

    List<Policy> select(int limit, Long skip);

    Policy selectById(String id);

    Policy cashById(String id);

    List<Policy> selectLikeName(String name, int limit, Long skip);

    Policy selectByName(String name);

    Long count();

    Long countLikeName(String name);

    Policy updateById(Policy policy);

    Policy updateById(String id,
                      String name,
                      String authentication,
                      String tokenLocation,
                      String tokenName,
                      String proxyUri,
                      String prefixUri,
                      String beforeUse,
                      String afterUse);

    void deleteById(String id);
}
