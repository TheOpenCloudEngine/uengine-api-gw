package org.opencloudengine.garuda.gateway;

import org.opencloudengine.garuda.model.GateResponse;
import org.opencloudengine.garuda.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by uengine on 2016. 6. 16..
 */
public class GateException {

    public static String POLICY_NOT_FOUND = "policy_not_found";
    public static String WORKFLOW_NOT_FOUND = "workflow_not_found";
    public static String NO_MAPPING_URI = "no_mapping_uri";
    public static String CLASS_NOT_FOUND = "class_not_found";
    public static String WORKFLOW_NOT_SUPPORT = "workflow_not_support";
    public static String SERVER_ERROR = "server_error";
    public static String AUTHENTICATION_FAIL = "authentication_fail";
    public static String BEFORE_USE_SCRIPT = "before_use_script_error";
    public static String AFTER_USE_SCRIPT = "after_use_script_error";
    public static String WORKFLOW_FAILED = "workflow_failed";
    public static String PROXY_FAILED = "proxy_failed";

    public GateResponse getResponse(String code, HttpServletRequest request, String msg) {
        GateResponse response = new GateResponse();
        switch (code) {
            case "policy_not_found":
                response.setError(POLICY_NOT_FOUND);
                response.setError_description("policy not found while execute " + request.getPathInfo());
                break;

            case "workflow_not_found":
                response.setError(WORKFLOW_NOT_FOUND);
                response.setError_description("workflow not found while execute " + request.getPathInfo());
                break;

            case "no_mapping_uri":
                response.setError(NO_MAPPING_URI);
                response.setError_description("No mapping uri found for" + request.getPathInfo());
                break;

            case "class_not_found":
                response.setError(CLASS_NOT_FOUND);
                response.setError_description("class not found while execute " + request.getPathInfo());
                break;

            case "workflow_not_support":
                response.setError(WORKFLOW_NOT_SUPPORT);
                response.setError_description("Workflow is not supported yet : " + request.getPathInfo());
                break;

            case "server_error":
                response.setError(SERVER_ERROR);
                response.setError_description("unknown server error while execute " + request.getPathInfo());
                break;

            case "authentication_fail":
                response.setError(AUTHENTICATION_FAIL);
                response.setError_description("authentication failed : " + request.getPathInfo());
                break;

            case "before_use_script_error":
                response.setError(BEFORE_USE_SCRIPT);
                response.setError_description("before_use_script_error while execute " + request.getPathInfo());
                break;

            case "workflow_failed":
                response.setError(WORKFLOW_FAILED);
                response.setError_description("workflow_failed while execute " + request.getPathInfo());
                break;

            case "after_use_script_error":
                response.setError(AFTER_USE_SCRIPT);
                response.setError_description("after_use_script_error while execute " + request.getPathInfo());
                break;

            case "proxy_failed":
                response.setError(PROXY_FAILED);
                response.setError_description("proxy_failed while execute " + request.getPathInfo());
                break;

            default:
                response.setError(SERVER_ERROR);
                response.setError_description("unknown server error while execute " + request.getPathInfo());
                break;
        }
        if (!StringUtils.isEmpty(msg)) {
            response.setError_description(msg);
        }
        return response;
    }
}
