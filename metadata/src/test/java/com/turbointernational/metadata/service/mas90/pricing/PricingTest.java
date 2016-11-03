package com.turbointernational.metadata.service.mas90.pricing;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 *
 * @author jrodriguez
 */
public class PricingTest {
    
    Pricing instance;
    ResultSet mockResultSet;
    Map<String, Pricing> priceLevelPricings = new HashMap();
    ItemPricing itemPricing;
    
    @Before
    public void setUp() throws SQLException {
        mockResultSet = mock(ResultSet.class);
        
        // Setup the default case: Price override with no additional bulk discount
        when(mockResultSet.getString("discount_type")).thenReturn(DiscountType.Override.CODE);
        
        addMockPriceBreak(0, new BigDecimal("999999"), new BigDecimal("5000.0000")); 
        
        for (int i = 1; i < 5; i++) { // Skip the first row
            addMockPriceBreak(i, new BigDecimal("0"), new BigDecimal("0"));
        }
        
        instance = Pricing.fromResultSet(mockResultSet);
        
        itemPricing = new ItemPricing("FOOBAR", new BigDecimal("10000"));
    }
    
    public void addMockPriceBreak(int level, BigDecimal breakColumn, BigDecimal rateColumn) throws SQLException {
        String breakColumnName = "BreakQty" + (level+1);
        String rateColumnName  = "DiscountMarkupPriceRate" + (level+1);

        when(mockResultSet.getBigDecimal(breakColumnName)).thenReturn(breakColumn);
        when(mockResultSet.getBigDecimal(rateColumnName)).thenReturn(rateColumn);
    }
    
    @Test
    public void testGetDiscountType_Override() throws SQLException {
        DiscountType discountType = DiscountType.Override;
        
        when(mockResultSet.getString("discount_type")).thenReturn(discountType.CODE);
        
        instance = Pricing.fromResultSet(mockResultSet);
        
        assertEquals(discountType, instance.getDiscountType());
    }

    @Test
    public void testGetDiscountType_Amount() throws SQLException {
        DiscountType discountType = DiscountType.Amount;
        
        when(mockResultSet.getString("discount_type")).thenReturn(discountType.CODE);
        
        instance = Pricing.fromResultSet(mockResultSet);
        
        assertEquals(discountType, instance.getDiscountType());
    }

    @Test
    public void testGetDiscountType_Percentage() throws SQLException {
        DiscountType discountType = DiscountType.Percentage;
        
        when(mockResultSet.getString("discount_type")).thenReturn(discountType.CODE);
        
        instance = Pricing.fromResultSet(mockResultSet);
        
        assertEquals(discountType, instance.getDiscountType());
    }

    @Test
    public void testApplyPriceBreak_Override() throws SQLException {
        DiscountType discountType = DiscountType.Override;
        
        reset(mockResultSet);
        when(mockResultSet.getString("discount_type")).thenReturn(discountType.CODE);
        addMockPriceBreak(0, new BigDecimal("999999"), new BigDecimal("50"));
        
        instance = Pricing.fromResultSet(mockResultSet);
        
        assertEquals(new BigDecimal("50"), instance.getPriceBreak(0).apply(new BigDecimal("75")));
    }

    @Test
    public void testApplyPriceBreak_Amount() throws SQLException {
        DiscountType discountType = DiscountType.Amount;
        
        reset(mockResultSet);
        when(mockResultSet.getString("discount_type")).thenReturn(discountType.CODE);
        addMockPriceBreak(0, new BigDecimal("999999"), new BigDecimal("25"));
        
        instance = Pricing.fromResultSet(mockResultSet);
        
        assertEquals(new BigDecimal("50"), instance.getPriceBreak(0).apply(new BigDecimal("75")));
    }

    @Test
    public void testApplyPriceBreak_Percentage() throws SQLException {
        DiscountType discountType = DiscountType.Percentage;
        
        reset(mockResultSet);
        when(mockResultSet.getString("discount_type")).thenReturn(discountType.CODE);
        addMockPriceBreak(0, new BigDecimal("999999"), new BigDecimal("25"));
        
        instance = Pricing.fromResultSet(mockResultSet);
        
        assertEquals(new BigDecimal("150.00"), instance.getPriceBreak(0).apply(new BigDecimal("200"))); // $200 @ 25% discount
    }
    

    @Test
    public void testCalculate_OneBreak() {
        instance = new Pricing(DiscountType.Override,
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
        
        List<CalculatedPrice> prices = instance.calculate(BigDecimal.ZERO);
        
        assertEquals(1, prices.size());
        
        CalculatedPrice price = prices.get(0);
        assertEquals(0, price.getBreakLevel());
        assertEquals(999999, price.getQuantity());
        assertEquals(new BigDecimal("10"), price.getPrice());
    }

    @Test
    public void testCalculate_AllBreaks() {
        instance = new Pricing(DiscountType.Override,
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
        
        List<CalculatedPrice> prices = instance.calculate(BigDecimal.ZERO);
        
        assertEquals(5, prices.size());
        
        for (int i = 0; i < Pricing.BREAK_COUNT; i++) {
            CalculatedPrice price = prices.get(i);
            assertEquals(i, price.getBreakLevel());
            assertEquals((i+1) * 100, price.getQuantity());
            assertEquals(new BigDecimal((5 - i) * 1000), price.getPrice());
        }
    }
}