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
import java.util.Set;
import java.util.TreeSet;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author jrodriguez
 */
public class MagmiMas90 {
    
    static final String PRICE_LEVEL_CODE = "0";
    
    private Database db;

    public MagmiMas90(String path) throws IOException {
        db = DatabaseBuilder.open(new File(path));
    }

    public BigDecimal getStandardPrice(String itemNumber) throws IOException {
        itemNumber = padItemNumber(itemNumber);
        
        Table table = db.getTable("IM1_InventoryMasterFile");
        
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
        Table table = db.getTable("IMB_PriceCode");
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

    public Set<String> getPriceLevels() throws IOException {
        Table table = db.getTable("AR_Customer");
        
        Set<String> priceLevels = new TreeSet<String>();
        
        // TODO: Ugly hack alert
        for (Row row : table) {
            String priceLevel = (String) row.get("PriceLevel");
            
            if (priceLevel != null) {
                priceLevels.add(priceLevel);
            }
        }
        
        return priceLevels;
    }
    
}
