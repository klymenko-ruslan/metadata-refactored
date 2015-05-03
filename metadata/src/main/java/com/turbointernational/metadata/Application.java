package com.turbointernational.metadata;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@SpringBootApplication
@EnableScheduling
@EnableAsync(mode = AdviceMode.ASPECTJ)
public class Application extends WebMvcConfigurerAdapter {
    
    @Value("${images.resized}")
    String productImages;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
    
    @Bean
    protected Filter corsFilter() {
        return new GenericFilterBean() {
            @Override
            public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
                HttpServletRequest httpRequest = (HttpServletRequest) request;
                HttpServletResponse httpResponse = (HttpServletResponse) response;

                httpResponse.addHeader(
                        "Access-Control-Allow-Methods",
                        "GET, POST, PUT, DELETE, OPTIONS, HEAD");
                
                httpResponse.addHeader(
                        "Access-Control-Allow-Headers",
                        httpRequest.getHeader("Access-Control-Request-Headers"));
                
                httpResponse.addHeader(
                        "Access-Control-Allow-Origin",
                        StringUtils.defaultIfBlank(httpRequest.getHeader("Origin"), "*"));
                
                httpResponse.addHeader(
                        "Access-Control-Allow-Credentials",
                        "true");

                chain.doFilter(request, response);
            }
        };
    }
    
    @Bean(name = "asyncExecutor")
    protected ThreadPoolTaskExecutor mvcTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setThreadNamePrefix("async-");
        executor.setMaxPoolSize(10);
        return executor;
    }
    
    @Bean(name = "bomRebuildExecutor")
    protected ThreadPoolTaskExecutor bomRebuildExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setThreadNamePrefix("bom-rebuild-");
        executor.setMaxPoolSize(1);
        return executor;
    }
    

    /**
     * Product Image Support
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/product_images/**")
                .addResourceLocations(productImages);
        
        
//        <url-pattern>/part/*</url-pattern>
//        <url-pattern>/security/*</url-pattern>
//        <url-pattern>/password/*</url-pattern>
    }

    @Bean
    public EmbeddedServletContainerFactory servletContainer() {
        TomcatEmbeddedServletContainerFactory factory = new TomcatEmbeddedServletContainerFactory();
        return factory;
    }
    
    @Bean
    public JavaMailSender mail(
            @Value("${email.host}") String host,
            @Value("${email.user}") String user,
            @Value("${email.pass}") String pass) {
        JavaMailSenderImpl mail = new JavaMailSenderImpl();
        
        mail.setHost(host);
        mail.setUsername(user);
        mail.setPassword(pass);
        
        return mail;
    }
}
