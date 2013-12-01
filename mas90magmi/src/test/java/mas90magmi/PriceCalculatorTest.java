package mas90magmi;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mas90magmi.PriceCalculator.CalculatedPrice;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author jrodriguez
 */
public class PriceCalculatorTest {
    PriceCalculator instance;
    Map<String, Pricing> priceLevelPricings = new HashMap();
    String priceLevel = "W";
    Pricing testPricing1;
    Pricing testPricing2;
    ItemPricing itemPricing;
    
    @Before
    public void setUp() {
        instance = new PriceCalculator(priceLevelPricings);
        
        testPricing1 = new Pricing(DiscountType.Override,
                new BigDecimal[] {
                    new BigDecimal("100"),
                    new BigDecimal("200"),
                    new BigDecimal("300"),
                    new BigDecimal("400"),
                    new BigDecimal("500")
                },
                new BigDecimal[] {
                    new BigDecimal("5000"),
                    new BigDecimal("4000"),
                    new BigDecimal("3000"),
                    new BigDecimal("2000"),
                    new BigDecimal("1000")
                });
        
        
        testPricing2 = new Pricing(DiscountType.Override,
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
                });
        
        itemPricing = new ItemPricing("FOOBAR", new BigDecimal("10000"));
        
    }

    @Test
    public void testCalculate_OneBreak() {
        List<CalculatedPrice> prices = instance.calculate(BigDecimal.ZERO, testPricing2);
        
        assertEquals(1, prices.size());
        
        CalculatedPrice price = prices.get(0);
        assertEquals(0, price.getBreakLevel());
        assertEquals(999999, price.getQuantity());
        assertEquals(new BigDecimal("10"), price.getPrice());
    }

    @Test
    public void testCalculate_AllBreaks() {
        List<CalculatedPrice> prices = instance.calculate(BigDecimal.ZERO, testPricing1);
        
        assertEquals(5, prices.size());
        
        for (int i = 0; i < Pricing.BREAK_COUNT; i++) {
            CalculatedPrice price = prices.get(i);
            assertEquals(i, price.getBreakLevel());
            assertEquals((i+1) * 100, price.getQuantity());
            assertEquals(new BigDecimal((5 - i) * 1000), price.getPrice());
        }
    }

    @Test
    public void testCalculateCustomerSpecificPrices() {
        fail("TODO");
    }

    @Test
    public void testGetPriceLevelPrices_ItemSpecific() {
        priceLevelPricings.put(priceLevel, testPricing1);
        itemPricing.getPriceLevelPricings().put(priceLevel, testPricing2);
        
        List<CalculatedPrice> prices = instance.getPriceLevelPrices(priceLevel, itemPricing);
        assertEquals(1, prices.size());
        
        CalculatedPrice price = prices.get(0);
        
        assertEquals(0, price.getBreakLevel());
        assertEquals(999999, price.getQuantity());
        assertEquals(new BigDecimal("10"), price.getPrice());
    }

    @Test
    public void testGetPriceLevelPrices_Default() {
        priceLevelPricings.put(priceLevel, testPricing1);
        
        List<CalculatedPrice> prices = instance.getPriceLevelPrices(priceLevel, itemPricing);
        
        assertEquals(5, prices.size());
        
        for (int i = 0; i < Pricing.BREAK_COUNT; i++) {
            CalculatedPrice price = prices.get(i);
            assertEquals(i, price.getBreakLevel());
            assertEquals((i+1) * 100, price.getQuantity());
            assertEquals(new BigDecimal((5 - i) * 1000), price.getPrice());
        }
    }
}