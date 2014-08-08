package com.turbointernational.metadata.mas90;

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
    
    @Before
    public void setUp() throws IOException {
        String dbPath = getClass().getResource("/mas90.accdb").getFile();
        instance = new Mas90Prices(new File(dbPath));
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
        assertEquals("INFO@TURBOPARTS.ES", customer);
        
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
        assertEquals("race-tech@hotmail.com", customer);
        
        customer = customerIt.next();
        assertEquals("turbosbaez1@yahoo.com.mx", customer);
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
}