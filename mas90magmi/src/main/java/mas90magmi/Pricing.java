package mas90magmi;

import com.healthmarketscience.jackcess.Row;
import mas90magmi.Pricing.PriceBreak;
import java.math.BigDecimal;

/**
 *
 * @author jrodriguez
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
    
}
