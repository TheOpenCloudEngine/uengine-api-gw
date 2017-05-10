package org.opencloudengine.garuda.web.uris;


import net.minidev.json.JSONObject;
import org.opencloudengine.garuda.common.exception.ServiceException;
import org.opencloudengine.garuda.util.JsonUtils;
import org.opencloudengine.garuda.web.iam.Iam;
import org.opencloudengine.garuda.web.iam.IamService;
import org.opencloudengine.garuda.web.policy.Policy;
import org.opencloudengine.garuda.web.policy.PolicyService;
import org.opencloudengine.garuda.web.workflow.Workflow;
import org.opencloudengine.garuda.web.workflow.WorkflowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

@Controller
@RequestMapping("/uris")
public class ResourceUriController {
    @Autowired
    @Qualifier("config")
    private Properties config;

    @Autowired
    private ResourceUriService uriService;

    @Autowired
    private PolicyService policyService;

    @Autowired
    private WorkflowService workflowService;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public String load() {
        return "/uris/list";
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
                @RequestParam(value = "uri", required = false, defaultValue = "") String uri) {

        Long count;
        List<ResourceUri> resourceUris;

        if (uri.length() > 0) {
            count = uriService.countLikeUri(uri);
            resourceUris = uriService.selectLikeUri(uri, limit, new Long(skip));

        } else {
            count = uriService.count();
            resourceUris = uriService.select(limit, new Long(skip));
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("recordsTotal", limit);
        jsonObject.put("recordsFiltered", count);
        jsonObject.put("displayStart", skip);
        jsonObject.put("data", resourceUris);

        return jsonObject.toString();
    }

    @RequestMapping(value = "/new", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public ModelAndView newUri(HttpSession session) {

        List<Policy> policies = policyService.selectAll();
        List<Workflow> workflows = workflowService.selectAll();

        ModelAndView mav = new ModelAndView("/uris/new");
        mav.addObject("policies", policies);
        mav.addObject("workflows", workflows);
        return mav;
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public ModelAndView create(HttpSession session,HttpServletResponse response,
                               @RequestParam(defaultValue = "1") int order,
                               @RequestParam(defaultValue = "") String uri,
                               @RequestParam(defaultValue = "") String method,
                               @RequestParam(defaultValue = "") String runWith,
                               @RequestParam(defaultValue = "") String wid,
                               @RequestParam(defaultValue = "") String className,
                               @RequestParam(defaultValue = "") String policyId
    ) throws IOException {

        List<Policy> policies = policyService.selectAll();
        List<Workflow> workflows = workflowService.selectAll();

        try {
            //같은 오더 검색
            ResourceUri existUri = uriService.selectByOrder(order);
            if (existUri != null) {
                ModelAndView mav = new ModelAndView("/uris/new");
                mav.addObject("duplicate", true);
                mav.addObject("policies", policies);
                mav.addObject("workflows", workflows);
                return mav;
            }

            uriService.createResourceUri(order, uri, method, runWith, wid, className, policyId);

            //리스트 페이지 반환
            response.sendRedirect("/service-console/uris");
            return null;

        } catch (Exception ex) {
            ModelAndView mav = new ModelAndView("/uris/new");
            mav.addObject("failed", true);
            mav.addObject("policies", policies);
            mav.addObject("workflows", workflows);
            return mav;
        }
    }

    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public ModelAndView edit(HttpSession session,
                             @RequestParam(defaultValue = "") String _id) throws IOException {

        try {
            ResourceUri resourceUri = uriService.selectById(_id);
            if (resourceUri == null) {
                throw new ServiceException("Invalid resourceUri id");
            }

            List<Policy> policies = policyService.selectAll();
            List<Workflow> workflows = workflowService.selectAll();

            ModelAndView mav = new ModelAndView("/uris/edit");
            mav.addObject("resourceUri", resourceUri);
            mav.addObject("policies", policies);
            mav.addObject("workflows", workflows);
            return mav;
        } catch (Exception ex) {
            throw new ServiceException("Invalid resourceUri id");
        }
    }

    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public ModelAndView delete(HttpSession session,HttpServletResponse response,
                               @RequestParam(defaultValue = "") String _id) throws IOException {

        try {
            ResourceUri resourceUri = uriService.selectById(_id);
            if (resourceUri == null) {
                throw new ServiceException("Invalid resourceUri id");
            }

            uriService.deleteById(_id);

            response.sendRedirect("/service-console/uris");
            return null;

        } catch (Exception ex) {
            throw new ServiceException("Invalid resourceUri id");
        }
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public ModelAndView update(HttpSession session,HttpServletResponse response,
                               @RequestParam(defaultValue = "1") int order,
                               @RequestParam(defaultValue = "") String _id,
                               @RequestParam(defaultValue = "") String uri,
                               @RequestParam(defaultValue = "") String method,
                               @RequestParam(defaultValue = "") String runWith,
                               @RequestParam(defaultValue = "") String wid,
                               @RequestParam(defaultValue = "") String className,
                               @RequestParam(defaultValue = "") String policyId
    ) throws IOException {

        ResourceUri resourceUri = uriService.selectById(_id);
        if (resourceUri == null) {
            throw new ServiceException("Invalid resourceUri id");
        }

        List<Policy> policies = policyService.selectAll();
        List<Workflow> workflows = workflowService.selectAll();
        try {
            //같은 오더 검색
            ResourceUri existUri = uriService.selectByOrder(order);
            if (existUri != null) {
                if (!existUri.get_id().equals(_id)) {
                    ModelAndView mav = new ModelAndView("/uris/edit");
                    mav.addObject("resourceUri", resourceUri);
                    mav.addObject("duplicate", true);
                    mav.addObject("policies", policies);
                    mav.addObject("workflows", workflows);
                    return mav;
                }
            }

            uriService.updateById(_id, order, uri, method, runWith, wid, className, policyId);
            response.sendRedirect("/service-console/uris");
            return null;

        } catch (Exception ex) {
            ModelAndView mav = new ModelAndView("/uris/edit");
            mav.addObject("resourceUri", resourceUri);
            mav.addObject("failed", true);
            mav.addObject("policies", policies);
            mav.addObject("workflows", workflows);
            return mav;
        }
    }
}
