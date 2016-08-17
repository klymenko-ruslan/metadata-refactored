package com.turbointernational.metadata.utils;

import org.junit.Test;

import static com.turbointernational.metadata.utils.RegExpUtils.*;
import static org.junit.Assert.*;

/**
 * Created by dmytro.trunykov@zorallabs.com on 11.05.16.
 */
public class RegExpUtilsTest {

    @Test
    public void testPtrnInteger() {
        assertTrue(PTRN_INTEGER.matcher("123").matches());
        assertTrue(PTRN_INTEGER.matcher(" 123 ").matches());
        assertTrue(PTRN_INTEGER.matcher("-123").matches());
        assertTrue(PTRN_INTEGER.matcher("+123").matches());
        assertTrue(PTRN_INTEGER.matcher(" -123 ").matches());
        assertTrue(PTRN_INTEGER.matcher(" +123 ").matches());
        assertFalse(PTRN_INTEGER.matcher("- 123").matches());
        assertFalse(PTRN_INTEGER.matcher("+ 123").matches());
        assertFalse(PTRN_INTEGER.matcher("1.23").matches());
    }

    @Test
    public void testPtrnDouble() {
        assertTrue(PTRN_DOUBLE.matcher("123").matches());
        assertTrue(PTRN_DOUBLE.matcher(" 123 ").matches());
        assertTrue(PTRN_DOUBLE.matcher("1.23").matches());
        assertTrue(PTRN_DOUBLE.matcher(" 1.23 ").matches());
        assertTrue(PTRN_DOUBLE.matcher("-123").matches());
        assertTrue(PTRN_DOUBLE.matcher("+123").matches());
        assertTrue(PTRN_DOUBLE.matcher(" -123 ").matches());
        assertTrue(PTRN_DOUBLE.matcher(" +123 ").matches());
        assertTrue(PTRN_DOUBLE.matcher("-123.4").matches());
        assertTrue(PTRN_DOUBLE.matcher("+123.4").matches());
        assertTrue(PTRN_DOUBLE.matcher(" -123.4 ").matches());
        assertTrue(PTRN_DOUBLE.matcher(" +123.4 ").matches());
        assertFalse(PTRN_DOUBLE.matcher("- 123.4").matches());
        assertFalse(PTRN_DOUBLE.matcher("+ 123.4").matches());
        assertFalse(PTRN_DOUBLE.matcher("123,4").matches());
    }

    @Test
    public void testParseInt_0() {
        assertNull(parseInt(null));
        assertEquals(Integer.valueOf(123), parseInt("123"));
        assertEquals(Integer.valueOf(123), parseInt(" 123 "));
        assertEquals(Integer.valueOf(-123), parseInt("-123"));
        assertEquals(Integer.valueOf(-123), parseInt(" -123 "));
        assertEquals(Integer.valueOf(123), parseInt("+123"));
        assertEquals(Integer.valueOf(123), parseInt(" +123 "));
     }

    @Test(expected = NumberFormatException.class)
    public void testParseInt_1() {
        parseInt("123.4");
    }

    @Test(expected = NumberFormatException.class)
    public void testParseInt_1_0() {
        parseInt(" 123.4 ");
    }

    @Test(expected = NumberFormatException.class)
    public void testParseInt_1_1() {
        parseInt("- 123");
    }

    @Test(expected = NumberFormatException.class)
    public void testParseInt_1_2() {
        parseInt(" -1 23 ");
    }

    @Test(expected = NumberFormatException.class)
    public void testParseInt_1_3() {
        parseInt("+ 123");
    }

    @Test(expected = NumberFormatException.class)
    public void testParseInt_1_4() {
        parseInt(" + 123 ");
    }

    @Test
    public void testParseDouble_0() {
        assertNull(parseDouble(null));
        assertEquals(Double.valueOf(123), parseDouble("123"));
        assertEquals(Double.valueOf(123), parseDouble(" 123 "));
        assertEquals(Double.valueOf(-123), parseDouble("-123"));
        assertEquals(Double.valueOf(-123), parseDouble(" -123 "));
        assertEquals(Double.valueOf(123), parseDouble("+123"));
        assertEquals(Double.valueOf(123), parseDouble(" +123 "));
        assertEquals(Double.valueOf(123.4), parseDouble("123.4"));
        assertEquals(Double.valueOf(123.4), parseDouble(" 123.4 "));
        assertEquals(Double.valueOf(-123.4), parseDouble("-123.4"));
        assertEquals(Double.valueOf(-123.4), parseDouble(" -123.4 "));
        assertEquals(Double.valueOf(123.4), parseDouble("+123.4"));
        assertEquals(Double.valueOf(123.4), parseDouble(" +123.4 "));
     }

    @Test(expected = NumberFormatException.class)
    public void testParseDouble_1() {
        parseDouble("123,4");
    }

    @Test(expected = NumberFormatException.class)
    public void testParseDouble_2() {
        parseDouble("123.45.6");
    }

}