package com.turbointernational.metadata;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Bean;
import org.springframework.instrument.classloading.LoadTimeWeaver;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@SpringBootApplication
@EnableScheduling
@EnableAsync(mode = AdviceMode.PROXY)
@EnableTransactionManagement(mode = AdviceMode.PROXY)
public class Application extends WebMvcConfigurerAdapter {

    @Value("${images.resized}")
    String productImages;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
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
    protected LoadTimeWeaver loadTimeWeaver() {
        return new org.springframework.instrument.classloading.InstrumentationLoadTimeWeaver();
    }
    

    /**
     * Product Image Support
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/product_images/**")
                .addResourceLocations(productImages);
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

    @Bean
    public EmbeddedServletContainerFactory servletContainer() {
        TomcatEmbeddedServletContainerFactory factory = new TomcatEmbeddedServletContainerFactory();
        
        return factory;
    }

}
