package com.turbointernational.metadata.service;

import java.io.IOException;
import java.nio.charset.Charset;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

/**
 * @author dmytro.trunykov@zorallabs.com
 */
@Service
@Profile("integration")
public class MessagingServiceMockImpl implements MessagingService {

  private Logger log = LoggerFactory.getLogger(MessagingServiceMockImpl.class);

  @Override
  public void bomChanged(byte[] message) throws IOException {
    log.debug("Send notification about changes in BOMs: {}", new String(message, Charset.forName("UTF-8")));
  }

  @Override
  public void interchangeChanged(byte[] message) throws IOException {
    log.debug("Send notification about changes in Interchanges: {}", new String(message, Charset.forName("UTF-8")));
  }

}
