package com.turbointernational.metadata.web;

import java.net.URI;
import java.net.URISyntaxException;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestMvcConfiguration;

/**
 *
 * @author jrodriguez
 */
@Configuration
@Import(RepositoryRestMvcConfiguration.class)
public class RestDataConfig  extends RepositoryRestMvcConfiguration {

  @Override
  protected void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
    super.configureRepositoryRestConfiguration(config);
    try {
      config.setBaseUri(new URI("/metadata/resource"));
    } catch (URISyntaxException e) {
      e.printStackTrace();
    }
  }
}