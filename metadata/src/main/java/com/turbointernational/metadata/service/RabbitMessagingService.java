package com.turbointernational.metadata.service;

import static org.springframework.amqp.core.MessageProperties.CONTENT_TYPE_JSON;

import java.io.IOException;
import java.nio.charset.Charset;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
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
    public void bomChanged(String groupId, byte[] message) throws IOException {
        if (log.isDebugEnabled()) {
            log.debug("Send notification about changes in BOMs: {}, {}", groupId,
                    new String(message, Charset.forName("UTF-8")));
        }
        sendNotification(rbbtTmplMetadataBomChanges, nameMqBomChanged, groupId, message);
    }

    @Override
    public void interchangeChanged(String groupId, byte[] message) throws IOException {
        if (log.isDebugEnabled()) {
            log.debug("Send notification about changes in Interchanges: {}, {}", groupId,
                    new String(message, Charset.forName("UTF-8")));
        }
        sendNotification(rbbtTmplMetadataInterchangeChanges, nameMqInterchangeChanged, groupId, message);
    }

    private void sendNotification(RabbitTemplate rabbitTemplate, String routingQueue, String groupId, byte[] body) {
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setContentType(CONTENT_TYPE_JSON);
        if (groupId != null) {
            messageProperties.setHeader("xxx-group-id", groupId);
        }
        Message message = new Message(body, messageProperties);
        rabbitTemplate.send(routingQueue, message);
    }

}
