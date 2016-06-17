package org.opencloudengine.garuda.web.workflow;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

@Controller
@RequestMapping("/")
public class WorkflowController {
    @Autowired
    @Qualifier("config")
    private Properties config;

    @RequestMapping(value = "/workflow", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public ModelAndView load(HttpSession session) {
        ModelAndView mav = new ModelAndView("/workflow/list");
        return mav;
    }

    @RequestMapping(value = "/workflow/list", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody String list(@RequestParam(value = "limit", required = false, defaultValue = "10") int limit,
                                     @RequestParam(value = "skip", required = false, defaultValue = "0") int skip,
                                     @RequestParam(value = "managementName", required = false, defaultValue = "") String managementName) {

//        Long count;
//        List<Management> managements;
//        String id = SessionUtils.getId();
//
//        if(managementName.length() > 0) {
//            count = managementService.countAllByUserIdLikeManagementName(id, managementName);
//            managements = managementService.selectByUserIdLikeManagementName(id, managementName, limit, new Long(skip));
//
//        } else {
//            count = managementService.countAllByUserId(id);
//            managements = managementService.selectByUserId(id, limit, new Long(skip));
//        }
//
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("recordsTotal", limit);
//        jsonObject.put("recordsFiltered", count);
//        jsonObject.put("displayStart", skip);
//        jsonObject.put("data", managements);
//
//        return jsonObject.toString();
        return null;
    }

    @RequestMapping(value = "/workflow-new", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public ModelAndView newManagement(HttpSession session) {
        ModelAndView mav = new ModelAndView("/workflow/new");
        return mav;
    }

//    @RequestMapping(value = "/create", method = RequestMethod.POST)
//    @ResponseStatus(HttpStatus.OK)
//    public ModelAndView create(HttpSession session,
//                               @RequestParam(defaultValue = "") String managementName,
//                               @RequestParam(defaultValue = "") String description,
//                               @RequestParam(defaultValue = "") Integer sessionTokenLifetime,
//                               @RequestParam(defaultValue = "") Integer scopeCheckLifetime) throws IOException {
//
//        try {
//            managementService.createManagement(SessionUtils.getId(), managementName, description, sessionTokenLifetime, scopeCheckLifetime);
//            ModelAndView mav = new ModelAndView("/management/list");
//
//            List<Management> managements = managementService.selectAllByUserId(SessionUtils.getId());
//            mav.addObject("managements", managements);
//            return mav;
//        } catch (Exception ex) {
//            ModelAndView mav = new ModelAndView("/management/new");
//            mav.addObject("failed", true);
//            return mav;
//        }
//    }
}
