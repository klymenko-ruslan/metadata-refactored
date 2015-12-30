package com.turbointernational.metadata.services.mas90.pricing;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 *
 * @author jrodriguez
 */
public class ItemPricing {
    
    private final String itemNumber;
    
    /**
     * Standard pricing
     */
    private final BigDecimal standardPrice;
    
    /**
     * Customer-specific item pricing.
     */
    private final TreeMap<String, Pricing> customerPricings = new TreeMap();
    
    /**
     * Price-level-specific item pricing.
     */
    private final TreeMap<String, Pricing> priceLevelPricings = new TreeMap();

    public ItemPricing(String itemNumber, BigDecimal standardPrice) {
        this.itemNumber = itemNumber;
        this.standardPrice = standardPrice;
    }

    /**
     * @return the itemNumber
     */
    public String getItemNumber() {
        return itemNumber;
    }

    /**
     * @return the standardPrice
     */
    public BigDecimal getStandardPrice() {
        return standardPrice;
    }

    /**
     * @return the customerPricings
     */
    public SortedMap<String, Pricing> getCustomerPricings() {
        return customerPricings;
    }

    /**
     * @return the priceLevelPricings
     */
    public SortedMap<String, Pricing> getPriceLevelPricings() {
        return priceLevelPricings;
    }
    
    public Map<String, List<CalculatedPrice>> calculateCustomerSpecificPrices() {
        Map<String, List<CalculatedPrice>> prices = new TreeMap();
        
        // Calculate customer-specific prices
        for (Map.Entry<String, Pricing> pricing : customerPricings.entrySet()) {
            
            List<CalculatedPrice> customerPrices = pricing.getValue().calculate(standardPrice);
            
            prices.put(pricing.getKey(), customerPrices);
        }
        
        return prices;
    }
    
}
