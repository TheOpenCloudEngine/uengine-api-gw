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

import org.apache.commons.codec.binary.Base64;
import org.opencloudengine.garuda.common.security.SessionUtils;
import org.opencloudengine.garuda.model.User;
import org.opencloudengine.garuda.util.DateUtils;
import org.opencloudengine.garuda.web.configuration.DefaultController;
import org.opencloudengine.garuda.web.security.AESPasswordEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Date;

/**
 * Security Auth Controller
 *
 * @author Seungpil PARK
 * @since 2.0
 */
@Controller
@RequestMapping("/auth")
public class UserController extends DefaultController {

    @Autowired
    private UserService userService;

    /**
     * SLF4J Logging
     */
    private Logger logger = LoggerFactory.getLogger(UserController.class);

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView login(HttpSession session) {
        return new ModelAndView("/auth/login");
    }


    @RequestMapping(value = "/fail", method = RequestMethod.GET)
    public ModelAndView fail(HttpSession session) {
        return new ModelAndView("/auth/fail");
    }

}
