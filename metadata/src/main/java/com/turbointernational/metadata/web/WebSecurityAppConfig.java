package com.turbointernational.metadata.web;

import com.turbointernational.metadata.util.LoginService;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

/**
 *
 * @author jrodriguez
 */
@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true)
public class WebSecurityAppConfig extends WebSecurityConfigurerAdapter {

    public WebSecurityAppConfig() {}
    
    @Autowired
    LoginService loginService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(loginService);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/resources/**");
        web.debug(false);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.sessionManagement()
            .maximumSessions(1)
            .maxSessionsPreventsLogin(true)
            .expiredUrl("/metadata/security/unauthorized?expiredSession")
            .and()
            .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
            .invalidSessionUrl("/metadata/security/unauthorized?invalidSession");
        
        http.authorizeRequests()
            .antMatchers("/metadata/security/password**").permitAll()  // Password reset endpoints open to anyone
            .antMatchers("/metadata/**").authenticated()               // API requires authentication
            .antMatchers("/product_images/**").authenticated()         // Images require authentication
            .antMatchers("/magmi/**").hasIpAddress("127.0.0.1/32")     // Allow magmi from localhost without user.
            .and().formLogin()
                .loginPage("/metadata/security/unauthorized")
                .loginProcessingUrl("/metadata/security/login")
                .defaultSuccessUrl("/metadata/security/user/me")
                .failureHandler(new AuthenticationFailureHandlerImpl())
            .and().rememberMe()
            .and().httpBasic().realmName("TI Metadata")
            .and().logout()
                .logoutUrl("/metadata/security/logout")
                .logoutSuccessHandler(new NoopLogoutSuccessHandler())
            .and().exceptionHandling().accessDeniedHandler(new AccessDeniedHandlerImpl())
            .and().authenticationProvider(createDaoAuthenticationProvider());
        
        http.csrf().disable();
        http.anonymous().disable();
//        http.httpBasic().disable();
    }

    @Bean
    public DaoAuthenticationProvider createDaoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(loginService);
        provider.setPasswordEncoder(new BCryptPasswordEncoder());
        return provider;
    }

    private static class AccessDeniedHandlerImpl implements AccessDeniedHandler {
        @Override
        public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
            response.sendError(HttpStatus.FORBIDDEN.value());
        }
    }
    
    private static class AuthenticationFailureHandlerImpl implements AuthenticationFailureHandler {
        @Override
        public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
            response.sendError(HttpStatus.UNAUTHORIZED.value());
        }
    }
    
    private static class NoopLogoutSuccessHandler implements LogoutSuccessHandler {

        @Override
        public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
            response.setStatus(HttpStatus.OK.value());
            response.flushBuffer();
        }
    }
}
