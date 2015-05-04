package com.turbointernational.metadata.web;

import com.turbointernational.metadata.util.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 *
 * @author jrodriguez
 */
@Configuration
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
        web.ignoring().antMatchers("/resources/**"); // #3
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.sessionManagement()
            .maximumSessions(1)
            .expiredUrl("/login?expired")
            .maxSessionsPreventsLogin(true)
            .and()
            .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
            .invalidSessionUrl("/");
        
        http.authorizeRequests()
            .antMatchers("/metadata/security/login",
                         "/metadata/security/login/**").permitAll()    // Login endpoints open to anyone
            .antMatchers("/metadata/security/password**").permitAll()  // Password reset endpoints open to anyone
            .antMatchers("/metadata/**").authenticated()               // API requires authentication
            .antMatchers("/product_images/**").authenticated()         // Images require authentication
            .antMatchers("/magmi/**").hasIpAddress("127.0.0.1/32")     // Allow magmi from localhost without user.
            .and().formLogin()
                    .loginPage("/metadata/security/login/required")
                    .loginProcessingUrl("/metadata/security/login")
                     .defaultSuccessUrl("/metadata/security/login/success")
                    .failureUrl("/metadata/security/login/denied")
            .and().rememberMe()
            .and().logout().logoutUrl("/metadata/security/logout")
            .and().exceptionHandling().accessDeniedPage("/metadata/security/login/forbidden")
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
    
}
