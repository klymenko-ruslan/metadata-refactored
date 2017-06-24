package com.turbointernational.metadata;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
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

    @Value("${rabbitmq.exchange.metadata}")
    private String exchangeNameMetadata;

    @Value("rabbitmq.queue.bom.changed")
    private String nameMqBomChanged;
    

    @Value("rabbitmq.queue.durability.bom.changed")
    private boolean mqBomChangedDurability;
    

    @Value("rabbitmq.queue.durability.interchange.changed")
    private boolean mqInterchangeChangedDurability;

    @Value("rabbitmq.queue.interchange.changed")
    private String nameMqInterchangeChanged;

    @Bean
    ConnectionFactory rabbitConnectionFactory() {
        return new CachingConnectionFactory(host, port);
    }

    @Bean
    RabbitTemplate rabbitTemplate() {
        RabbitTemplate retVal = new RabbitTemplate(rabbitConnectionFactory());
        return retVal;
    }
    @Bean
    DirectExchange metadataExchange() {
        return new DirectExchange(exchangeNameMetadata);
    }

    @Bean
    Queue bomChangesQueue() {
        return new Queue(nameMqBomChanged, mqBomChangedDurability);
    }

    @Bean
    Queue interchangeChangesQueue() {
        return new Queue(nameMqInterchangeChanged, mqInterchangeChangedDurability);
    }

    @Bean
    Binding bindingBoms(Queue bomChangesQueue, DirectExchange metadataExchange) {
        return BindingBuilder.bind(bomChangesQueue).to(metadataExchange).with(nameMqBomChanged);
    }
 
    @Bean
    Binding bindingInterchanges(Queue interchangeChangesQueue, DirectExchange metadataExchange) {
        return BindingBuilder.bind(interchangeChangesQueue).to(metadataExchange).with(nameMqInterchangeChanged);
    }
   
    @Bean("rbbtTmplMetadataBomChanges")
    RabbitTemplate rbbtTmplMetadataBomChanges() {
        RabbitTemplate retVal = new RabbitTemplate(rabbitConnectionFactory());
        retVal.setExchange(exchangeNameMetadata);
        return retVal;
    }
   
    @Bean("rbbtTmplMetadataInterchangeChanges")
    RabbitTemplate rbbtTmplMetadataInterchangeChanges() {
        RabbitTemplate retVal = new RabbitTemplate(rabbitConnectionFactory());
        retVal.setExchange(exchangeNameMetadata);
        return retVal;
    }

}
