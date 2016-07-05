package org.opencloudengine.garuda.web.workflow;

import net.minidev.json.JSONObject;
import org.opencloudengine.garuda.common.exception.ServiceException;
import org.opencloudengine.garuda.web.policy.Policy;
import org.opencloudengine.garuda.web.uris.ResourceUri;
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
@RequestMapping("/")
public class WorkflowController {
    @Autowired
    @Qualifier("config")
    private Properties config;

    @Autowired
    private WorkflowService workflowService;

    @RequestMapping(value = "/workflow", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public ModelAndView load(HttpSession session) {
        ModelAndView mav = new ModelAndView("/workflow/list");
        return mav;
    }

    @RequestMapping(value = "/workflow/list", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public
    @ResponseBody
    String list(@RequestParam(value = "limit", required = false, defaultValue = "10") int limit,
                @RequestParam(value = "skip", required = false, defaultValue = "0") int skip,
                @RequestParam(value = "name", required = false, defaultValue = "") String name) {

        Long count;
        List<Workflow> workflows;

        if (name.length() > 0) {
            count = workflowService.countLikeName(name);
            workflows = workflowService.selectLikeName(name, limit, new Long(skip));

        } else {
            count = workflowService.count();
            workflows = workflowService.select(limit, new Long(skip));
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("recordsTotal", limit);
        jsonObject.put("recordsFiltered", count);
        jsonObject.put("displayStart", skip);
        jsonObject.put("data", workflows);

        return jsonObject.toString();
    }

    @RequestMapping(value = "/workflow-new", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public ModelAndView newManagement(HttpSession session) {
        ModelAndView mav = new ModelAndView("/workflow/new");

        mav.addObject("shapesProvidedList", WorkflowShapes.shapesProvidedList);
        return mav;
    }

    @RequestMapping(value = "/workflow/create", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public ModelAndView create(HttpSession session, HttpServletResponse response,
                               @RequestParam(defaultValue = "") String name,
                               @RequestParam(defaultValue = "") String designer_xml) throws IOException {

        try {
            //같은 이름 검색
            Workflow existWorkflow = workflowService.selectByName(name);
            if (existWorkflow != null) {
                ModelAndView mav = new ModelAndView("/workflow/new");
                mav.addObject("duplicate", true);
                mav.addObject("shapesProvidedList", WorkflowShapes.shapesProvidedList);
                return mav;
            }

            workflowService.createWorkflow(name, designer_xml);

            //리스트 페이지 반환
            response.sendRedirect("/service-console/workflow");
            return null;

        } catch (Exception ex) {
            ModelAndView mav = new ModelAndView("/workflow/new");
            mav.addObject("failed", true);
            mav.addObject("shapesProvidedList", WorkflowShapes.shapesProvidedList);
            return mav;
        }
    }

    @RequestMapping(value = "/workflow-edit", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public ModelAndView edit(HttpSession session,
                             @RequestParam(defaultValue = "") String _id) throws IOException {

        try {
            Workflow workflow = workflowService.selectById(_id);
            if (workflow == null) {
                throw new ServiceException("Invalid workflow id");
            }

            ModelAndView mav = new ModelAndView("/workflow/edit");
            mav.addObject("workflow", workflow);
            mav.addObject("shapesProvidedList", WorkflowShapes.shapesProvidedList);
            return mav;
        } catch (Exception ex) {
            throw new ServiceException("Invalid workflow id");
        }
    }

    @RequestMapping(value = "/workflow/delete", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public ModelAndView delete(HttpSession session,HttpServletResponse response,
                               @RequestParam(defaultValue = "") String _id) throws IOException {

        try {
            Workflow workflow = workflowService.selectById(_id);
            if (workflow == null) {
                throw new ServiceException("Invalid workflow id");
            }

            workflowService.deleteById(_id);

            response.sendRedirect("/service-console/workflow");
            return null;

        } catch (Exception ex) {
            throw new ServiceException("Invalid workflow id");
        }
    }

    @RequestMapping(value = "/workflow/update", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public ModelAndView update(HttpSession session,HttpServletResponse response,
                               @RequestParam(defaultValue = "") String _id,
                               @RequestParam(defaultValue = "") String name,
                               @RequestParam(defaultValue = "") String designer_xml
    ) throws IOException {

        Workflow workflow = workflowService.selectById(_id);
        if (workflow == null) {
            throw new ServiceException("Invalid workflow id");
        }

        try {
            //같은 오더 검색
            Workflow existWorkflow = workflowService.selectByName(name);
            if (existWorkflow != null) {
                if (!existWorkflow.get_id().equals(_id)) {
                    ModelAndView mav = new ModelAndView("/workflow/edit");
                    mav.addObject("workflow", workflow);
                    mav.addObject("duplicate", true);
                    mav.addObject("shapesProvidedList", WorkflowShapes.shapesProvidedList);
                    return mav;
                }
            }

            workflowService.updateById(_id, name, designer_xml);
            response.sendRedirect("/service-console/workflow");
            return null;

        } catch (Exception ex) {
            ModelAndView mav = new ModelAndView("/workflow/edit");
            mav.addObject("workflow", workflow);
            mav.addObject("failed", true);
            mav.addObject("shapesProvidedList", WorkflowShapes.shapesProvidedList);
            return mav;
        }
    }
}
