package org.opencloudengine.garuda.web.security;

import org.springframework.security.web.util.matcher.RegexRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.regex.Pattern;

/**
 * Created by uengine on 2015. 6. 18..
 */
public class CsrfSecurityRequestMatcher implements RequestMatcher {
    private Pattern allowedMethods = Pattern.compile("^(GET|HEAD|TRACE|OPTIONS)$");

    private RegexRequestMatcher securityMatcher = new RegexRequestMatcher("/service-console/j_spring_security_check", null);

    @Override
    public boolean matches(HttpServletRequest request) {

        if(allowedMethods.matcher(request.getMethod()).matches()){
            return false;
        }

        if(securityMatcher.matches(request)){
            return false;
        }

        return true;
    }
}
