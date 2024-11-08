package com.yilan.sba.server.config;

import de.codecentric.boot.admin.server.config.AdminServerProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

/**
 * @author yilan0916
 * @date 2024/11/8
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private String adminSecurityContextPath;

    public SecurityConfig(AdminServerProperties adminServerProperties) {
        this.adminSecurityContextPath = adminServerProperties.getContextPath();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        SavedRequestAwareAuthenticationSuccessHandler successHandler = new SavedRequestAwareAuthenticationSuccessHandler();
        successHandler.setTargetUrlParameter("redirectTo");
        http.authorizeRequests()
                .antMatchers(adminSecurityContextPath + "/assets/**").permitAll()
                .antMatchers(adminSecurityContextPath + "/login").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin().loginPage(adminSecurityContextPath + "/login").successHandler(successHandler)
                .and()
                .logout().logoutUrl(adminSecurityContextPath + "/logout")
                .and()
                .httpBasic()
                .and()
                .csrf()
                .ignoringAntMatchers(adminSecurityContextPath + "/instances", adminSecurityContextPath + "/actuator/**")
                .disable();
    }
}
