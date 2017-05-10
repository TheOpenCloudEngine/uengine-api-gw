package org.opencloudengine.garuda.web.analysis;


import net.minidev.json.JSONObject;
import org.opencloudengine.garuda.common.exception.ServiceException;
import org.opencloudengine.garuda.history.TaskHistory;
import org.opencloudengine.garuda.history.TaskHistoryRepository;
import org.opencloudengine.garuda.history.TransactionHistory;
import org.opencloudengine.garuda.history.TransactionHistoryRepository;
import org.opencloudengine.garuda.util.JsonUtils;
import org.opencloudengine.garuda.web.policy.Policy;
import org.opencloudengine.garuda.web.policy.PolicyService;
import org.opencloudengine.garuda.web.uris.ResourceUri;
import org.opencloudengine.garuda.web.uris.ResourceUriService;
import org.opencloudengine.garuda.web.workflow.Workflow;
import org.opencloudengine.garuda.web.workflow.WorkflowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

@Controller
@RequestMapping("/analysis")
public class AnalysisController {
    @Autowired
    @Qualifier("config")
    private Properties config;

    @Autowired
    private TransactionHistoryRepository transactionRepository;

    @Autowired
    private TaskHistoryRepository taskRepository;


    @RequestMapping(method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public String load() {
        return "/analysis/list";
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public
    @ResponseBody
    String list(HttpSession session,
                @RequestParam(value = "limit", required = false, defaultValue = "10") int limit,
                @RequestParam(value = "skip", required = false, defaultValue = "0") int skip,
                @RequestParam(value = "uri", required = false, defaultValue = "") String uri) {

        Long count;
        List<TransactionHistory> transactionHistories;

        if (uri.length() > 0) {
            count = transactionRepository.countLikeUri(uri);
            transactionHistories = transactionRepository.selectLikeUri(uri, limit, new Long(skip));

        } else {
            count = transactionRepository.count();
            transactionHistories = transactionRepository.select(limit, new Long(skip));
        }
        for (TransactionHistory transactionHistory : transactionHistories) {
            this.convertHumanReadable(transactionHistory);
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("recordsTotal", limit);
        jsonObject.put("recordsFiltered", count);
        jsonObject.put("displayStart", skip);
        jsonObject.put("data", transactionHistories);

        return jsonObject.toString();
    }

    @RequestMapping(value = "/info", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public ModelAndView info(HttpSession session, @RequestParam(defaultValue = "") String _id) {

        try {
            TransactionHistory transactionHistory = transactionRepository.selectById(_id);
            if (transactionHistory == null) {
                throw new ServiceException("Invalid transactionHistory id");
            }
            this.convertHumanReadable(transactionHistory);

            String identifier = transactionHistory.getIdentifier();
            List<TaskHistory> taskHistories = taskRepository.selectByIdentifier(identifier);

            for (TaskHistory taskHistory : taskHistories) {
                this.convertHumanReadable(taskHistory);
            }

            ModelAndView mav = new ModelAndView("/analysis/info");
            mav.addObject("transactionHistory", transactionHistory);
            mav.addObject("taskHistories", taskHistories);
            return mav;
        } catch (Exception ex) {
            throw new ServiceException("Invalid transactionHistory id");
        }
    }

    @RequestMapping(value = "/task/list", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public String list(HttpSession session,
                @RequestParam(value = "identifier", required = false, defaultValue = "") String identifier) throws IOException {

        List<TaskHistory> taskHistories = taskRepository.selectByIdentifier(identifier);
        for (TaskHistory taskHistory : taskHistories) {
            this.convertHumanReadable(taskHistory);
        }
        return JsonUtils.marshal(taskHistories);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public ModelAndView delete(HttpSession session, HttpServletResponse response,
                               @RequestParam(defaultValue = "") String _id) throws IOException {

        try {
            TransactionHistory transactionHistory = transactionRepository.selectById(_id);
            if (transactionHistory == null) {
                throw new ServiceException("Invalid transactionHistory id");
            }
            String identifier = transactionHistory.getIdentifier();
            transactionRepository.deleteById(_id);
            taskRepository.deleteByIdentifier(identifier);

            response.sendRedirect("/service-console/analysis");
            return null;

        } catch (Exception ex) {
            throw new ServiceException("Invalid transactionHistory id");
        }
    }

    private void convertHumanReadable(TransactionHistory transactionHistory) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        Long startDate = transactionHistory.getStartDate();
        Long endDate = transactionHistory.getEndDate();
        Long duration = transactionHistory.getDuration();
        if (startDate != null) {
            transactionHistory.setHumanStartDate(sdf.format(new Date(startDate)));
        } else {
            transactionHistory.setHumanStartDate("");
        }
        if (endDate != null) {
            transactionHistory.setHumanEndDate(sdf.format(new Date(endDate)));
        } else {
            transactionHistory.setHumanEndDate("");
        }
        if (duration != null) {
            long minutes = TimeUnit.MILLISECONDS.toMinutes(duration);
            long mis = transactionHistory.getDuration() - (minutes * 60 * 1000);
            double seconds = mis / 1000.0;
            transactionHistory.setHumanDuration(minutes + " min, " + seconds + " sec");
        } else {
            transactionHistory.setHumanDuration("");
        }
    }

    private void convertHumanReadable(TaskHistory taskHistory) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        Long startDate = taskHistory.getStartDate();
        Long endDate = taskHistory.getEndDate();
        Long duration = taskHistory.getDuration();
        if (startDate != null) {
            taskHistory.setHumanStartDate(sdf.format(new Date(startDate)));
        } else {
            taskHistory.setHumanStartDate("");
        }
        if (endDate != null) {
            taskHistory.setHumanEndDate(sdf.format(new Date(endDate)));
        } else {
            taskHistory.setHumanEndDate("");
        }
        if (duration != null) {
            long minutes = TimeUnit.MILLISECONDS.toMinutes(duration);
            long mis = taskHistory.getDuration() - (minutes * 60 * 1000);
            double seconds = mis / 1000.0;
            taskHistory.setHumanDuration(minutes + " min, " + seconds + " sec");
        } else {
            taskHistory.setHumanDuration("");
        }
    }
}
