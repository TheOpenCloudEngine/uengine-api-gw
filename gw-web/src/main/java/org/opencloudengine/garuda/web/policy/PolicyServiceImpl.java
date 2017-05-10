package org.opencloudengine.garuda.web.policy;

import org.opencloudengine.garuda.web.configuration.ConfigurationHelper;
import org.opencloudengine.garuda.web.uris.ResourceUri;
import org.opencloudengine.garuda.web.uris.ResourceUriRepository;
import org.opencloudengine.garuda.web.uris.ResourceUriService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Properties;

@Service
public class PolicyServiceImpl implements PolicyService {
    @Autowired
    @Qualifier("config")
    private Properties config;

    @Autowired
    private PolicyRepository policyRepository;

    @Autowired
    ConfigurationHelper configurationHelper;

    @Override
    public Policy createPolicy(String name,
                               String authentication,
                               String tokenLocation,
                               String tokenName,
                               String proxyUri,
                               String prefixUri,
                               String beforeUse,
                               String afterUse) {

        Policy policy = new Policy();
        policy.setName(name);
        policy.setAuthentication(authentication);
        policy.setTokenLocation(tokenLocation);
        policy.setTokenName(tokenName);
        policy.setProxyUri(proxyUri);
        policy.setPrefixUri(prefixUri);
        policy.setBeforeUse(beforeUse);
        policy.setAfterUse(afterUse);

        return policyRepository.insert(policy);
    }

    @Override
    public List<Policy> selectAll() {
        return policyRepository.selectAll();
    }

    @Override
    public List<Policy> select(int limit, Long skip) {
        return policyRepository.select(limit, skip);
    }

    @Override
    public Policy selectById(String id) {
        return policyRepository.selectById(id);
    }

    @Override
    public Policy cashById(String id) {
        Policy cashPolicy = null;
        List<Policy> cash = policyRepository.getCash();
        for (Policy policy : cash) {
            if(policy.get_id().equals(id)){
                cashPolicy = policy;
            }
        }
        return cashPolicy;
    }

    @Override
    public List<Policy> selectLikeName(String name, int limit, Long skip) {
        return policyRepository.selectLikeName(name, limit, skip);
    }

    @Override
    public Policy selectByName(String name) {
        return policyRepository.selectByName(name);
    }

    @Override
    public Long count() {
        return policyRepository.count();
    }

    @Override
    public Long countLikeName(String name) {
        return policyRepository.countLikeName(name);
    }

    @Override
    public Policy updateById(Policy policy) {
        return policyRepository.updateById(policy);
    }

    @Override
    public Policy updateById(String id,
                                  String name,
                                  String authentication,
                                  String tokenLocation,
                                  String tokenName,
                                  String proxyUri,
                                  String prefixUri,
                                  String beforeUse,
                                  String afterUse) {
        Policy policy = new Policy();
        policy.set_id(id);
        policy.setName(name);
        policy.setAuthentication(authentication);
        policy.setTokenLocation(tokenLocation);
        policy.setTokenName(tokenName);
        policy.setProxyUri(proxyUri);
        policy.setPrefixUri(prefixUri);
        policy.setBeforeUse(beforeUse);
        policy.setAfterUse(afterUse);

        return policyRepository.updateById(policy);
    }

    @Override
    public void deleteById(String id) {
        policyRepository.deleteById(id);
    }
}
