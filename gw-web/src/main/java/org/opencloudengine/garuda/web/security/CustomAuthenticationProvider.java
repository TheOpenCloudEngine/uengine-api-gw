package org.opencloudengine.garuda.web.security;

import org.opencloudengine.garuda.model.User;
import org.opencloudengine.garuda.util.ApplicationContextRegistry;
import org.opencloudengine.garuda.web.system.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Created by uengine on 2016. 4. 27..
 */
public class CustomAuthenticationProvider implements AuthenticationProvider {

    /**
     * SLF4J Logging
     */
    private Logger logger = LoggerFactory.getLogger(CustomAuthenticationProvider.class);

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String email = (String) authentication.getPrincipal();
        String userPassword = (String) authentication.getCredentials();

        ApplicationContext context = ApplicationContextRegistry.getApplicationContext();
        UserService userService = context.getBean(UserService.class);

        logger.info("Login try. {}", email + "/" + userPassword);

        User user = userService.selectUser();
        if(user == null){
            throw new BadCredentialsException("Bad credentials");
        }
        if(!user.getEmail().equals(email)){
            throw new BadCredentialsException("Bad credentials");
        }
        if(!user.getEnabled()){
            logger.info("Not enabled user. {}", email + "/" + userPassword);
            throw new BadCredentialsException("Bad credentials");
        }

        if (userPassword.equals(user.getPassword())) {
            logger.info("Login success. {}", email + "/" + userPassword);
            List<GrantedAuthority> roles = new ArrayList<GrantedAuthority>();
            roles.add(new SimpleGrantedAuthority(user.getAuthority()));

            UsernamePasswordAuthenticationToken result = new UsernamePasswordAuthenticationToken(email, userPassword, roles);
            result.setDetails(new CustomUserDetails(email, userPassword, user.getAuthority()));
            return result;
        } else {
            logger.info("Invalid Credentials.");
            throw new BadCredentialsException("Bad credentials");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
