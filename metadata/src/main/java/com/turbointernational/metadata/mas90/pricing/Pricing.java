package com.turbointernational.metadata.mas90.pricing;

import org.apache.commons.lang.ObjectUtils;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Quantity-based tiered pricing.
 * 
 * MAS90 supports 5 levels of price breaks. See: {@link #BREAK_COUNT}
 * 
 * Each level has a quantity and 'rate' used to calculate the final price.
 * 
 * The meaning of the price break 'rate' depends on the pricing discount type.
 * See {@link #applyPriceBreak(int, java.math.BigDecimal)}
 * 
 * A price break with quantity 999999 is the last price break.
 */
public class Pricing {
    
    public class PriceBreak {
        private final int position;
        private final BigDecimal quantity;
        private final BigDecimal rate;

        public PriceBreak(int position, BigDecimal quantity, BigDecimal rate) {
            this.position = position;
            this.quantity = (BigDecimal) ObjectUtils.defaultIfNull(quantity, BigDecimal.ZERO);
            this.rate = (BigDecimal) ObjectUtils.defaultIfNull(rate, BigDecimal.ZERO);
        }

        public int getPosition() {
            return position;
        }
        
        public int getQuantity() {
            return quantity.intValueExact();
        }

        public BigDecimal getRate() {
            return rate;
        }
    
        public BigDecimal apply(BigDecimal standardPrice) {
            switch (discountType) {
                case Amount:
                    return standardPrice.subtract(rate);
                case Override:
                    return rate;
                case Percentage:
                    BigDecimal pctMultiplier = rate.movePointLeft(2);
                    BigDecimal discountAmount = standardPrice.multiply(pctMultiplier);

                    return standardPrice.subtract(discountAmount);
                default:
                    throw new IllegalStateException("Unknown discount type.");
            }
        }
    }
    
    public static final int BREAK_COUNT = 5;
    
    public static Pricing fromResultSet(ResultSet rs) throws SQLException {
        String discountTypeCode = (String) rs.getString("discount_type");

        Pricing pricing = new Pricing(DiscountType.getDiscountType(discountTypeCode));

        for (int i = 0; i < BREAK_COUNT; i++) {
            String breakColumnName = "BreakQty" + (i+1);
            String rateColumnName  = "DiscountMarkupPriceRate" + (i+1);

            pricing.breaks[i] = rs.getBigDecimal(breakColumnName); // Quantity
            pricing.rates[i] = rs.getBigDecimal(rateColumnName);   // Discount/Markup/Price/Rate
        }

        return pricing;
    }
    
    private final DiscountType discountType;
    private final BigDecimal[] breaks;
    private final BigDecimal[] rates;
    
    public Pricing(DiscountType discountType, BigDecimal[] breaks, BigDecimal[] rates) {
        this.discountType = discountType;
        this.breaks = breaks;
        this.rates = rates;
    }
    
    private Pricing(DiscountType discountType) {
        this.discountType = discountType;
        this.breaks = new BigDecimal[BREAK_COUNT];
        this.rates = new BigDecimal[BREAK_COUNT];
    }

    public PriceBreak getPriceBreak(int level) {
        
        if (level < 0 || level >= BREAK_COUNT) {
            throw new IllegalArgumentException("Level must be between 0 and " + (BREAK_COUNT - 1));
        }
        
        return new PriceBreak(level, breaks[level], rates[level]);
    }

    public DiscountType getDiscountType() {
        return discountType;
    }
    
    public List<CalculatedPrice> calculate(BigDecimal standardPrice) {
        List<CalculatedPrice> prices = new ArrayList();

        // Calculate prices for each price break
        for (int i = 0; i < Pricing.BREAK_COUNT; i++) {
            PriceBreak priceBreak = getPriceBreak(i);
            
            // Add price breaks until their quantity == 0
            if (priceBreak.getQuantity() == 0) {
                break;
            }
        
            BigDecimal price = priceBreak.apply(standardPrice);
            prices.add(new CalculatedPrice(i, priceBreak.getQuantity(), price));
        }
        
        return prices;
    }
    
}
