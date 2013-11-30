package mas90magmi;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;
import mas90magmi.Pricing.PriceBreak;

/**
 *
 * @author jrodriguez
 */
public class PriceCalculator {
    
    public static class CalculatedPrice {
        private String priceLevel;
        private int breakLevel;
        private int quantity;
        private BigDecimal price;

        public CalculatedPrice(String priceLevel, int breakLevel, int quantity, BigDecimal price) {
            this.priceLevel = priceLevel;
            this.breakLevel = breakLevel;
            this.quantity = quantity;
            this.price = price;
        }

        /**
         * @return the priceLevel
         */
        public String getPriceLevel() {
            return priceLevel;
        }

        /**
         * @return the breakLevel
         */
        public int getBreakLevel() {
            return breakLevel;
        }

        /**
         * @return the quantity
         */
        public int getQuantity() {
            return quantity;
        }

        /**
         * @return the price
         */
        public BigDecimal getPrice() {
            return price;
        }
    }
    
    private final Map<String, Pricing> priceLevelPricings;

    public PriceCalculator(Map<String, Pricing> priceLevelPricings) {
        this.priceLevelPricings = priceLevelPricings;
    }
    
    public Map<String, BigDecimal> calculateLevelPrices(ItemPricing item) {
        Map<String, BigDecimal> prices = new TreeMap();
        Map<String, Pricing> itemPriceLevelPricings = item.getPriceLevelPricings();
        
        for (String priceLevel : Mas90Magmi.getPriceLevels()) {
            
            // Get the item-level pricing if there is one
            Pricing itemPriceLevelPricing = itemPriceLevelPricings.get(priceLevel);
            if (itemPriceLevelPricing != null) {
                BigDecimal price = itemPriceLevelPricing.applyPriceBreak(0, item.getStandardPrice());
                prices.put(priceLevel, price);
                continue;
            }
            
            // Fall back to price-level pricing
            Pricing priceLevelPricing = priceLevelPricings.get(priceLevel);
            BigDecimal price = priceLevelPricing.applyPriceBreak(0, item.getStandardPrice());
            prices.put(priceLevel, price);
        }
        
        return prices;
    }
    
    public Pricing getPriceLevelPricing(String priceLevel, ItemPricing item) {
        
        // Get the item-level pricing if there is one
        Pricing itemPriceLevelPricing = item.getPriceLevelPricings().get(priceLevel);
        if (itemPriceLevelPricing != null) {
            return itemPriceLevelPricing;
        }

        // Fall back to price-level pricing
        return priceLevelPricings.get(priceLevel);
    }
    
    public Iterator<CalculatedPrice> calculatePriceBreaks(final String priceLevel, final ItemPricing item) {
        final AtomicInteger nextBreakLevel = new AtomicInteger(0);
        
        final Pricing priceLevelPricing = getPriceLevelPricing(priceLevel, item);
        
        return new Iterator<CalculatedPrice>() {
            public boolean hasNext() {
                PriceBreak priceBreak = priceLevelPricing.getPriceBreak(nextBreakLevel.get());
                return priceBreak.getQuantity() > 0;
            }

            public CalculatedPrice next() {
                PriceBreak priceBreak = priceLevelPricing.getPriceBreak(nextBreakLevel.get());
                BigDecimal price = priceLevelPricing.applyPriceBreak(nextBreakLevel.get(), item.getStandardPrice());
                
                return new CalculatedPrice(priceLevel, nextBreakLevel.getAndIncrement(), priceBreak.getQuantity(), price);
            }

            public void remove() {
                throw new UnsupportedOperationException("Not supported.");
            }
        };
    }
}
