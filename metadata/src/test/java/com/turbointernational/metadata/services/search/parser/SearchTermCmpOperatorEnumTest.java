package com.turbointernational.metadata.services.search.parser;

import org.junit.Test;

import static com.turbointernational.metadata.services.search.parser.SearchTermCmpOperatorEnum.*;
import static org.junit.Assert.*;

/**
 * Created by dmytro.trunykov@zorallabs.com on 11.05.16.
 */
public class SearchTermCmpOperatorEnumTest {

    @Test
    public void testFromSign() {
        assertNull(fromSign(null));
        assertEquals(LT, fromSign(" < "));
        assertEquals(LTE, fromSign(" <= "));
        assertEquals(EQ, fromSign(" = "));
        assertEquals(GTE, fromSign(" >= "));
        assertEquals(GT, fromSign(" > "));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFromSignFailure() {
        fromSign(" X ");
    }
}