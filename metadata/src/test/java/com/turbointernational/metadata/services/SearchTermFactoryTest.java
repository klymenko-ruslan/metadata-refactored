package com.turbointernational.metadata.services;

import com.turbointernational.metadata.services.SearchTermFactory.Limit;
import com.turbointernational.metadata.services.SearchTermFactory.Range;
import org.junit.Test;

import static com.turbointernational.metadata.services.SearchTermCmpOperatorEnum.*;
import static org.junit.Assert.*;

/**
 * Created by dmytry.trunykov@zorallabs.com on 10.05.16.
 */
public class SearchTermFactoryTest {


    @Test
    public void testParseLimit() throws Exception {
        Object[][] cases = new Object[][]{
                new Object[]{null, null},

                new Object[]{"<5.1", new Limit(LT, new Double(5.1))},
                new Object[]{" < 5.1 ", new Limit(LT, new Double(5.1))},
                new Object[]{" < +5.1 ", new Limit(LT, new Double(5.1))},
                new Object[]{" < -5.1 ", new Limit(LT, new Double(-5.1))},

                new Object[]{"<=5.1", new Limit(LTE, new Double(5.1))},
                new Object[]{" <= 5.1", new Limit(LTE, new Double(5.1))},
                new Object[]{" <= +5.1", new Limit(LTE, new Double(5.1))},
                new Object[]{" <= -5.1", new Limit(LTE, new Double(-5.1))},

                new Object[]{"5.1", new Limit(EQ, new Double(5.1))},
                new Object[]{" 5.1 ", new Limit(EQ, new Double(5.1))},
                new Object[]{" -5.1 ", new Limit(EQ, new Double(-5.1))},
                new Object[]{" +5.1 ", new Limit(EQ, new Double(5.1))},

                new Object[]{"=5.1", new Limit(EQ, new Double(5.1))},
                new Object[]{" = 5.1", new Limit(EQ, new Double(5.1))},
                new Object[]{" =-5.1 ", new Limit(EQ, new Double(-5.1))},
                new Object[]{" =+5.1 ", new Limit(EQ, new Double(5.1))},

                new Object[]{">=5.1", new Limit(GTE, new Double(5.1))},
                new Object[]{" >= 5.1", new Limit(GTE, new Double(5.1))},
                new Object[]{" >= +5.1", new Limit(GTE, new Double(5.1))},
                new Object[]{" >= -5.1", new Limit(GTE, new Double(-5.1))},

                new Object[]{">5.1", new Limit(GT, new Double(5.1))},
                new Object[]{" > 5.1", new Limit(GT, new Double(5.1))},
                new Object[]{" > -5.1", new Limit(GT, new Double(-5.1))},
                new Object[]{" > +5.1", new Limit(GT, new Double(5.1))},
        };
        for (Object[] c : cases) {
            String s = (String) c[0];
            Limit expectation = (Limit) c[1];
            Limit limit = SearchTermFactory.parseLimit(s);
            assertEquals(expectation, limit);
        }
    }

    @Test
    public void testParseRange() {
        Object[][] cases = new Object[][]{
                new Object[]{null, null},
                new Object[]{"-1.5..5.8", new Range(new Limit(GTE, new Double(-1.5)), new Limit(LTE, new Double(5.8)))},
        };
        for (Object[] c : cases) {
            String s = (String) c[0];
            Range expectation = (Range) c[1];
            Range range = SearchTermFactory.parseRange(s);
            assertEquals(expectation, range);
        }
    }

    @Test
    public void parseSearchStr_0() throws Exception {
        Range range;
        range = SearchTermFactory.parseSearchStr("0.05");
        assertNotNull(range);
        assertEquals(EQ, range.limit1.operator);
        assertNotNull(range.limit1.val);
        assertEquals(new Double(0.05), range.limit1.val);
        assertNull(range.limit2);
        assertFalse(range.isBoundLimit());
    }

    @Test
    public void parseSearchStr_1() throws Exception {
        Range range;
        range = SearchTermFactory.parseSearchStr("= 0.05");
        assertNotNull(range);
        assertEquals(EQ, range.limit1.operator);
        assertNotNull(range.limit1.val);
        assertEquals(new Double(0.05), range.limit1.val);
        assertNull(range.limit2);
        assertFalse(range.isBoundLimit());
    }

    @Test
    public void parseSearchStr_2() throws Exception {
        Range range;
        range = SearchTermFactory.parseSearchStr("< 0.05");
        assertNotNull(range);
        assertEquals(LT, range.limit1.operator);
        assertNotNull(range.limit1.val);
        assertEquals(new Double(0.05), range.limit1.val);
        assertNull(range.limit2);
        assertFalse(range.isBoundLimit());
    }

    @Test
    public void parseSearchStr_3() throws Exception {
        Range range;
        range = SearchTermFactory.parseSearchStr("<= 0.05");
        assertNotNull(range);
        assertEquals(LTE, range.limit1.operator);
        assertNotNull(range.limit1.val);
        assertEquals(new Double(0.05), range.limit1.val);
        assertNull(range.limit2);
        assertFalse(range.isBoundLimit());
    }

    @Test
    public void parseSearchStr_4() throws Exception {
        Range range;
        range = SearchTermFactory.parseSearchStr("> 0.05");
        assertNotNull(range);
        assertEquals(SearchTermCmpOperatorEnum.GT, range.limit1.operator);
        assertNotNull(range.limit1.val);
        assertEquals(new Double(0.05), range.limit1.val);
        assertNull(range.limit2);
        assertFalse(range.isBoundLimit());
    }

    @Test
    public void parseSearchStr_5() throws Exception {
        Range range;
        range = SearchTermFactory.parseSearchStr(">= 0.05");
        assertNotNull(range);
        assertEquals(GTE, range.limit1.operator);
        assertNotNull(range.limit1.val);
        assertEquals(new Double(0.05), range.limit1.val);
        assertNull(range.limit2);
        assertFalse(range.isBoundLimit());
    }

    @Test
    public void parseSearchStr_6() throws Exception {
        Range range;
        range = SearchTermFactory.parseSearchStr("0.05..0.07");
        assertNotNull(range);
        assertEquals(GTE, range.limit1.operator);
        assertNotNull(range.limit1.val);
        assertEquals(new Double(0.05), range.limit1.val);
        assertEquals(LTE, range.limit2.operator);
        assertNotNull(range.limit2.val);
        assertEquals(new Double(0.07), range.limit2.val);
        assertTrue(range.isBoundLimit());
    }

}
