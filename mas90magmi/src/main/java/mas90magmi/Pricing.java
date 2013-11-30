package mas90magmi;

import com.healthmarketscience.jackcess.Row;
import mas90magmi.Pricing.PriceBreak;
import java.math.BigDecimal;
import static mas90magmi.DiscountType.Amount;
import static mas90magmi.DiscountType.Override;
import static mas90magmi.DiscountType.Percentage;

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
    
    public static class PriceBreak {
        private final int position;
        private final BigDecimal quantity;
        private final BigDecimal rate;

        public PriceBreak(int position, BigDecimal quantity, BigDecimal rate) {
            this.position = position;
            this.quantity = quantity;
            this.rate = rate;
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
    }
    
    public static final int BREAK_COUNT = 5;
    
    public static Pricing fromRow(Row row, String discountMethodColumnName) {
        String discountTypeCode = (String) row.get(discountMethodColumnName);
        Pricing pricing = new Pricing(DiscountType.getDiscountType(discountTypeCode));
        
        for (int i = 0; i < BREAK_COUNT; i++) {
            String breakColumnName = "BreakQty" + (i+1);
            String rateColumnName  = "DiscountMarkupPriceRate" + (i+1);
            
            pricing.breaks[i] = (BigDecimal) row.get(breakColumnName); // Quantity
            pricing.rates[i] = (BigDecimal) row.get(rateColumnName);   // Discount/Markup/Price/Rate
        }
        
        return pricing;
    }
    
    private final DiscountType discountType;
    private final BigDecimal[] breaks = new BigDecimal[BREAK_COUNT];
    private final BigDecimal[] rates  = new BigDecimal[BREAK_COUNT];
    
    private Pricing(DiscountType discountType) {
        this.discountType = discountType;
    }

    public PriceBreak getPriceBreak(int level) {
        
        if (level < 0 || level >= BREAK_COUNT) {
            throw new IllegalArgumentException("Level must be between 0 and " + (BREAK_COUNT - 1));
        }
        
        if (breaks[level] != null && rates[level] != null) {
            return new PriceBreak(level, breaks[level], rates[level]);
        }
        
        return null;
    }

    public DiscountType getDiscountType() {
        return discountType;
    }
    
    public BigDecimal applyPriceBreak(int level, BigDecimal standardPrice) {
        PriceBreak priceBreak = getPriceBreak(level);
        
        switch (discountType) {
            case Amount:
                return standardPrice.subtract(priceBreak.getRate());
            case Override:
                return priceBreak.getRate();
            case Percentage:
                BigDecimal pctMultiplier = priceBreak.getRate().movePointLeft(2);
                
                return standardPrice.multiply(pctMultiplier);
            default:
                throw new IllegalStateException("Unknown discount type.");
        }
    }
    
}
