package com.turbointernational.metadata.mas90;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author jrodriguez
 */
public class DiscountTypeTest {

    @Test
    public void testGetDiscountType_Override() {
        assertEquals(DiscountType.Override, DiscountType.getDiscountType("O"));
        assertEquals(DiscountType.Override, DiscountType.getDiscountType("o"));
    }

    @Test
    public void testGetDiscountType_Percentage() {
        assertEquals(DiscountType.Percentage, DiscountType.getDiscountType("D"));
        assertEquals(DiscountType.Percentage, DiscountType.getDiscountType("d"));
    }

    @Test
    public void testGetDiscountType_Amount() {
        assertEquals(DiscountType.Amount, DiscountType.getDiscountType("P"));
        assertEquals(DiscountType.Amount, DiscountType.getDiscountType("p"));
    }
}