package mas90magmi;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

/**
 *
 * @author jrodriguez
 */
public class Mas90PricesTest {
    
    Mas90Prices instance;
    Pricing testPricing1;
    Pricing testPricing2;
    
    @Before
    public void setUp() throws IOException {
        instance = new Mas90Prices(new File("/home/jrodriguez/Downloads/MAS90_pricing_model.accdb"));
        
        
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
    }

    @Test
    public void testGetStandardPrice() throws Exception {
        String itemNumber = "015T-030";
        
        BigDecimal standardPrice = instance.getStandardPrice(itemNumber);
        assertEquals(new BigDecimal("0.0800"), standardPrice);
    }

    @Test
    public void testGetItemPricing_StandardPrice() throws Exception {
        String itemNumber = "015T-030";
        
        ItemPricing itemPricing = instance.getItemPricing(itemNumber);
        
        assertEquals(new BigDecimal("0.0800"), itemPricing.getStandardPrice());
    }
    
    @Test
    public void testGetItemPricing_Customer() throws Exception {
        String itemNumber = "1-A-3246";
        
        ItemPricing itemPricing = instance.getItemPricing(itemNumber);
        
        assertEquals(1, itemPricing.getCustomerPricings().size());
        
        Iterator<String> customerIt = itemPricing.getCustomerPricings().keySet().iterator();
        assertTrue(customerIt.hasNext());
        
        String customer = customerIt.next();
        assertEquals("turboparts1@gmail.com", customer);
        
        Pricing pricing = itemPricing.getCustomerPricings().get(customer);
        assertEquals(DiscountType.Override, pricing.getDiscountType());
        
        Pricing.PriceBreak discountLevel = pricing.getPriceBreak(0);
        assertEquals(999999, discountLevel.getQuantity());
        assertEquals(new BigDecimal("140.2000"), discountLevel.getRate());
    }

    @Test
    public void testGetItemCustomerPricings_Two() throws Exception {
        String itemNumber = "8-A-1511";
        
        ItemPricing itemPricing = instance.getItemPricing(itemNumber);
        
        assertEquals(2, itemPricing.getCustomerPricings().size());
        
        Iterator<String> customerIt = itemPricing.getCustomerPricings().keySet().iterator();
        assertTrue(customerIt.hasNext());
        
        String customer = customerIt.next();
        assertEquals("turbosbaez1@yahoo.com.mx", customer);
        
        customer = customerIt.next();
        assertEquals("race-tech@hotmail.com", customer);
    }
    
    @Test
    public void testGetItemCustomerPricings_WithQuantityBreaks() throws Exception {
        String itemNumber = "8-A-1520";
        
        ItemPricing itemPricing = instance.getItemPricing(itemNumber);
        
        assertEquals(1, itemPricing.getCustomerPricings().size());
        
        Iterator<String> customerIt = itemPricing.getCustomerPricings().keySet().iterator();
        assertTrue(customerIt.hasNext());
        
        String customer = customerIt.next();
        assertEquals("abethomas@cardone.com", customer);
        
        Pricing pricing = itemPricing.getCustomerPricings().get(customer);
        assertEquals(DiscountType.Override, pricing.getDiscountType());
        
        // Level 1
        Pricing.PriceBreak discountLevel = pricing.getPriceBreak(0);
        assertEquals(99, discountLevel.getQuantity());
        assertEquals(new BigDecimal("1.1500"), discountLevel.getRate());
        
        // Level 2
        discountLevel = pricing.getPriceBreak(1);
        assertEquals(199, discountLevel.getQuantity());
        assertEquals(new BigDecimal("0.9500"), discountLevel.getRate());
        
        // Level 3
        discountLevel = pricing.getPriceBreak(2);
        assertEquals(999999, discountLevel.getQuantity());
        assertEquals(new BigDecimal("0.8000"), discountLevel.getRate());
    }

    @Test
    public void testCalculatePriceLevelPrices() {
    }

    
    @Test
    public void testGetPriceLevels() throws Exception {
        Set<String> expectedPriceLevels = new HashSet<String>();
        expectedPriceLevels.add("0");
        expectedPriceLevels.add("1");
        expectedPriceLevels.add("2");
        expectedPriceLevels.add("3");
        expectedPriceLevels.add("4");
        expectedPriceLevels.add("5");
        expectedPriceLevels.add("E");
        expectedPriceLevels.add("R");
        expectedPriceLevels.add("W");
        
        Set<String> priceLevels = Mas90Prices.getPriceLevels();
        
        assertEquals(expectedPriceLevels, priceLevels);
    }
    
    @Test
    public void testGetDefaultPriceLevelPricing() throws Exception {
        Map<String, Pricing> priceLevelPricings = instance.getDefaultPriceLevelPricing();

        assertTrue(Mas90Prices.getPriceLevels().containsAll(priceLevelPricings.keySet()));
        assertEquals(new BigDecimal("10.0000"),  priceLevelPricings.get("0").getPriceBreak(0).getRate());
        assertEquals(new BigDecimal("5.0000"),   priceLevelPricings.get("1").getPriceBreak(0).getRate());
        assertEquals(new BigDecimal("0.0000"),   priceLevelPricings.get("2").getPriceBreak(0).getRate());
        assertEquals(new BigDecimal("-5.0000"),  priceLevelPricings.get("3").getPriceBreak(0).getRate());
        assertEquals(new BigDecimal("-10.0000"), priceLevelPricings.get("4").getPriceBreak(0).getRate());
        assertEquals(new BigDecimal("-25.0000"), priceLevelPricings.get("5").getPriceBreak(0).getRate());
        assertEquals(new BigDecimal("20.0000"),  priceLevelPricings.get("E").getPriceBreak(0).getRate());
        assertEquals(new BigDecimal("0.0000"),   priceLevelPricings.get("R").getPriceBreak(0).getRate());
        assertEquals(new BigDecimal("27.5000"),  priceLevelPricings.get("W").getPriceBreak(0).getRate());
    }
    
    

//    @Test
//    public void testGetPriceLevelPrices_ItemSpecific() {
//        String priceLevel = "W";
//        instance.calculatePriceLevelPrices(priceLevel, testPricing1);
//        
//        priceLevelPricings.put(priceLevel, testPricing1);
//        itemPricing.getPriceLevelPricings().put(priceLevel, testPricing2);
//        
//        List<CalculatedPrice> prices = instance.getPriceLevelPrices(priceLevel, itemPricing);
//        assertEquals(1, prices.size());
//        
//        CalculatedPrice price = prices.get(0);
//        
//        assertEquals(0, price.getBreakLevel());
//        assertEquals(999999, price.getQuantity());
//        assertEquals(new BigDecimal("10"), price.getPrice());
//    }
//
//    @Test
//    public void testGetPriceLevelPrices_Default() {
//        priceLevelPricings.put(priceLevel, testPricing1);
//        
//        List<CalculatedPrice> prices = instance.getPriceLevelPrices(priceLevel, itemPricing);
//        
//        assertEquals(5, prices.size());
//        
//        for (int i = 0; i < Pricing.BREAK_COUNT; i++) {
//            CalculatedPrice price = prices.get(i);
//            assertEquals(i, price.getBreakLevel());
//            assertEquals((i+1) * 100, price.getQuantity());
//            assertEquals(new BigDecimal((5 - i) * 1000), price.getPrice());
//        }
//    }
}