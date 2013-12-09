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
    public void testCalculateCustomerSpecificPrices() {
        fail("TODO");
    }

}