package org.opencloudengine.garuda.web.iam;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpSession;
import java.util.Properties;

@Controller
@RequestMapping("/iam")
public class IamController {
    @Autowired
    @Qualifier("config")
    private Properties config;

    @Autowired
    private IamService iamService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public ModelAndView info(HttpSession session) {
        ModelAndView mav = new ModelAndView("/iam/info");

        Iam iam = iamService.select();
        mav.addObject("iam", iam);

        return mav;
    }

    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public ModelAndView edit(HttpSession session) {
        ModelAndView mav = new ModelAndView("/iam/edit");

        Iam iam = iamService.select();
        mav.addObject("iam", iam);

        return mav;
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public ModelAndView save(HttpSession session,
                             @RequestParam(defaultValue = "") String host,
                             @RequestParam(defaultValue = "") int port,
                             @RequestParam(defaultValue = "") String managementKey,
                             @RequestParam(defaultValue = "") String managementSecret) {

        iamService.update(host, port, managementKey, managementSecret);
        //ModelAndView mav = new ModelAndView("redirect:/iam/info");

        ModelAndView mav = new ModelAndView("/iam/info");
        Iam iam = iamService.select();
        mav.addObject("iam", iam);
        return mav;
    }
}
