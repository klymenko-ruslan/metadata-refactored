package com.turbointernational.metadata;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.turbointernational.metadata.service.CriticalDimensionService;

/**
 * @author dmytro.trunykov@zorallabs.com
 */
@Component
public class ApplicationListenerHandler implements ApplicationListener<ApplicationReadyEvent> {

    private static final Logger log = LoggerFactory.getLogger(ApplicationListenerHandler.class);

    @Autowired
    private CriticalDimensionService criticalDimensionService;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        criticalDimensionService.buildCriticalDimensionsCache();
    }

}
