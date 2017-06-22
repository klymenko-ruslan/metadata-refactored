package com.turbointernational.metadata.service;

import java.io.IOException;

/**
 * @author dmytro.trunykov@zorallabs.com
 */
public interface MessagingService {

    void bomChanged(byte[] message) throws IOException;

    void interchangeChanged(byte[] message) throws IOException;

}
