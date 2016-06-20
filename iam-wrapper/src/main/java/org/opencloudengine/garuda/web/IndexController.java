package org.opencloudengine.garuda.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.Locale;
import java.util.Properties;

@Controller
@RequestMapping("/")
public class IndexController {

    /**
     * SLF4J Logging
     */
    private Logger logger = LoggerFactory.getLogger(IndexController.class);

    @Autowired
    @Qualifier("config")
    private Properties config;


    /**
     * 인덱스 페이지로 이동한다.
     *
     * @return Model And View
     */
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public ModelAndView index(HttpSession session, final Locale locale) {
        //session.setAttribute("lang", locale.toString());
        return new ModelAndView("index");
    }

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView home(HttpSession session, final Locale locale) {
        //session.setAttribute("lang", locale.toString());
        return new ModelAndView("index");
    }

    @RequestMapping(value = "/redirect", method = RequestMethod.GET)
    public ModelAndView redirect(@RequestParam String url) {
        return new ModelAndView("redirect:" + url);
    }

    @RequestMapping(value = "/go", method = RequestMethod.GET)
    public ModelAndView go(@RequestParam String url) {
        return new ModelAndView(url);
    }

}
