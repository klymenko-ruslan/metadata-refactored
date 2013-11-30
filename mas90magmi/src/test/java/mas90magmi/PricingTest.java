package mas90magmi;

import mas90magmi.Pricing;
import mas90magmi.DiscountType;
import com.healthmarketscience.jackcess.Row;
import mas90magmi.Pricing.PriceBreak;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 *
 * @author jrodriguez
 */
public class PricingTest {
    
    Pricing instance;
    Row mockRow;
    
    @Before
    public void setUp() {
        mockRow = mock(Row.class);
        
        // Setup the default case: Price override with no additional bulk discount
        when(mockRow.get("ItemMethod")).thenReturn(DiscountType.Override.CODE);
        
        addMockRow(0, new BigDecimal("999999"), new BigDecimal("5000.0000")); 
        
        for (int i = 1; i < 5; i++) { // Skip the first row
            addMockRow(i, new BigDecimal("0"), new BigDecimal("0"));
        }
        
        instance = Pricing.fromRow(mockRow, "ItemMethod");
    }
    
    public void addMockRow(int level, BigDecimal breakColumn, BigDecimal rateColumn) {
        String breakColumnName = "BreakQty" + (level+1);
        String rateColumnName  = "DiscountMarkupPriceRate" + (level+1);

        when(mockRow.get(breakColumnName)).thenReturn(breakColumn);
        when(mockRow.get(rateColumnName)).thenReturn(rateColumn);
    }
    
    @Test
    public void testFromRow_CommonCase() {
        
        assertEquals(DiscountType.Override, instance.getDiscountType());
        
        // Using the default pricing instance
        PriceBreak expected = new Pricing.PriceBreak(0, new BigDecimal("999999"), new BigDecimal("5000.0000"));
        
        
        PriceBreak actual = instance.getPriceBreak(0);
        
        
        assertEquals(expected.getPosition(), actual.getPosition());
        assertEquals(expected.getQuantity(), actual.getQuantity());
        assertEquals(expected.getRate(), actual.getRate());
                
        
        // Check the other values
        for (int i = 1; i < Pricing.BREAK_COUNT; i++) {
            actual = instance.getPriceBreak(i);
        
            assertEquals(i, actual.getPosition());
            assertEquals(0, actual.getQuantity());
            assertEquals(0, actual.getRate().intValueExact());
        }
    }
    
    @Test
    public void testFromRow_AllPriceBreaks() {
        reset(mockRow);
        when(mockRow.get("ItemMethod")).thenReturn(DiscountType.Override.CODE);
        
        List<PriceBreak> expectedList = new ArrayList<PriceBreak>(Pricing.BREAK_COUNT);
        
        for (int i = 0; i < Pricing.BREAK_COUNT; i++) {
            BigDecimal quantity = new BigDecimal((i+1) * 100); // 100 qty increments
            BigDecimal price = new BigDecimal((Pricing.BREAK_COUNT - i) * 1000);
            
            addMockRow(i, quantity, price);
            
            expectedList.add(new PriceBreak(i, quantity, price));
        }
        
        
        instance = Pricing.fromRow(mockRow, "ItemMethod");
        
        assertEquals(DiscountType.Override, instance.getDiscountType());
                
        for (int i = 0; i < Pricing.BREAK_COUNT; i++) {
            PriceBreak expected = expectedList.get(i);

            PriceBreak actual = instance.getPriceBreak(i);
        
            assertEquals(expected.getPosition(), actual.getPosition());
            assertEquals(expected.getQuantity(), actual.getQuantity());
            assertEquals(expected.getRate(), actual.getRate());
        }
    }

    @Test
    public void testGetDiscountType_Override() {
        DiscountType discountType = DiscountType.Override;
        
        when(mockRow.get("ItemMethod")).thenReturn(discountType.CODE);
        
        instance = Pricing.fromRow(mockRow, "ItemMethod");
        
        assertEquals(discountType, instance.getDiscountType());
    }

    @Test
    public void testGetDiscountType_Amount() {
        DiscountType discountType = DiscountType.Amount;
        
        when(mockRow.get("ItemMethod")).thenReturn(discountType.CODE);
        
        instance = Pricing.fromRow(mockRow, "ItemMethod");
        
        assertEquals(discountType, instance.getDiscountType());
    }

    @Test
    public void testGetDiscountType_Percentage() {
        DiscountType discountType = DiscountType.Percentage;
        
        when(mockRow.get("ItemMethod")).thenReturn(discountType.CODE);
        
        instance = Pricing.fromRow(mockRow, "ItemMethod");
        
        assertEquals(discountType, instance.getDiscountType());
    }
}