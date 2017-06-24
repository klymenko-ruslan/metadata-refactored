package com.turbointernational.metadata.service;

import java.io.IOException;
import java.nio.charset.Charset;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("!integration")
public class RabbitMessagingService implements MessagingService {

    private final static Logger log = LoggerFactory.getLogger(RabbitMessagingService.class);

    @Value("${rabbitmq.queue.bom.changed}")
    private String nameMqBomChanged;

    @Value("${rabbitmq.queue.interchange.changed}")
    private String nameMqInterchangeChanged;
 
    @Autowired
    private RabbitTemplate rbbtTmplMetadataBomChanges;

    @Autowired
    private RabbitTemplate rbbtTmplMetadataInterchangeChanges;

    @Override
    public void bomChanged(byte[] message) throws IOException {
        log.debug("Send notification about changes in BOMs: {}", new String(message, Charset.forName("UTF-8")));
        sendNotification(rbbtTmplMetadataBomChanges, nameMqBomChanged, message);
    }

    @Override
    public void interchangeChanged(byte[] message) throws IOException {
        log.debug("Send notification about changes in Interchanges: {}", new String(message, Charset.forName("UTF-8")));
        sendNotification(rbbtTmplMetadataInterchangeChanges, nameMqInterchangeChanged, message);
    }

    private void sendNotification(RabbitTemplate rabbitTemplate, String queueName, byte[] message) {
        rabbitTemplate.convertAndSend(queueName, message);
    }

}
