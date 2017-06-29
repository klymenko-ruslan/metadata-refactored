package com.turbointernational.metadata.service;

import org.springframework.stereotype.Service;

/**
 * This is a shim for some static methods.
 *
 * It is useful to mock in unit tests.
 *
 * @author dmytro.trunykov@zorallabs.com
 */
@Service
public class AppService {

    /**
     * @return current time millis
     */
    public long now() {
        return System.currentTimeMillis();
    }

}
