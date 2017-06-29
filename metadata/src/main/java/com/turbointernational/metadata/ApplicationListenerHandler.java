package com.turbointernational.metadata;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.turbointernational.metadata.service.CriticalDimensionService;

/**
 * A listener that receives an event just after start of the webapp.
 *
 * This listener is useful to do some post initialization steps (e.g. initialization of various caches).
 *
 * @author dmytro.trunykov@zorallabs.com
 */
@Component
public class ApplicationListenerHandler implements ApplicationListener<ApplicationReadyEvent> {

    // private static final Logger log = LoggerFactory.getLogger(ApplicationListenerHandler.class);

    @Autowired
    private CriticalDimensionService criticalDimensionService;

    /**
     * Initialize a cache of critical dimensions descriptors.
     *
     * @see org.springframework.context.ApplicationListener#onApplicationEvent(org.springframework.context.ApplicationEvent)
     */
    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        criticalDimensionService.buildCriticalDimensionsCache();
    }

}
