package mas90magmi;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import mas90magmi.Pricing.PriceBreak;

/**
 *
 * @author jrodriguez
 */
public class PriceCalculator {
    
    public static class CalculatedPrice {
        private int breakLevel;
        private int quantity;
        private BigDecimal price;

        public CalculatedPrice(int breakLevel, int quantity, BigDecimal price) {
            this.breakLevel = breakLevel;
            this.quantity = quantity;
            this.price = price;
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
    
    public Map<Customer, List<CalculatedPrice>> calculateCustomerSpecificPrices(ItemPricing item) {
        Map<Customer, List<CalculatedPrice>> prices = new TreeMap();
        
        // Calculate customer-specific prices
        for (Entry<Customer, Pricing> pricing : item.getCustomerPricings().entrySet()) {
            
            List<CalculatedPrice> customerPrices = calculate(
                    item.getStandardPrice(),
                    pricing.getValue());
            
            
            prices.put(pricing.getKey(), customerPrices);
        }
        
        return prices;
    }
    
    public List<CalculatedPrice> getPriceLevelPrices(final String priceLevel, final ItemPricing item) {
        
        // Get the item-price-level pricing, if any
        Pricing pricing = item.getPriceLevelPricings().get(priceLevel);
        
        // Fall back to price-level pricing
        if (pricing == null) {
            pricing = priceLevelPricings.get(priceLevel);
        }
        
        return calculate(item.getStandardPrice(), pricing);
    }
    
    /**
     * Calculates the price quantity breaks for a given pricing.
     * @param standardPrice the standard price.
     * @param pricing the pricing to calculate based on the standard price.
     * @return a list of quantity break prices.
     */
    List<CalculatedPrice> calculate(BigDecimal standardPrice, Pricing pricing) {
        List<CalculatedPrice> prices = new ArrayList();

        // Add price breaks as long as their quantity is > 0
        int breakLevel = 0;
        PriceBreak priceBreak = pricing.getPriceBreak(breakLevel);
        while (priceBreak.getQuantity() > 0) {
            BigDecimal price = priceBreak.apply(standardPrice);
            prices.add(new CalculatedPrice(breakLevel, priceBreak.getQuantity(), price));
        }
        
        return prices;
    }
}
