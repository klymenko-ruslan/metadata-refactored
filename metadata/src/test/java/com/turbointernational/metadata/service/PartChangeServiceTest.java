package com.turbointernational.metadata.service;

import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class PartChangeServiceTest {

    @Mock
    private MessagingService messagingService;

    @Mock
    private AppService appService;

    @InjectMocks
    private PartChangeService service;

    @Before
    public void setUp() {
        service.init();
    }

    @Test
    public void testAddedBom() throws IOException {
        service.addedBom(123L, 456L);
        verify(messagingService).bomChanged(isNull(String.class),
                eq("{\"chtyp\":\"INS\",\"ppid\":123,\"chpid\":456}".getBytes("UTF-8")));
    }

    @Test
    public void testAddedBoms() throws IOException {
        String groupId = "123-1498733284429";
        when(appService.now()).thenReturn(1498733284429L);
        service.addedBoms(123L, Arrays.asList(456L, 734L));
        verify(messagingService).bomChanged(eq(groupId),
                eq("{\"chtyp\":\"INS\",\"ppid\":123,\"chpid\":456}".getBytes("UTF-8")));
        verify(messagingService).bomChanged(eq(groupId),
                eq("{\"chtyp\":\"INS\",\"ppid\":123,\"chpid\":734}".getBytes("UTF-8")));
    }

    @Test
    public void testAddedToParentBoms() throws IOException {
        String groupId = "123-1498733284429";
        when(appService.now()).thenReturn(1498733284429L);
        service.addedToParentBoms(123L, Arrays.asList(456L, 789L));
        verify(messagingService).bomChanged(eq(groupId),
                eq("{\"chtyp\":\"INS\",\"ppid\":456,\"chpid\":123}".getBytes("UTF-8")));
        verify(messagingService).bomChanged(eq(groupId),
                eq("{\"chtyp\":\"INS\",\"ppid\":789,\"chpid\":123}".getBytes("UTF-8")));
    }

    @Test
    public void testUpdatedBom() throws IOException {
        service.updatedBom(123L);
        verify(messagingService).bomChanged(isNull(String.class),
                eq("{\"chtyp\":\"UPD\",\"ppid\":123,\"chpid\":null}".getBytes("UTF-8")));
    }

    @Test
    public void testDeletedBom() throws IOException {
        service.deletedBom(123L, 456L);
        verify(messagingService).bomChanged(isNull(String.class),
                eq("{\"chtyp\":\"DEL\",\"ppid\":123,\"chpid\":456}".getBytes("UTF-8")));
    }

    @Test
    public void testChangedInterchange() throws IOException {
        service.changedInterchange(123L, 456L);
        verify(messagingService).interchangeChanged(isNull(String.class),
                eq("{\"intchid0\":123,\"intchid1\":456}".getBytes("UTF-8")));
    }

}
