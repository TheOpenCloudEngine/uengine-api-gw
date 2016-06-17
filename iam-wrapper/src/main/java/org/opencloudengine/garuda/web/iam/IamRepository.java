package org.opencloudengine.garuda.web.iam;

import org.opencloudengine.garuda.common.repository.PersistentRepository;
import org.opencloudengine.garuda.web.workflow.Workflow;

public interface IamRepository {

    public static final String NAMESPACE = IamRepository.class.getName();

    Iam getCash();

    Iam insert(Iam iam);

    Iam select();

    Iam update(Iam iam);
}
