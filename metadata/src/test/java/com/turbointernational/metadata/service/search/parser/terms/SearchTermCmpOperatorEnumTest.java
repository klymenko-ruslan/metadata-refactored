package com.turbointernational.metadata.service.search.parser.terms;

import org.junit.Test;

import static com.turbointernational.metadata.service.search.parser.terms.SearchTermCmpOperatorEnum.*;
import static org.junit.Assert.*;

/**
 * Created by dmytro.trunykov@zorallabs.com on 2016-05-11.
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