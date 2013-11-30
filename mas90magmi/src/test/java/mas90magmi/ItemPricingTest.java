package mas90magmi;

import com.healthmarketscience.jackcess.Row;
import java.math.BigDecimal;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import org.junit.Before;

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
    public void testAddPricing_Customer() {
        Row mockRow = mock(Row.class);
        
        when(mockRow.get("Division")).thenReturn("00");
        when(mockRow.get("CustomerNumber")).thenReturn("MYCUST");
        when(mockRow.get("PriceCodeRecord")).thenReturn(ItemPricing.ITEM_CUSTOMER_CODE);
        when(mockRow.get("ItemCustomerRecordMethod")).thenReturn(DiscountType.Override.CODE);
        
        instance.addPricing(mockRow);
        
        assertTrue(instance.getPriceLevelPricings().isEmpty());
        assertEquals(1, instance.getCustomerPricings().size());
        
        Customer customer = instance.getCustomerPricings().firstKey();
        assertEquals("MYCUST", instance.getCustomerPricings().firstKey().getCustomerNumber());
        
        Pricing pricing = instance.getCustomerPricings().get(customer);
        assertEquals(DiscountType.Override, pricing.getDiscountType());
    }

    @Test
    public void testAddPricing_Item() {
        String expectedPriceLevel = "W";

        Row mockRow = mock(Row.class);
        
        when(mockRow.get("PriceCodeRecord")).thenReturn(ItemPricing.ITEM_PRICE_LEVEL_CODE);
        when(mockRow.get("ItemMethod")).thenReturn(DiscountType.Amount.CODE);
        when(mockRow.get("ItemCustomerPriceLevel")).thenReturn(expectedPriceLevel);
        
        
        instance.addPricing(mockRow);
        
        assertEquals(1, instance.getPriceLevelPricings().size());
        
        String priceLevel = instance.getPriceLevelPricings().firstKey();
        assertEquals(expectedPriceLevel, priceLevel);
        
        Pricing pricing = instance.getPriceLevelPricings().get(priceLevel);
        assertEquals(DiscountType.Amount, pricing.getDiscountType());
    }
}