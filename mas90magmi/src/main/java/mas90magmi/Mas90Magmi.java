package mas90magmi;

import com.healthmarketscience.jackcess.CursorBuilder;
import com.healthmarketscience.jackcess.Database;
import com.healthmarketscience.jackcess.DatabaseBuilder;
import com.healthmarketscience.jackcess.Row;
import com.healthmarketscience.jackcess.Table;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author jrodriguez
 */
public class Mas90Magmi {
    
    static final String PRICE_LEVEL_CODE = "0";
    
    private static SortedSet<String> priceLevels = new TreeSet();
    
    static {
        
        TreeSet<String> tempPriceLevels = new TreeSet();
        tempPriceLevels.add("0");
        tempPriceLevels.add("1");
        tempPriceLevels.add("2");
        tempPriceLevels.add("3");
        tempPriceLevels.add("4");
        tempPriceLevels.add("5");
        tempPriceLevels.add("E");
        tempPriceLevels.add("R");
        tempPriceLevels.add("W");
        
        Mas90Magmi.priceLevels = Collections.unmodifiableSortedSet(tempPriceLevels);
    }

    public static SortedSet<String> getPriceLevels() {
        return priceLevels;
    }
    
    private File path;
    
    private volatile Database db;
    
    private volatile PriceCalculator priceCalculator;

    public Mas90Magmi(String path) throws IOException {
        this.path = new File(path);
    }
    
    private Database db() throws IOException {
        if (db == null) {
            synchronized(this) {
                if (db == null) {
                    db = DatabaseBuilder.open(path);
                }
            }
        }
        
        return db;
    }

    public BigDecimal getStandardPrice(String itemNumber) throws IOException {
        itemNumber = padItemNumber(itemNumber);
        
        Table table =  db().getTable("IM1_InventoryMasterFile");
        
        Row row = CursorBuilder.findRow(table, Collections.singletonMap("ItemNumber", itemNumber));
        
        if (row != null) {
            BigDecimal stdPrice = (BigDecimal) row.get("StdPrice");
            if (stdPrice != null) {
                return stdPrice;
            }
        }
        
        return null;
    }

    public ItemPricing getItemPricing(String itemNumber) throws IOException {
        String paddedItemNumber = padItemNumber(itemNumber);
        ItemPricing itemPricing = new ItemPricing(itemNumber, getStandardPrice(itemNumber));
        
        // TODO: Iterating every row in this table is awfully hacky; import into SQL first?
        Table table = db().getTable("IMB_PriceCode");
        for (Row row : table) {
            
            // Check for our item number
            if (!paddedItemNumber.equals(row.get("ItemNumber"))) {
                continue;
            }
            
            // Customer-specific discounts only
            itemPricing.addPricing(row);
        }
        
        return itemPricing;
    }
    
    /**
     * Right-pad the item number with spaces.
     * @param itemNumber the item number to pad.
     * @return right-padded with spaces to 15 characters.
     */
    private String padItemNumber(String itemNumber) {
        return StringUtils.rightPad(itemNumber, 15);
    }

    public Map<String, Pricing> getPriceLevelPricings() throws IOException {
        Table table = db().getTable("IMB_PriceCode");
        
        Map<String, Pricing> priceLevelPricings = new TreeMap();
        
        // TODO: Ugly hack iteration again
        for (Row row : table) {
            
            // Only price levels
            if (!PRICE_LEVEL_CODE.equals(row.get("PriceCodeRecord"))) {
                continue;
            }
            
            
            // Get the price level
            String priceLevel = (String) row.get("CustomerPriceLevel");
            
            // BUG: https://github.com/zero-one/TurboInternational/issues/5#issuecomment-29331951
            if (priceLevel == null || priceLevel.isEmpty() || " ".equals(priceLevel)) {
                priceLevel = "2";
            }
            
            Pricing pricing = Pricing.fromRow(row, "Method");
            
            priceLevelPricings.put(priceLevel, pricing);
        }
        
        return priceLevelPricings;
    }
    
    public PriceCalculator getCalculator() throws IOException {
        if (priceCalculator == null) {
            synchronized (this) {
                if (priceCalculator == null) {
                    priceCalculator = new PriceCalculator(getPriceLevelPricings());
                }
            }
        }
        
        return priceCalculator;
    }

    public String getCustomerEmail(Customer customer) throws IOException {
        Table table =  db().getTable("AR_Customer");
        
        Map<String, String> filter = new HashMap<String, String>();
        filter.put("ARDivisionNo", customer.getDivision());
        filter.put("CustomerNo", customer.getCustomerNumber());
        
        Row row = CursorBuilder.findRow(table, filter);
        
        if (row != null) {
            return (String) row.get("EmailAddress");
        }
        
        return null;
    }
}
