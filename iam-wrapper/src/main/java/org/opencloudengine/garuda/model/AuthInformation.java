package org.opencloudengine.garuda.model;

import org.opencloudengine.garuda.common.exception.ServiceException;
import org.opencloudengine.garuda.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.Map;

/**
 * Created by uengine on 2016. 6. 16..
 */
public class AuthInformation implements Serializable {

    public static String LOCATION_HEADER = "header";

    public static String LOCATION_PARAM = "param";

    public static String TOKEN_TYPE_JWT = "jwt";
    public static String TOKEN_TYPE_BEARER = "bearer";

    private String key;
    private String location;
    private String tokenType;
    private String token;

    private OauthUser oauthUser;
    private OauthClient oauthClient;
    private Long iat;
    private Long exp;
    private String scopes;
    private String type;
    private String refreshToken;
    private Map claim;

    private String error;
    private String error_description;

    public AuthInformation() {
    }

    public AuthInformation(HttpServletRequest request, String key, String location, String tokenType) {
        this.init(request, key, location, tokenType);
    }

    public void init(HttpServletRequest request, String key, String location, String tokenType) {
        this.key = key;
        this.location = location;
        this.tokenType = tokenType;

        if (location.equals(LOCATION_HEADER)) {
            this.token = request.getHeader(key);
        }
        if (location.equals(LOCATION_PARAM)) {
            this.token = request.getParameter(key);
        }
        if (StringUtils.isEmpty(tokenType)) {
            if (!StringUtils.isEmpty(token)) {
                boolean matches = token.matches("[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}");
                if (matches) {
                    this.tokenType = TOKEN_TYPE_BEARER;
                } else {
                    this.tokenType = TOKEN_TYPE_JWT;
                }
            }
        }
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public OauthUser getOauthUser() {
        return oauthUser;
    }

    public void setOauthUser(OauthUser oauthUser) {
        this.oauthUser = oauthUser;
    }

    public OauthClient getOauthClient() {
        return oauthClient;
    }

    public void setOauthClient(OauthClient oauthClient) {
        this.oauthClient = oauthClient;
    }

    public Long getIat() {
        return iat;
    }

    public void setIat(Long iat) {
        this.iat = iat;
    }

    public Long getExp() {
        return exp;
    }

    public void setExp(Long exp) {
        this.exp = exp;
    }

    public String getScopes() {
        return scopes;
    }

    public void setScopes(String scopes) {
        this.scopes = scopes;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public Map getClaim() {
        return claim;
    }

    public void setClaim(Map claim) {
        this.claim = claim;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getError_description() {
        return error_description;
    }

    public void setError_description(String error_description) {
        this.error_description = error_description;
    }
}
