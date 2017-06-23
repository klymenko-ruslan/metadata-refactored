package com.turbointernational.metadata;

import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author dmytro.trunykov@zorallabs.com
 */
@Configuration
public class RabbitConfiguration {

    @Value("${rabbitmq.host}")
    private String host;

    @Value("${rabbitmq.port}")
    private int port;

    @Bean
    public ConnectionFactory rabbitConnectionFactory() {
        return new CachingConnectionFactory(host, port);
    }

    @Bean
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate retVal = new RabbitTemplate(rabbitConnectionFactory());
        return retVal;
    }

}
