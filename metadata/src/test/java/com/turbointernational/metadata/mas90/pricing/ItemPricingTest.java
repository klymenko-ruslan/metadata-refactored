package com.turbointernational.metadata.mas90.pricing;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 *
 * @author jrodriguez
 */
public class ItemPricingTest {
    
    ItemPricing instance;
    
    @Before
    public void setUp() {
        instance = new ItemPricing("FOO123", new BigDecimal("12.1300"));
    }
    
    @Test
    public void testItemPricing() {
        assertEquals("FOO123", instance.getItemNumber());
        assertEquals(new BigDecimal("12.1300"), instance.getStandardPrice());
    }
    
    @Test
    public void testCalculateCustomerSpecificPrices() {
        instance.getCustomerPricings().put("me@me.com", new Pricing(
                DiscountType.Override,
                new BigDecimal[] {
                    new BigDecimal("999999"),
                    BigDecimal.ZERO,
                    BigDecimal.ZERO,
                    BigDecimal.ZERO,
                    BigDecimal.ZERO
                },
                new BigDecimal[] {
                    new BigDecimal("10"),
                    BigDecimal.ZERO,
                    BigDecimal.ZERO,
                    BigDecimal.ZERO,
                    BigDecimal.ZERO
                }));
        
        Map<String, List<CalculatedPrice>> prices = instance.calculateCustomerSpecificPrices();
        
        assertEquals(1, prices.size());
        assertEquals(1, prices.get("me@me.com").size());
        assertEquals(0, prices.get("me@me.com").get(0).getBreakLevel());
        assertEquals(999999, prices.get("me@me.com").get(0).getQuantity());
        assertEquals(new BigDecimal("10"), prices.get("me@me.com").get(0).getPrice());
    }

}