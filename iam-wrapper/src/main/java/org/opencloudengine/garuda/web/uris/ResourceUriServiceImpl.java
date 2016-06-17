package org.opencloudengine.garuda.web.uris;

import org.opencloudengine.garuda.web.configuration.ConfigurationHelper;
import org.opencloudengine.garuda.web.iam.Iam;
import org.opencloudengine.garuda.web.iam.IamRepository;
import org.opencloudengine.garuda.web.iam.IamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

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
    ConfigurationHelper configurationHelper;

    @Override
    public ResourceUri createResourceUri(int order, String uri, String method, String runWith, String wid, String className) {
        ResourceUri resourceUri = new ResourceUri();
        resourceUri.setOrder(order);
        resourceUri.setUri(uri);
        resourceUri.setMethod(method);
        resourceUri.setRunWith(runWith);
        resourceUri.setWid(wid);
        resourceUri.setClassName(className);

        return uriRepository.insert(resourceUri);
    }

    @Override
    public List<ResourceUri> selectAll() {
        return uriRepository.selectAll();
    }

    @Override
    public List<ResourceUri> select(int limit, Long skip) {
        return uriRepository.select(limit, skip);
    }

    @Override
    public ResourceUri selectById(String id) {
        return uriRepository.selectById(id);
    }

    @Override
    public List<ResourceUri> selectLikeUri(String uri, int limit, Long skip) {
        return uriRepository.selectLikeUri(uri, limit, skip);
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
    public ResourceUri updateById(String id, int order, String uri, String method, String runWith, String wid, String className) {
        ResourceUri resourceUri = new ResourceUri();
        resourceUri.setOrder(order);
        resourceUri.set_id(id);
        resourceUri.setUri(uri);
        resourceUri.setMethod(method);
        resourceUri.setRunWith(runWith);
        resourceUri.setWid(wid);
        resourceUri.setClassName(className);

        return uriRepository.updateById(resourceUri);
    }

    @Override
    public void deleteById(String id) {
        uriRepository.deleteById(id);
    }
}
