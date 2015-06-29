package com.turbointernational.metadata;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate4.Hibernate4Module;
import com.turbointernational.metadata.web.CORSFilter;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@SpringBootApplication
@EnableScheduling
@EnableAsync
@EnableTransactionManagement//(mode = AdviceMode.PROXY, proxyTargetClass = true)
@EnableJpaRepositories(basePackageClasses = Application.class)
@EnableSpringDataWebSupport
@EntityScan(basePackageClasses = Application.class)
@Configuration
public class Application extends WebMvcConfigurerAdapter implements WebApplicationInitializer {
    
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
    
    @Autowired(required=true)
    ObjectMapper objectMapper;
    
    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        FilterRegistration.Dynamic corsFilter = servletContext.addFilter("corsFilter", CORSFilter.class);
    }
    
    @Bean
    public Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder() {
        return new Jackson2ObjectMapperBuilder()
                .serializationInclusion(JsonInclude.Include.NON_EMPTY)
                .modulesToInstall(new Hibernate4Module());
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

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(new MappingJackson2HttpMessageConverter(objectMapper));
    }
    
    @Bean
    public EmbeddedServletContainerFactory servletContainer() {
        TomcatEmbeddedServletContainerFactory factory = new TomcatEmbeddedServletContainerFactory();
        factory.setSessionTimeout(24, TimeUnit.HOURS);
        return factory;
    }
    
}
