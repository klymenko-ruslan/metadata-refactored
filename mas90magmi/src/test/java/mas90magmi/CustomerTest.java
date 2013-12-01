package mas90magmi;

import com.healthmarketscience.jackcess.Row;
import java.math.BigDecimal;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 *
 * @author jrodriguez
 */
public class CustomerTest {
    
    Row mockRow;
    
    @Before
    public void setUp() {
        mockRow = mock(Row.class);
        
        // Setup the default case: Price override with no additional bulk discount
        when(mockRow.get("ItemCustomerRecordMethod")).thenReturn(DiscountType.Override.CODE);
        
        addMockBreak(0, new BigDecimal("999999"), new BigDecimal("5000.0000")); 
        
        for (int i = 1; i < 5; i++) { // Skip the first row
            addMockBreak(i, new BigDecimal("0"), new BigDecimal("0"));
        }
    }
    
    public void addMockBreak(int level, BigDecimal breakColumn, BigDecimal rateColumn) {
        String breakColumnName = "BreakQty" + (level+1);
        String rateColumnName  = "DiscountMarkupPriceRate" + (level+1);

        when(mockRow.get(breakColumnName)).thenReturn(breakColumn);
        when(mockRow.get(rateColumnName)).thenReturn(rateColumn);
    }

    @Test
    public void testFromRow() {
        when(mockRow.get("CustomerNumber")).thenReturn("MYCUST");
        when(mockRow.get("Division")).thenReturn("00");
        
        Customer instance = Customer.fromRow(mockRow);
        assertEquals("00", instance.getDivision());
        assertEquals("MYCUST", instance.getCustomerNumber());
    }

    @Test
    public void testCompareTo() {
        Customer customerA = new Customer("00", "a");
        Customer customerB = new Customer("00", "b");
        
        assertTrue(customerA.compareTo(customerA) == 0);
        assertTrue(customerB.compareTo(customerB) == 0);
        
        assertTrue(customerB.compareTo(customerA) > 0);
        assertTrue(customerA.compareTo(customerB) < 0);
    }
}