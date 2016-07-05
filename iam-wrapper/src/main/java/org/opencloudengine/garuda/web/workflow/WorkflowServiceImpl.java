package org.opencloudengine.garuda.web.workflow;

import org.opencloudengine.garuda.web.configuration.ConfigurationHelper;
import org.opencloudengine.garuda.web.policy.Policy;
import org.opencloudengine.garuda.web.policy.PolicyRepository;
import org.opencloudengine.garuda.web.policy.PolicyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Properties;

@Service
public class WorkflowServiceImpl implements WorkflowService {
    @Autowired
    @Qualifier("config")
    private Properties config;

    @Autowired
    private WorkflowRepository workflowRepository;

    @Autowired
    ConfigurationHelper configurationHelper;

    @Override
    public Workflow createWorkflow(String name,
                                 String designer_xml) {

        Workflow workflow = new Workflow();
        workflow.setName(name);
        workflow.setDesigner_xml(designer_xml);
        return workflowRepository.insert(workflow);
    }

    @Override
    public List<Workflow> selectAll() {
        return workflowRepository.selectAll();
    }

    @Override
    public List<Workflow> select(int limit, Long skip) {
        return workflowRepository.select(limit, skip);
    }

    @Override
    public Workflow selectById(String id) {
        return workflowRepository.selectById(id);
    }

    @Override
    public List<Workflow> selectLikeName(String name, int limit, Long skip) {
        return workflowRepository.selectLikeName(name, limit, skip);
    }

    @Override
    public Workflow selectByName(String name) {
        return workflowRepository.selectByName(name);
    }

    @Override
    public Long count() {
        return workflowRepository.count();
    }

    @Override
    public Long countLikeName(String name) {
        return workflowRepository.countLikeName(name);
    }

    @Override
    public Workflow updateById(Workflow workflow) {
        return workflowRepository.updateById(workflow);
    }

    @Override
    public Workflow updateById(String id,
                               String name,
                               String designer_xml) {
        Workflow workflow = new Workflow();
        workflow.set_id(id);
        workflow.setName(name);
        workflow.setDesigner_xml(designer_xml);

        return workflowRepository.updateById(workflow);
    }

    @Override
    public void deleteById(String id) {
        workflowRepository.deleteById(id);
    }
}
