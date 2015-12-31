package com.turbointernational.metadata.services.mas90;

import com.healthmarketscience.jackcess.Database;
import com.healthmarketscience.jackcess.DatabaseBuilder;
import com.healthmarketscience.jackcess.Row;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.IOException;

/**
 *
 * @author jrodriguez
 */
public class MsAccessImpl extends AbstractMas90 {

    private final Database mas90Db;

    public MsAccessImpl(File mas90DbFile) throws IOException {
        // Open the mas90 database
        mas90Db = DatabaseBuilder.open(mas90DbFile);
        super.init();
    }

    /**
     * 
     * @return
     * @throws IOException 
     */
    @Override
    protected void loadMas90Data() throws IOException {
        
        // Load customer data
        for (Row row : mas90Db.getTable("AR_Customer")) {
            h2db.update("INSERT INTO customer (id, email) VALUES(?, ?)",
                    StringUtils.trim((String) row.get("CustomerNo")), row.get("EmailAddress"));
        }
        
        // Load product data
        for (Row row : mas90Db.getTable("IM1_InventoryMasterfile")) {
            h2db.update("MERGE INTO product (id, price) VALUES(?, ?)",
                    StringUtils.trim((String) row.get("ItemNumber")), row.get("StdPrice"));
        }
        
        // Load pricing data
        for (Row row : mas90Db.getTable("IMB_PriceCode")) {
            String priceCode = ObjectUtils.toString(row.get("PriceCodeRecord"));
            
            if ("0".equals(priceCode)) {

                // PriceLevel Pricing
                h2db.update("INSERT INTO price_level_prices (\n"
                        + "  price_level,\n"
                        + "  discount_type,\n"
                        + "  BreakQty1, DiscountMarkupPriceRate1,"
                        + "  BreakQty2, DiscountMarkupPriceRate2,"
                        + "  BreakQty3, DiscountMarkupPriceRate3,"
                        + "  BreakQty4, DiscountMarkupPriceRate4,"
                        + "  BreakQty5, DiscountMarkupPriceRate5"
                        + ") VALUES(?, ?,  ?, ?,  ?, ?,  ?, ?,  ?, ?,  ?, ?)",
                        row.get("CustomerPriceLevel"),
                        row.get("Method"),
                        row.get("BreakQty1"), row.get("DiscountMarkupPriceRate1"),
                        row.get("BreakQty2"), row.get("DiscountMarkupPriceRate2"),
                        row.get("BreakQty3"), row.get("DiscountMarkupPriceRate3"),
                        row.get("BreakQty4"), row.get("DiscountMarkupPriceRate4"),
                        row.get("BreakQty5"), row.get("DiscountMarkupPriceRate5"));
            } else if ("1".equals(priceCode)) {

                // Part-PriceLevel Pricing
                h2db.update("INSERT INTO product_price_level_prices (\n"
                        + "  product_id,\n"
                        + "  price_level,\n"
                        + "  discount_type,\n"
                        + "  BreakQty1, DiscountMarkupPriceRate1,"
                        + "  BreakQty2, DiscountMarkupPriceRate2,"
                        + "  BreakQty3, DiscountMarkupPriceRate3,"
                        + "  BreakQty4, DiscountMarkupPriceRate4,"
                        + "  BreakQty5, DiscountMarkupPriceRate5"
                        + ") VALUES(?, ?, ?,  ?, ?,  ?, ?,  ?, ?,  ?, ?,  ?, ?)",
                        StringUtils.trim((String) row.get("ItemNumber")),
                        row.get("ItemCustomerPriceLevel"),
                        row.get("ItemMethod"),
                        row.get("BreakQty1"), row.get("DiscountMarkupPriceRate1"),
                        row.get("BreakQty2"), row.get("DiscountMarkupPriceRate2"),
                        row.get("BreakQty3"), row.get("DiscountMarkupPriceRate3"),
                        row.get("BreakQty4"), row.get("DiscountMarkupPriceRate4"),
                        row.get("BreakQty5"), row.get("DiscountMarkupPriceRate5"));
            } else if ("2".equals(priceCode)) {

                // Part-Customer Pricing
                h2db.update("INSERT INTO product_customer_prices (\n"
                        + "  product_id,\n"
                        + "  customer_id,\n"
                        + "  discount_type,\n"
                        + "  BreakQty1, DiscountMarkupPriceRate1,"
                        + "  BreakQty2, DiscountMarkupPriceRate2,"
                        + "  BreakQty3, DiscountMarkupPriceRate3,"
                        + "  BreakQty4, DiscountMarkupPriceRate4,"
                        + "  BreakQty5, DiscountMarkupPriceRate5"
                        + ") VALUES(?, ?, ?,  ?, ?,  ?, ?,  ?, ?,  ?, ?,  ?, ?)",
                        StringUtils.trim((String) row.get("ItemNumber")),
                        StringUtils.trim((String) row.get("CustomerNumber")),
                        row.get("ItemCustomerRecordMethod"),
                        row.get("BreakQty1"), row.get("DiscountMarkupPriceRate1"),
                        row.get("BreakQty2"), row.get("DiscountMarkupPriceRate2"),
                        row.get("BreakQty3"), row.get("DiscountMarkupPriceRate3"),
                        row.get("BreakQty4"), row.get("DiscountMarkupPriceRate4"),
                        row.get("BreakQty5"), row.get("DiscountMarkupPriceRate5"));
            } else {
                throw new IllegalStateException("Unknown PriceCodeRecord in IMB_PriceCode: " + priceCode);
            }
            
            // Mas90 bug handling: https://github.com/pthiry/TurboInternational/issues/5#issuecomment-29331951
            h2db.update("UPDATE price_level_prices SET price_level = 2 WHERE price_level = ' '");
        }


    }

}
