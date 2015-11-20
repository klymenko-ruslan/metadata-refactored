package com.turbointernational.metadata.mas90.pricing;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 *
 * @author jrodriguez
 */
public class DiscountTypeTest {

    @Test
    public void testGetDiscountType_Override() throws Exception {
        assertEquals(DiscountType.Override, DiscountType.getDiscountType("O"));
        assertEquals(DiscountType.Override, DiscountType.getDiscountType("o"));
    }

    @Test
    public void testGetDiscountType_Percentage() throws Exception {
        assertEquals(DiscountType.Percentage, DiscountType.getDiscountType("D"));
        assertEquals(DiscountType.Percentage, DiscountType.getDiscountType("d"));
    }

    @Test
    public void testGetDiscountType_Amount() throws Exception {
        assertEquals(DiscountType.Amount, DiscountType.getDiscountType("P"));
        assertEquals(DiscountType.Amount, DiscountType.getDiscountType("p"));
    }
}