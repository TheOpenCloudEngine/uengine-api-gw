/**
 * Copyright (C) 2011  Flamingo Project (http://www.opencloudengine.org).
 * <p/>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p/>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.opencloudengine.garuda.couchdb;

import com.cloudant.client.api.Database;
import com.cloudant.client.api.views.AllDocsResponse;
import org.opencloudengine.garuda.common.exception.ServiceException;
import org.opencloudengine.garuda.util.JsonUtils;
import org.opencloudengine.garuda.util.ResourceUtils;
import org.opencloudengine.garuda.web.iam.Iam;
import org.opencloudengine.garuda.web.iam.IamRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * @author Seungpil, Park
 * @since 0.1
 */
@Service
public class CouchView implements InitializingBean {

    /**
     * SLF4J Logging
     */
    private Logger logger = LoggerFactory.getLogger(CouchView.class);

    @Autowired
    @Qualifier("config")
    private Properties config;

    @Autowired
    private CouchServiceFactory serviceFactory;

    @Autowired
    private IamRepository iamRepository;

    @Override
    public void afterPropertiesSet() throws Exception {

        String property = config.getProperty("couch.db.autoview");
        if (property.equals("false")) {
            return;
        }

        URL url = getClass().getResource("/views");
        File views = ResourceUtils.getFile(url);
        if (!views.exists()) {
            throw new ServiceException("class folder views not exist");
        }

        File[] files = views.listFiles();
        for (File file : files) {
            String json = new String(Files.readAllBytes(file.toPath()));
            Map map = JsonUtils.unmarshal(json);

            Database db = serviceFactory.getDb();

            AllDocsResponse viewResponse = db.getAllDocsRequestBuilder().keys((String) map.get("_id")).includeDocs(true).build().getResponse();
            List<Map> docsAs = viewResponse.getDocsAs(Map.class);
            if (docsAs.isEmpty()) {
                db.save(map);
            } else {
                Map viewDoc = docsAs.get(0);
                Map<String, Object> copy = JsonUtils.convertClassToMap(viewDoc);
                copy.remove("_rev");
                if (!copy.equals(map)) {
                    db.remove(viewDoc);
                    db.save(map);
                }
            }
        }

        //기본 Iam 등록
        if (iamRepository.select() == null) {
            Iam iam = new Iam();
            iam.setHost("localhost");
            iam.setPort(8080);
            iam.setManagementKey("");
            iam.setManagementSecret("");

            iamRepository.insert(iam);
        }
    }
}
