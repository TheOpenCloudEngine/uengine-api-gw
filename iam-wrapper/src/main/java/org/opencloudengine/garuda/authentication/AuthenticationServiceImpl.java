package org.opencloudengine.garuda.authentication;

import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.opencloudengine.garuda.handler.GateHandlerService;
import org.opencloudengine.garuda.model.AuthInformation;
import org.opencloudengine.garuda.model.OauthClient;
import org.opencloudengine.garuda.model.OauthUser;
import org.opencloudengine.garuda.proxy.ProxyService;
import org.opencloudengine.garuda.web.configuration.ConfigurationHelper;
import org.opencloudengine.garuda.web.iam.IamService;
import org.opencloudengine.garuda.web.uris.ResourceUriRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.ConcurrentMap;

@Service
public class AuthenticationServiceImpl implements AuthenticationService, InitializingBean {
    @Autowired
    @Qualifier("config")
    private Properties config;

    @Autowired
    ConfigurationHelper configurationHelper;

    @Autowired
    ProxyService proxyService;

    @Autowired
    GateHandlerService handlerService;

    @Autowired
    ResourceUriRepository uriRepository;

    @Autowired
    IamService iamService;

    ConcurrentMap AuthMap;

    /**
     * SLF4J Logging
     */
    private Logger logger = LoggerFactory.getLogger(AuthenticationServiceImpl.class);

    @Override
    public void afterPropertiesSet() throws Exception {
        DB db = DBMaker.memoryDB().make();
        AuthMap = db.hashMap("AuthMap").make();
    }

    /**
     * 밸리데이팅에 실패하였을 경우 null return
     *
     * @param request   HttpServletRequest
     * @param key       토큰이 들어간 헤더또는 파라미터 키
     * @param location  토큰의 위치 (헤더,파라미터)
     * @param tokenType 토큰 타입(optional)
     * @return AuthInformation
     */
    @Override
    public AuthInformation validateRequest(HttpServletRequest request, String key, String location, String tokenType) {

        AuthInformation authInformation = new AuthInformation();
        try {
            authInformation.init(request, key, location, tokenType);

            String token = authInformation.getToken();
            if (token == null) {
                authInformation.setError("Token not found");
                authInformation.setError_description("Invalid Authentication Request");
                return authInformation;
            }

            boolean hasChash = false;
            Object o = AuthMap.get(token);
            if (o != null) {
                authInformation = (AuthInformation) o;
                Long exp = authInformation.getExp();
                long currentTime = new Date().getTime();
                if (currentTime > exp) {
                    AuthMap.remove(token);
                } else {
                    hasChash = true;
                }
            }

            if (hasChash) {
                return authInformation;
            }

            Map tokenInfo = iamService.tokenInfo(token);
            if (tokenInfo.containsKey("error")) {
                authInformation.setError(tokenInfo.get("error").toString());
                authInformation.setError_description(tokenInfo.get("error_description").toString());
                return authInformation;
            }

            OauthUser oauthUser = null;
            OauthClient oauthClient = null;
            Long iat = null;
            Long exp = null;
            String scopes = null;
            String type = null;
            String refreshToken = null;
            Map claim = null;

            if (authInformation.getTokenType().equals(AuthInformation.TOKEN_TYPE_JWT)) {
                if (!tokenInfo.containsKey("context")) {
                    authInformation.setError("Invalid Authentication Request");
                    authInformation.setError_description("Invalid Authentication Request");
                    return authInformation;
                }


                iat = (Long) tokenInfo.get("iat");
                exp = (Long) tokenInfo.get("exp");

                Map context = (Map) tokenInfo.get("context");

                oauthClient = iamService.getClient(context.get("clientId").toString());
                scopes = context.get("scopes").toString();
                type = context.get("type").toString();

                if (context.containsKey("userId")) {
                    String userId = context.get("userId").toString();
                    oauthUser = iamService.getUser(userId);
                }

                if (context.containsKey("refreshToken")) {
                    refreshToken = context.get("refreshToken").toString();
                }

                if (context.containsKey("claim")) {
                    claim = (Map) context.get("claim");
                }
            }

            if (authInformation.getTokenType().equals(AuthInformation.TOKEN_TYPE_BEARER)) {
                if (!tokenInfo.containsKey("expires_in")) {
                    authInformation.setError("Invalid Authentication Request");
                    authInformation.setError_description("Invalid Authentication Request");
                    return authInformation;
                }

                //iat 는 구할 수 없으므로 exp 만 구하도록 한다.
                long currentTime = new Date().getTime();
                Long expires_in = (Long) tokenInfo.get("expires_in");
                Date expDate = new Date(currentTime + expires_in * 1000);
                exp = expDate.getTime();

                oauthClient = iamService.getClient(tokenInfo.get("clientId").toString());
                scopes = tokenInfo.get("scope").toString();
                type = tokenInfo.get("type").toString();

                if (tokenInfo.containsKey("userId")) {
                    String userId = tokenInfo.get("userId").toString();
                    oauthUser = iamService.getUser(userId);
                }

                if (tokenInfo.containsKey("refreshToken")) {
                    refreshToken = tokenInfo.get("refreshToken").toString();
                }
            }

            authInformation.setOauthUser(oauthUser);
            authInformation.setOauthClient(oauthClient);
            authInformation.setIat(iat);
            authInformation.setExp(exp);
            authInformation.setScopes(scopes);
            authInformation.setType(type);
            authInformation.setRefreshToken(refreshToken);
            authInformation.setClaim(claim);

            //메모리디비에 저장한다.
            AuthMap.put(token, authInformation);

            return authInformation;
        } catch (Exception ex) {
            authInformation.setError("Invalid Authentication Request");
            authInformation.setError_description("Invalid Authentication Request");
            return authInformation;
        }
    }

}
