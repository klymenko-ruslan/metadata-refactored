package mas90magmi;

import com.healthmarketscience.jackcess.Row;
import java.math.BigDecimal;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 *
 * @author jrodriguez
 */
public class ItemPricing {
    
    static final String ITEM_PRICE_LEVEL_CODE = "1";
    
    static final String ITEM_CUSTOMER_CODE = "2";
    
    private final String itemNumber;
    
    /**
     * Standard pricing
     */
    private final BigDecimal standardPrice;
    
    /**
     * Customer-specific item pricing.
     */
    private final TreeMap<Customer, Pricing> customerPricings = new TreeMap();
    
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
    public SortedMap<Customer, Pricing> getCustomerPricings() {
        return customerPricings;
    }

    /**
     * @return the priceLevelPricings
     */
    public SortedMap<String, Pricing> getPriceLevelPricings() {
        return priceLevelPricings;
    }
    
    /**
     * Adds a pricing 
     * @param row 
     */
    void addPricing(Row row) {
        String priceCodeRecord = (String) row.get("PriceCodeRecord");
        
        if (ITEM_CUSTOMER_CODE.equals(priceCodeRecord)) {
            
            // Customer-specific item price
            getCustomerPricings().put(Customer.fromRow(row), Pricing.fromRow(row, "ItemCustomerRecordMethod"));
            
        } else if (ITEM_PRICE_LEVEL_CODE.equals(priceCodeRecord)) {
            
            // Price-level specific item price
            String priceLevel = (String) row.get("ItemCustomerPriceLevel");
            getPriceLevelPricings().put(priceLevel, Pricing.fromRow(row, "ItemMethod"));
        }
    }
    
}
