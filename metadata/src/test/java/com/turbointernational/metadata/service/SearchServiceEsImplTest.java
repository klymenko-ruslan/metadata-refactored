package com.turbointernational.metadata.service;

import java.util.HashMap;
import java.util.Map;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.turbointernational.metadata.AbstractFunctionalTest;

/**
 * These are not real integration tests. It is rather code snippets for testing
 * implemented functionality. These tests are launched manually.
 *
 * Created by dmytro.trunykov@zorallabs.com on 2016-11-05.
 */
public class SearchServiceEsImplTest extends AbstractFunctionalTest {

    @Autowired
    private SearchService searchService;

    @Ignore
    @Test
    public void testFilterParts_PartNum() throws Exception {
        String json = searchService.filterParts("5304-150-0006", null, null, null, null, null, null, null, null, null,
                null, null, null, null, null, null, null, 10);
        System.out.println(json);
    }

    @Ignore
    @Test
    public void testFilterParts_CritDim_Exact() throws Exception {
        Map<String, String[]> params = new HashMap<>();
        params.put("brngHsngBoreDiaMin", new String[] { "0.545" });
        String json = searchService.filterParts(null, 13L, null, null, null, null, null, null, null, null, null, null,
                null, params, null, null, null, 10);
        System.out.println("FOUND:");
        System.out.println(json);
    }

    @Ignore
    @Test
    public void testFilterParts_CritDim_Range() throws Exception {
        Map<String, String[]> params = new HashMap<>();
        params.put("brngHsngBoreDiaMin", new String[] { "0.544..0.546" });
        String json = searchService.filterParts(null, 13L, null, null, null, null, null, null, null, null, null, null,
                null, params, null, null, null, 10);
        System.out.println("FOUND:");
        System.out.println(json);
    }

    @Ignore
    @Test
    public void testFilterParts_CritDim_Gt() throws Exception {
        Map<String, String[]> params = new HashMap<>();
        params.put("brngHsngBoreDiaMin", new String[] { "> 0.544" });
        String json = searchService.filterParts(null, 13L, null, null, null, null, null, null, null, null, null, null,
                null, params, null, null, null, 10);
        System.out.println("FOUND:");
        System.out.println(json);
    }

    @Ignore
    @Test
    public void testFilterParts_CritDim_Lt() throws Exception {
        Map<String, String[]> params = new HashMap<>();
        params.put("brngHsngBoreDiaMin", new String[] { "< 0.546" });
        String json = searchService.filterParts(null, 13L, null, null, null, null, null, null, null, null, null, null,
                null, params, null, null, null, 10);
        System.out.println("FOUND:");
        System.out.println(json);
    }

}