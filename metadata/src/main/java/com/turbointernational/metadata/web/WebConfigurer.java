package com.turbointernational.metadata.web;

import java.util.concurrent.TimeUnit;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.MimeMappings;
import org.springframework.boot.context.embedded.ServletContextInitializer;
import org.springframework.context.annotation.Configuration;

/**
 * @author jrodriguez
 */
@Configuration
public class WebConfigurer implements ServletContextInitializer, EmbeddedServletContainerCustomizer {
    
    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
//        EnumSet<DispatcherType> disps = EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD, DispatcherType.ASYNC);
        
//        servletContext.addFilter("CORSFilter", new CORSFilter());
//        FilterRegistration.Dynamic staticResourcesProductionFilter =
//                servletContext.addFilter("staticResourcesProductionFilter",
//                        new StaticResourcesProductionFilter());

//        staticResourcesProductionFilter.addMappingForUrlPatterns(disps, true, "/");
//        staticResourcesProductionFilter.addMappingForUrlPatterns(disps, true, "/index.html");
//        staticResourcesProductionFilter.addMappingForUrlPatterns(disps, true, "/assets/*");
//        staticResourcesProductionFilter.addMappingForUrlPatterns(disps, true, "/scripts/*");
//        staticResourcesProductionFilter.setAsyncSupported(true);
    }

    /**
     * Set up Mime types.
     */
    @Override
    public void customize(ConfigurableEmbeddedServletContainer container) {
        container.setSessionTimeout(24, TimeUnit.HOURS);
        
        MimeMappings mappings = new MimeMappings(MimeMappings.DEFAULT);
        // IE issue, see https://github.com/jhipster/generator-jhipster/pull/711
        mappings.add("html", "text/html;charset=utf-8");
        // CloudFoundry issue, see https://github.com/cloudfoundry/gorouter/issues/64
        mappings.add("json", "text/html;charset=utf-8");
        container.setMimeMappings(mappings);
    }
}