package org.opencloudengine.garuda.authentication;

import org.opencloudengine.garuda.model.AuthInformation;

import javax.servlet.http.HttpServletRequest;

public interface AuthenticationService {

    AuthInformation validateRequest(HttpServletRequest request, String key, String location, String tokenType);

}
