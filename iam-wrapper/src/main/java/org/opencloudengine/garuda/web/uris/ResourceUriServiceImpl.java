package org.opencloudengine.garuda.web.uris;

import org.opencloudengine.garuda.web.configuration.ConfigurationHelper;
import org.opencloudengine.garuda.web.iam.Iam;
import org.opencloudengine.garuda.web.iam.IamRepository;
import org.opencloudengine.garuda.web.iam.IamService;
import org.opencloudengine.garuda.web.policy.Policy;
import org.opencloudengine.garuda.web.policy.PolicyRepository;
import org.opencloudengine.garuda.web.workflow.Workflow;
import org.opencloudengine.garuda.web.workflow.WorkflowRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

@Service
public class ResourceUriServiceImpl implements ResourceUriService {
    @Autowired
    @Qualifier("config")
    private Properties config;

    @Autowired
    private ResourceUriRepository uriRepository;

    @Autowired
    private PolicyRepository policyRepository;

    @Autowired
    private WorkflowRepository workflowRepository;

    @Autowired
    ConfigurationHelper configurationHelper;

    @Override
    public ResourceUri createResourceUri(int order, String uri, String method, String runWith, String wid, String className, String policyId) {
        ResourceUri resourceUri = new ResourceUri();
        resourceUri.setOrder(order);
        resourceUri.setUri(uri);
        resourceUri.setMethod(method);
        resourceUri.setRunWith(runWith);
        resourceUri.setWid(wid);
        resourceUri.setClassName(className);
        resourceUri.setPolicyId(policyId);

        return uriRepository.insert(resourceUri);
    }

    @Override
    public List<ResourceUri> selectAll() {
        return uriRepository.selectAll();
    }

    @Override
    public List<ResourceUri> select(int limit, Long skip) {
        return this.joinByIds(uriRepository.select(limit, skip));
    }

    private List<ResourceUri> joinByIds(List<ResourceUri> uris) {
        List<String> policyIds = new ArrayList<>();
        for (ResourceUri uri : uris) {
            if (uri.getRunWith().equals("policy")) {
                policyIds.add(uri.getPolicyId());
            }
        }
        List<Policy> policies = policyRepository.selectByIds(policyIds);
        for (Policy policy : policies) {
            for (ResourceUri uri : uris) {
                if (policy.get_id().equals(uri.getPolicyId())) {
                    uri.setPolicyName(policy.getName());
                }
            }
        }

        List<String> workflowIds = new ArrayList<>();
        for (ResourceUri uri : uris) {
            if (uri.getRunWith().equals("workflow")) {
                workflowIds.add(uri.getWid());
            }
        }
        List<Workflow> workflows = workflowRepository.selectByIds(workflowIds);
        for (Workflow workflow : workflows) {
            for (ResourceUri uri : uris) {
                if (workflow.get_id().equals(uri.getWid())) {
                    uri.setWorkflowName(workflow.getName());
                }
            }
        }
        return uris;
    }

    @Override
    public ResourceUri selectById(String id) {
        return uriRepository.selectById(id);
    }

    @Override
    public ResourceUri selectByOrder(int order) {
        return uriRepository.selectByOrder(order);
    }

    @Override
    public List<ResourceUri> selectLikeUri(String uri, int limit, Long skip) {
        return this.joinByIds(uriRepository.selectLikeUri(uri, limit, skip));
    }

    @Override
    public ResourceUri selectByUri(String uri) {
        return uriRepository.selectByUri(uri);
    }

    @Override
    public Long count() {
        return uriRepository.count();
    }

    @Override
    public Long countLikeUri(String uri) {
        return uriRepository.countLikeUri(uri);
    }

    @Override
    public ResourceUri updateById(ResourceUri resourceUri) {
        return uriRepository.updateById(resourceUri);
    }

    @Override
    public ResourceUri updateById(String id, int order, String uri, String method, String runWith, String wid, String className, String policyId) {
        ResourceUri resourceUri = new ResourceUri();
        resourceUri.setOrder(order);
        resourceUri.set_id(id);
        resourceUri.setUri(uri);
        resourceUri.setMethod(method);
        resourceUri.setRunWith(runWith);
        resourceUri.setWid(wid);
        resourceUri.setClassName(className);
        resourceUri.setPolicyId(policyId);

        return uriRepository.updateById(resourceUri);
    }

    @Override
    public void deleteById(String id) {
        uriRepository.deleteById(id);
    }
}
