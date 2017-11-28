package com.turbointernational.metadata.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.test.util.ReflectionTestUtils;

import com.turbointernational.metadata.service.GraphDbService.GetAncestorsResponse;
import com.turbointernational.metadata.web.dto.Ancestor;
import com.turbointernational.metadata.web.dto.Page;

@RunWith(Parameterized.class)
public class PartServiceAncestorsTest {

    private Long fPartId;
    private int fOffset;
    private int fLimit;
    private int fResultPageLength;

    @Mock
    private GraphDbService graphDbService;

    @Mock
    private DtoMapperService dtoMapperService;

    private PartService partService;

    //@formatter:off
    private GetAncestorsResponse graphDbResponse = new GetAncestorsResponse(new GetAncestorsResponse.Row[] {
        new GetAncestorsResponse.Row(123L, true, 1),
        new GetAncestorsResponse.Row(124L, true, 1),
        new GetAncestorsResponse.Row(125L, true, 2),
        new GetAncestorsResponse.Row(126L, false, 2),
        new GetAncestorsResponse.Row(127L, false, 2)
    });
    //@formatter:on

    //@formatter:off
    @Parameters
    public static List<Object[]> parameters() {
        return Arrays.asList(new Object[][] {
            { 10L, -5, 3, 0 },
            { 10L, 0, 3, 3 },
            { 10L, 1, 3, 3 },
            { 10L, 3, 3, 2 },
            { 10L, 5, 3, 0 },
        });
    }
    //@formatter:on

    public PartServiceAncestorsTest(Long partIdA, int offsetA, int limitA, int resultPageLength) {
        fPartId = partIdA;
        fOffset = offsetA;
        fLimit = limitA;
        fResultPageLength = resultPageLength;
    }

    @Before
    public void setUp() {
        initMocks(this);
        this.partService = new PartService();
        ReflectionTestUtils.setField(partService, "graphDbService", graphDbService);
        ReflectionTestUtils.setField(partService, "dtoMapperService", dtoMapperService);
    }

    @Test
    public void testSlicing() throws Exception {
        when(graphDbService.getAncestors(fPartId)).thenReturn(graphDbResponse);
        when(dtoMapperService.map(Mockito.any(GetAncestorsResponse.Row[].class), Mockito.any()))
                .thenAnswer(new Answer<Ancestor[]>() {
                    @Override
                    public Ancestor[] answer(InvocationOnMock invocation) throws Throwable {
                        GetAncestorsResponse.Row[] rows = invocation.getArgument(0);
                        return new Ancestor[rows.length];
                    }

                });
        Page<Ancestor> page = partService.ancestors(fPartId, fOffset, fLimit);
        assertEquals(fResultPageLength, page.getRecs().size());
    }

}
