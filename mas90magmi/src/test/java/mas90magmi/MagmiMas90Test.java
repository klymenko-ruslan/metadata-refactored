package mas90magmi;

import mas90magmi.Pricing;
import mas90magmi.DiscountType;
import mas90magmi.MagmiMas90;
import mas90magmi.Customer;
import mas90magmi.ItemPricing;
import mas90magmi.Pricing.PriceBreak;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;



/**
 *
 * @author jrodriguez
 */
public class MagmiMas90Test {
    
    MagmiMas90 instance;
    
    @Before
    public void setUp() throws IOException {
        instance = new MagmiMas90("/home/jrodriguez/Downloads/MAS90_pricing_model.accdb");
    }

    @Test
    public void testGetStandardPrice() throws Exception {
        String itemNumber = "015T-030";
        
        ItemPricing itemPricing = instance.getItemPricing(itemNumber);
        
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
        
        Iterator<Customer> customerIt = itemPricing.getCustomerPricings().keySet().iterator();
        assertTrue(customerIt.hasNext());
        
        Customer customer = customerIt.next();
        assertEquals("00", customer.getDivision());
        assertEquals("TURBOPA", customer.getCustomerNumber());
        
        Pricing pricing = itemPricing.getCustomerPricings().get(customer);
        assertEquals(DiscountType.Override, pricing.getDiscountType());
        
        PriceBreak discountLevel = pricing.getPriceBreak(0);
        assertEquals(999999, discountLevel.getQuantity());
        assertEquals(new BigDecimal("140.2000"), discountLevel.getRate());
    }

    @Test
    public void testGetItemCustomerPricings_Two() throws Exception {
        String itemNumber = "8-A-1511";
        
        ItemPricing itemPricing = instance.getItemPricing(itemNumber);
        
        assertEquals(2, itemPricing.getCustomerPricings().size());
        
        Iterator<Customer> customerIt = itemPricing.getCustomerPricings().keySet().iterator();
        assertTrue(customerIt.hasNext());
        
        Customer customer = customerIt.next();
        assertEquals("00", customer.getDivision());
        assertEquals("REPB541", customer.getCustomerNumber());
        
        customer = customerIt.next();
        assertEquals("00", customer.getDivision());
        assertEquals("RTS0061", customer.getCustomerNumber());
    }
    
    @Test
    public void testGetItemCustomerPricings_WithQuantityBreaks() throws Exception {
        String itemNumber = "8-A-1520";
        
        ItemPricing itemPricing = instance.getItemPricing(itemNumber);
        
        assertEquals(1, itemPricing.getCustomerPricings().size());
        
        Iterator<Customer> customerIt = itemPricing.getCustomerPricings().keySet().iterator();
        assertTrue(customerIt.hasNext());
        
        Customer customer = customerIt.next();
        assertEquals("00", customer.getDivision());
        assertEquals("CARD191", customer.getCustomerNumber());
        
        Pricing pricing = itemPricing.getCustomerPricings().get(customer);
        assertEquals(DiscountType.Override, pricing.getDiscountType());
        
        // Level 1
        PriceBreak discountLevel = pricing.getPriceBreak(0);
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
    public void testGetPriceLevels() throws Exception {
        Set<String> expectedPriceLevels = new HashSet<String>();
        expectedPriceLevels.add("#");
        expectedPriceLevels.add("0");
        expectedPriceLevels.add("1");
        expectedPriceLevels.add("2");
        expectedPriceLevels.add("3");
        expectedPriceLevels.add("4");
        expectedPriceLevels.add("5");
        expectedPriceLevels.add("E");
        expectedPriceLevels.add("R");
        expectedPriceLevels.add("W");
        
        Set<String> priceLevels = instance.getPriceLevels();
        
        assertEquals(expectedPriceLevels, priceLevels);
    }
    
}