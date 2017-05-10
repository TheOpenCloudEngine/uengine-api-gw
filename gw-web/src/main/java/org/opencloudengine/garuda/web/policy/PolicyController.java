package org.opencloudengine.garuda.web.policy;


import net.minidev.json.JSONObject;
import org.opencloudengine.garuda.common.exception.ServiceException;
import org.opencloudengine.garuda.web.uris.ResourceUri;
import org.opencloudengine.garuda.web.uris.ResourceUriService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

@Controller
@RequestMapping("/policy")
public class PolicyController {
    @Autowired
    @Qualifier("config")
    private Properties config;

    @Autowired
    private PolicyService policyService;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public String load() {
        return "/policy/list";
    }

    // limit default value is javascript datatables _iDisplayLength
    // plz check user/list.jsp
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public
    @ResponseBody
    String list(HttpSession session,
                @RequestParam(value = "limit", required = false, defaultValue = "10") int limit,
                @RequestParam(value = "skip", required = false, defaultValue = "0") int skip,
                @RequestParam(value = "name", required = false, defaultValue = "") String name) {

        Long count;
        List<Policy> policies;

        if (name.length() > 0) {
            count = policyService.countLikeName(name);
            policies = policyService.selectLikeName(name, limit, new Long(skip));

        } else {
            count = policyService.count();
            policies = policyService.select(limit, new Long(skip));
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("recordsTotal", limit);
        jsonObject.put("recordsFiltered", count);
        jsonObject.put("displayStart", skip);
        jsonObject.put("data", policies);

        return jsonObject.toString();
    }

    @RequestMapping(value = "/new", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public ModelAndView newUri(HttpSession session) {

        ModelAndView mav = new ModelAndView("/policy/new");
        return mav;
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public ModelAndView create(HttpSession session, HttpServletResponse response,
                               @RequestParam(defaultValue = "") String name,
                               @RequestParam(defaultValue = "") String authentication,
                               @RequestParam(defaultValue = "") String tokenLocation,
                               @RequestParam(defaultValue = "") String tokenName,
                               @RequestParam(defaultValue = "") String proxyUri,
                               @RequestParam(defaultValue = "") String prefixUri,
                               @RequestParam(defaultValue = "") String beforeUse,
                               @RequestParam(defaultValue = "") String afterUse
    ) throws IOException {

        try {

            Policy existPolicy = policyService.selectByName(name);
            if (existPolicy != null) {
                ModelAndView mav = new ModelAndView("/policy/new");
                mav.addObject("duplicate", true);
                return mav;
            }

            policyService.createPolicy(name, authentication, tokenLocation, tokenName, proxyUri, prefixUri, beforeUse, afterUse);

            //리스트 페이지 반환
            response.sendRedirect("/service-console/policy");
            return null;

        } catch (Exception ex) {
            ModelAndView mav = new ModelAndView("/policy/new");
            mav.addObject("failed", true);
            return mav;
        }
    }

    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public ModelAndView edit(HttpSession session,
                             @RequestParam(defaultValue = "") String _id) throws IOException {

        try {
            Policy policy = policyService.selectById(_id);
            if (policy == null) {
                throw new ServiceException("Invalid policy id");
            }

            ModelAndView mav = new ModelAndView("/policy/edit");
            mav.addObject("policy", policy);
            return mav;
        } catch (Exception ex) {
            throw new ServiceException("Invalid policy id");
        }
    }

    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public ModelAndView delete(HttpSession session,HttpServletResponse response,
                               @RequestParam(defaultValue = "") String _id) throws IOException {

        try {
            Policy policy = policyService.selectById(_id);
            if (policy == null) {
                throw new ServiceException("Invalid policy id");
            }

            policyService.deleteById(_id);

            response.sendRedirect("/service-console/policy");
            return null;

        } catch (Exception ex) {
            throw new ServiceException("Invalid policy id");
        }
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public ModelAndView update(HttpSession session,HttpServletResponse response,
                               @RequestParam(defaultValue = "") String _id,
                               @RequestParam(defaultValue = "") String name,
                               @RequestParam(defaultValue = "") String authentication,
                               @RequestParam(defaultValue = "") String tokenLocation,
                               @RequestParam(defaultValue = "") String tokenName,
                               @RequestParam(defaultValue = "") String proxyUri,
                               @RequestParam(defaultValue = "") String prefixUri,
                               @RequestParam(defaultValue = "") String beforeUse,
                               @RequestParam(defaultValue = "") String afterUse
    ) throws IOException {

        Policy policy = policyService.selectById(_id);
        if (policy == null) {
            throw new ServiceException("Invalid policy id");
        }

        try {
            Policy existPolicy = policyService.selectByName(name);
            if (existPolicy != null) {
                if (!existPolicy.get_id().equals(_id)) {
                    ModelAndView mav = new ModelAndView("/policy/edit");
                    mav.addObject("policy", policy);
                    mav.addObject("duplicate", true);
                    return mav;
                }
            }

            policyService.updateById(_id, name, authentication, tokenLocation, tokenName, proxyUri, prefixUri, beforeUse, afterUse);
            response.sendRedirect("/service-console/policy");
            return null;

        } catch (Exception ex) {
            ModelAndView mav = new ModelAndView("/policy/edit");
            mav.addObject("policy", policy);
            mav.addObject("failed", true);
            return mav;
        }
    }
}
