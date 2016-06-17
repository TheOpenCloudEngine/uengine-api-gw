package org.opencloudengine.garuda.web.iam;

import org.opencloudengine.garuda.common.exception.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by uengine on 2016. 6. 16..
 */
@Component
public class IamServiceFactory {

    @Autowired
    IamService iamService;

    /**
     * SLF4J Logging
     */
    private Logger logger = LoggerFactory.getLogger(IamServiceFactory.class);

    public IamApi create() {
        Iam iam = iamService.select();
        if (iam == null) {
            throw new ServiceException("No Iam connection Data");
        }
        return new IamApi(iam.getHost(), iam.getPort(), iam.getManagementKey(), iam.getManagementSecret());
    }
}
