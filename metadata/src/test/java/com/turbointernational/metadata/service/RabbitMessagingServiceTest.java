package com.turbointernational.metadata.service;

import static org.junit.Assert.fail;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.test.util.ReflectionTestUtils;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * @author dmytro.trunykov@zorallabs.com
 */
@RunWith(MockitoJUnitRunner.class)
public class RabbitMessagingServiceTest {

    private final static String MQ_BOM_CHANGES = "mq.timms.metadata.test.bom.changes";
    private final static String MQ_INTERCHANGE_CHANGES = "mq.timms.metadata.test.interchange.changes";

    @Mock
    private RabbitTemplate rbbtTmplMetadataBomChanges;

    @Mock
    private RabbitTemplate rbbtTmplMetadataInterchangeChanges;

    @InjectMocks
    private RabbitMessagingService service;

    @Before
    public void setUp() {
        ReflectionTestUtils.setField(service, "nameMqBomChanged", MQ_BOM_CHANGES);
        ReflectionTestUtils.setField(service, "nameMqInterchangeChanged", MQ_INTERCHANGE_CHANGES);
    }

    /**
     * Test method for
     * {@link com.turbointernational.metadata.service.RabbitMessagingService#bomChanged(java.lang.String, byte[])}.
     * 
     * @throws IOException
     */
    @Test
    public void testBomChanged() throws IOException {
        byte[] body = "{}".getBytes();
        service.bomChanged(null, body);
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setContentType(MessageProperties.CONTENT_TYPE_JSON);
        messageProperties.setContentEncoding("UTF-8");
        messageProperties.setContentLength(body.length);
        Message expectedMessage = new Message(body, messageProperties);
        verify(rbbtTmplMetadataBomChanges).send(eq(MQ_BOM_CHANGES), eq(expectedMessage));
    }

    /**
     * Test method for
     * {@link com.turbointernational.metadata.service.RabbitMessagingService#interchangeChanged(java.lang.String, byte[])}.
     */
    @Test
    public void testInterchangeChanged() {
        fail("Not yet implemented");
    }

}
