package com.turbointernational.metadata.services;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by dmytry.trunykov@zorallabs.com on 10.05.16.
 */
public class SearchTermFactoryTest {

    @Test
    public void parseSearchStr_0() throws Exception {
        SearchTermFactory.ParseResult pr;
        pr = SearchTermFactory.parseSearchStr("0.05");
        assertNotNull(pr);
        assertEquals(SearchTermCmpOperatorEnum.EQ, pr.operator1);
        assertNotNull(pr.val1);
        assertEquals(new Double(0.05), pr.val1);
        assertNull(pr.operator2);
        assertNull(pr.val2);
        assertFalse(pr.isLimitedRange());
    }

    @Test
    public void parseSearchStr_1() throws Exception {
        SearchTermFactory.ParseResult pr;
        pr = SearchTermFactory.parseSearchStr("= 0.05");
        assertNotNull(pr);
        assertEquals(SearchTermCmpOperatorEnum.EQ, pr.operator1);
        assertNotNull(pr.val1);
        assertEquals(new Double(0.05), pr.val1);
        assertNull(pr.operator2);
        assertNull(pr.val2);
        assertFalse(pr.isLimitedRange());
    }

    @Test
    public void parseSearchStr_2() throws Exception {
        SearchTermFactory.ParseResult pr;
        pr = SearchTermFactory.parseSearchStr("< 0.05");
        assertNotNull(pr);
        assertEquals(SearchTermCmpOperatorEnum.LT, pr.operator1);
        assertNotNull(pr.val1);
        assertEquals(new Double(0.05), pr.val1);
        assertNull(pr.operator2);
        assertNull(pr.val2);
        assertFalse(pr.isLimitedRange());
    }

    @Test
    public void parseSearchStr_3() throws Exception {
        SearchTermFactory.ParseResult pr;
        pr = SearchTermFactory.parseSearchStr("<= 0.05");
        assertNotNull(pr);
        assertEquals(SearchTermCmpOperatorEnum.LTE, pr.operator1);
        assertNotNull(pr.val1);
        assertEquals(new Double(0.05), pr.val1);
        assertNull(pr.operator2);
        assertNull(pr.val2);
        assertFalse(pr.isLimitedRange());
    }

    @Test
    public void parseSearchStr_4() throws Exception {
        SearchTermFactory.ParseResult pr;
        pr = SearchTermFactory.parseSearchStr("> 0.05");
        assertNotNull(pr);
        assertEquals(SearchTermCmpOperatorEnum.GT, pr.operator1);
        assertNotNull(pr.val1);
        assertEquals(new Double(0.05), pr.val1);
        assertNull(pr.operator2);
        assertNull(pr.val2);
        assertFalse(pr.isLimitedRange());
    }

    @Test
    public void parseSearchStr_5() throws Exception {
        SearchTermFactory.ParseResult pr;
        pr = SearchTermFactory.parseSearchStr(">= 0.05");
        assertNotNull(pr);
        assertEquals(SearchTermCmpOperatorEnum.GTE, pr.operator1);
        assertNotNull(pr.val1);
        assertEquals(new Double(0.05), pr.val1);
        assertNull(pr.operator2);
        assertNull(pr.val2);
        assertFalse(pr.isLimitedRange());
    }

    @Test
    public void parseSearchStr_6() throws Exception {
        SearchTermFactory.ParseResult pr;
        pr = SearchTermFactory.parseSearchStr("0.05..0.07");
        assertNotNull(pr);
        assertEquals(SearchTermCmpOperatorEnum.GTE, pr.operator1);
        assertNotNull(pr.val1);
        assertEquals(new Double(0.05), pr.val1);
        assertEquals(SearchTermCmpOperatorEnum.LTE, pr.operator2);
        assertNotNull(pr.val2);
        assertEquals(new Double(0.07), pr.val2);
        assertTrue(pr.isLimitedRange());
    }

}
