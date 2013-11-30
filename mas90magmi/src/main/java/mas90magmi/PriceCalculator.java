package mas90magmi;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 *
 * @author jrodriguez
 */
public class PriceCalculator {
    
    private final Set<String> priceLevels = new TreeSet();
    
    private final Map<String, Pricing> priceLevelPricings;

    public PriceCalculator(Map<String, Pricing> priceLevelPricings) {
        this.priceLevelPricings = priceLevelPricings;
        
        priceLevels.add("0");
        priceLevels.add("1");
        priceLevels.add("2");
        priceLevels.add("3");
        priceLevels.add("4");
        priceLevels.add("5");
        priceLevels.add("E");
        priceLevels.add("R");
        priceLevels.add("W");
    }
    
    public Map<String, BigDecimal> calculateLevelPrices(ItemPricing item) {
        Map<String, BigDecimal> prices = new TreeMap();
        Map<String, Pricing> itemPriceLevelPricings = item.getPriceLevelPricings();
        
        for (String priceLevel : priceLevels) {
            
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
}
