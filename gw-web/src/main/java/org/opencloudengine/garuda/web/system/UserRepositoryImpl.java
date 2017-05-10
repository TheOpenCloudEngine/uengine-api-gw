/**
 * Copyright (C) 2011 Flamingo Project (http://www.opencloudengine.org).
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
package org.opencloudengine.garuda.web.system;

import com.cloudant.client.api.model.Response;
import com.cloudant.client.api.views.Key;
import com.cloudant.client.api.views.ViewRequestBuilder;
import org.opencloudengine.garuda.couchdb.CouchServiceFactory;
import org.opencloudengine.garuda.model.User;
import org.opencloudengine.garuda.util.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Properties;

/**
 * @author Seungpil PARK
 */
@Repository
public class UserRepositoryImpl implements UserRepository {

    @Autowired
    @Qualifier("config")
    private Properties config;

    @Override
    public User selectUser() {
        User user = new User();


        user.setEmail(config.getProperty("system.admin.username"));
        user.setPassword(config.getProperty("system.admin.password"));
        user.setName("시스템 관리자");
        user.setAuthority("ROLE_ADMIN");

        user.setEnabled(true);
        user.setLevel("1");
        user.setCountry("KR");

        return user;
    }

}