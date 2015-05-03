package com.turbointernational.metadata.web;

import com.turbointernational.metadata.util.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

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
        
//        http.httpBasic().disable();
        
        http.authorizeRequests()
            .anyRequest().permitAll()
//            .antMatchers("/admin/**").authenticated()
//            .antMatchers("/admin/**").hasRole("ADMIN")
//            .antMatchers("/metadata/security**").permitAll()
//            .antMatchers("/", "/signup", "/about").permitAll()
            .and().formLogin()
                    .loginPage("/metadata/security/login/required")
                    .loginProcessingUrl("/metadata/security/login")
                    .failureUrl("/metadata/security/denied")
            .and().rememberMe();
//            .and().permitAll();
        
//        http.csrf().disable()
//            .authorizeRequests().antMatchers("/", "/index.html", "/bootstrap/**", "/jquery/**").permitAll()
//            .anyRequest().authenticated().and().formLogin()
//            .loginPage("/login.html").passwordParameter("password").usernameParameter("username")
//            .permitAll()                    
//            .and().logout()
//            .permitAll();
        
    }
    
    
//    <security:http use-expressions="true" access-denied-page="/metadata/security/denied">
//        <security:anonymous enabled="false"/>
//        <security:intercept-url pattern="/metadata/**"/>
//        <security:intercept-url pattern="hasIpAddress('127.0.0.1/32')"/>
//        <security:remember-me/>
//        <security:form-login login-processing-url="/metadata/security/login"
//                             login-page="/metadata/security/login/required"
//                             default-target-url="/metadata/security/login/success"
//                             authentication-failure-url="/metadata/security/login/failed"
//                             always-use-default-target="true"/>
//        
//        <security:logout logout-url="/metadata/security/logout" logout-success-url="/#/login"/>
//    </security:http>

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }
    
}
