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
        mas90Db.getTable("IMB_PriceCode").forEach(row -> {
            String priceCode = (String) row.get("PriceCodeRecord");
            String customerPriceLevel = ObjectUtils.toString(row.get("CustomerPriceLevel"), "");
            String pricingMethod = ObjectUtils.toString(row.get("Method"), "");
            String breakQuantity1 = ObjectUtils.toString(row.get("BreakQty1"));
            String breakQuantity2 = ObjectUtils.toString(row.get("BreakQty2"));
            String breakQuantity3 = ObjectUtils.toString(row.get("BreakQty3"));
            String breakQuantity4 = ObjectUtils.toString(row.get("BreakQty4"));
            String breakQuantity5 = ObjectUtils.toString(row.get("BreakQty5"));
            String discountMarkUp1 = ObjectUtils.toString(row.get("DiscountMarkupPriceRate1"));
            String discountMarkUp2 = ObjectUtils.toString(row.get("DiscountMarkupPriceRate2"));
            String discountMarkUp3 = ObjectUtils.toString(row.get("DiscountMarkupPriceRate3"));
            String discountMarkUp4 = ObjectUtils.toString(row.get("DiscountMarkupPriceRate4"));
            String discountMarkUp5 = ObjectUtils.toString(row.get("DiscountMarkupPriceRate5"));
            String itemCode = ObjectUtils.toString(row.get("ItemNumber"));
            String customerNo = ObjectUtils.toString(row.get("CustomerNumber"));
            insertPrices(priceCode, customerPriceLevel, pricingMethod, breakQuantity1, breakQuantity2,
                    breakQuantity3, breakQuantity4, breakQuantity5, discountMarkUp1, discountMarkUp2,
                    discountMarkUp3, discountMarkUp4, discountMarkUp5, itemCode, customerNo);
        });

    }

}
