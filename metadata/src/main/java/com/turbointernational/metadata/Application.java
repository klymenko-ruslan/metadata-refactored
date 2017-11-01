package com.turbointernational.metadata;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewFilter;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate4.Hibernate4Module;
import com.turbointernational.metadata.service.GraphDbService.GetInterchangeResponse;
import com.turbointernational.metadata.web.dto.Ancestor;
import com.turbointernational.metadata.web.dto.Interchange;
import com.turbointernational.metadata.web.dto.Manufacturer;
import com.turbointernational.metadata.web.dto.Part;
import com.turbointernational.metadata.web.dto.PartType;
import com.turbointernational.metadata.web.filter.CORSFilter;

@SpringBootApplication
@EnableTransactionManagement
@EnableScheduling
@EnableAsync
@EnableSpringDataWebSupport
public class Application extends WebMvcConfigurerAdapter implements WebApplicationInitializer, ApplicationContextAware {

    /**
     * Skip any disk I/O operations.
     *
     * This property can be used in integration tests to avoid (mock)
     * unnecessary disk I/O operations.
     */
    public final static String TEST_SKIPFILEIO = "com.turbointernational.metadata.skipfileio";

    private static ApplicationContext springContext = null;

    public static ApplicationContext getContext() {
        return springContext;
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Autowired
    ObjectMapper objectMapper;

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        servletContext.addFilter("corsFilter", CORSFilter.class);
        servletContext.addFilter("openEntityManagerInViewFilter", OpenEntityManagerInViewFilter.class);
    }

    @Bean
    public Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder() {
        return new Jackson2ObjectMapperBuilder().failOnUnknownProperties(false)
                .serializationInclusion(JsonInclude.Include.NON_EMPTY).modulesToInstall(new Hibernate4Module() {
                    {
                        enable(Feature.FORCE_LAZY_LOADING);
                    }
                });
    }

    @Bean(name = "asyncExecutor")
    protected ThreadPoolTaskExecutor mvcTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setThreadNamePrefix("async-");
        executor.setMaxPoolSize(10);
        return executor;
    }

    @Bean
    public JavaMailSender mail(@Value("${email.host}") String host, @Value("${email.user}") String user,
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

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Application.springContext = applicationContext;
    }

    @Bean
    public ModelMapper dtoMapper() {
        ModelMapper modelMapper = new ModelMapper();
        // DTO: PartType
        modelMapper.createTypeMap(com.turbointernational.metadata.entity.PartType.class, PartType.class)
            .addMapping(com.turbointernational.metadata.entity.PartType::getId, PartType::setId)
            .addMapping(com.turbointernational.metadata.entity.PartType::getName, PartType::setName);
        // DTO: Manufacturer
        modelMapper.createTypeMap(com.turbointernational.metadata.entity.Manufacturer.class, Manufacturer.class)
            .addMapping(com.turbointernational.metadata.entity.Manufacturer::getId, Manufacturer::setId)
            .addMapping(com.turbointernational.metadata.entity.Manufacturer::getName, Manufacturer::setName);
        // DTO: Part
        modelMapper.createTypeMap(com.turbointernational.metadata.entity.part.Part.class, Part.class)
            .addMapping(com.turbointernational.metadata.entity.part.Part::getId, Part::setPartId)
            .addMapping(com.turbointernational.metadata.entity.part.Part::getName, Part::setName)
            .addMapping(com.turbointernational.metadata.entity.part.Part::getDescription, Part::setDescription)
            .addMapping(com.turbointernational.metadata.entity.part.Part::getManufacturerPartNumber, Part::setPartNumber)
            .addMapping(com.turbointernational.metadata.entity.part.Part::getPartType, Part::setPartType)
            .addMapping(com.turbointernational.metadata.entity.part.Part::getManufacturer, Part::setManufacturer);
        // DTO: Ancestor
        modelMapper.createTypeMap(com.turbointernational.metadata.entity.part.Part.class, Ancestor.class)
            .addMapping(com.turbointernational.metadata.entity.part.Part::getId, Ancestor::setPartId)
            .addMapping(com.turbointernational.metadata.entity.part.Part::getName, Ancestor::setName)
            .addMapping(com.turbointernational.metadata.entity.part.Part::getDescription, Ancestor::setDescription)
            .addMapping(com.turbointernational.metadata.entity.part.Part::getManufacturerPartNumber, Ancestor::setPartNumber)
            .addMapping(com.turbointernational.metadata.entity.part.Part::getManufacturer, Ancestor::setManufacturer)
            .addMapping(com.turbointernational.metadata.entity.part.Part::getPartType, Ancestor::setPartType);
        // DTO: Interchange
        Converter<Long, com.turbointernational.metadata.entity.part.Part> partId2Part;
        modelMapper.createTypeMap(GetInterchangeResponse.class, Interchange.class)
            .addMapping(GetInterchangeResponse::getHeaderId, Interchange::setId);
            //.addMapping(GetInterchangeResponse::getParts, destinationSetter);
        return modelMapper;
    }

}
