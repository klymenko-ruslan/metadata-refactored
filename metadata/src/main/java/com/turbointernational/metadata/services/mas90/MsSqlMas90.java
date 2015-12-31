package com.turbointernational.metadata.services.mas90;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by dmytro.trunykov on 12/30/15.
 */
public class MsSqlMas90  extends AbstractMas90 {

    private JdbcTemplate mssqldb;

    public MsSqlMas90(String url, String username, String password) throws IOException {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        this.mssqldb = new JdbcTemplate(dataSource, true);
        super.init();
    }

    @Override
    protected void loadMas90Data() throws IOException {

        // Load customer data
        mssqldb.query("SELECT CUSTOMERNO, EMAILADDRESS FROM AR_CUSTOMER", new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                String customerNo = StringUtils.trim((String) rs.getString(1));
                String emailAddress = rs.getString(2);
                h2db.update("INSERT INTO customer (id, email) VALUES(?, ?)", customerNo, emailAddress);
            }
        });

        // Load product data
        mssqldb.query("SELECT ITEMCODE, STANDARDUNITPRICE FROM CI_ITEM", new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                String itemCode = StringUtils.trim((String) rs.getString(1));
                String stdPrice = rs.getString(2);
                h2db.update("MERGE INTO product (id, price) VALUES(?, ?)", itemCode, stdPrice);
            }
        });

        // Load pricing data
        mssqldb.query(
                "SELECT " +
                "   PRICECODERECORD, CUSTOMERPRICELEVEL, PRICINGMETHOD, BREAKQUANTITY1, BREAKQUANTITY2, " +
                "   BREAKQUANTITY3, BREAKQUANTITY4, BREAKQUANTITY5, DISCOUNTMARKUP1, DISCOUNTMARKUP2, " +
                "   DISCOUNTMARKUP3, DISCOUNTMARKUP4, DISCOUNTMARKUP5, ITEMCODE, CUSTOMERNO " +
                "FROM IM_PRICECODE", new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                String priceCode = ObjectUtils.toString(rs.getString(1));
                String customerPriceLevel = rs.getString(2);
                String pricingMethod = rs.getString(3);
                String breakQuantity1 = rs.getString(4);
                String breakQuantity2 = rs.getString(5);
                String breakQuantity3 = rs.getString(6);
                String breakQuantity4 = rs.getString(7);
                String breakQuantity5 = rs.getString(8);
                String discountMarkUp1 = rs.getString(9);
                String discountMarkUp2 = rs.getString(10);
                String discountMarkUp3 = rs.getString(11);
                String discountMarkUp4 = rs.getString(12);
                String discountMarkUp5 = rs.getString(13);
                String itemCode = StringUtils.trim(rs.getString(14));
                String customerNo = StringUtils.trim(rs.getString(15));
                if ("0".equals(priceCode)) {
                    // PriceLevel Pricing
                    h2db.update("INSERT INTO price_level_prices ("
                            + "  price_level,"
                            + "  discount_type,"
                            + "  BreakQty1, DiscountMarkupPriceRate1,"
                            + "  BreakQty2, DiscountMarkupPriceRate2,"
                            + "  BreakQty3, DiscountMarkupPriceRate3,"
                            + "  BreakQty4, DiscountMarkupPriceRate4,"
                            + "  BreakQty5, DiscountMarkupPriceRate5"
                            + ") VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                            customerPriceLevel, pricingMethod,
                            breakQuantity1, discountMarkUp1,
                            breakQuantity2, discountMarkUp2,
                            breakQuantity3, discountMarkUp3,
                            breakQuantity4, discountMarkUp4,
                            breakQuantity5, discountMarkUp5
                    );
                } else if ("1".equals(priceCode)) {
                    // Part-PriceLevel Pricing
                    h2db.update("INSERT INTO product_price_level_prices ("
                            + "  product_id,"
                            + "  price_level,"
                            + "  discount_type,"
                            + "  BreakQty1, DiscountMarkupPriceRate1,"
                            + "  BreakQty2, DiscountMarkupPriceRate2,"
                            + "  BreakQty3, DiscountMarkupPriceRate3,"
                            + "  BreakQty4, DiscountMarkupPriceRate4,"
                            + "  BreakQty5, DiscountMarkupPriceRate5"
                            + ") VALUES(?, ?, ?,  ?, ?,  ?, ?,  ?, ?,  ?, ?,  ?, ?)",
                            itemCode, customerPriceLevel, pricingMethod,
                            breakQuantity1, discountMarkUp1,
                            breakQuantity2, discountMarkUp2,
                            breakQuantity3, discountMarkUp3,
                            breakQuantity4, discountMarkUp4,
                            breakQuantity5, discountMarkUp5
                    );
                } else if ("2".equals(priceCode)) {

                    // Part-Customer Pricing
                    h2db.update("INSERT INTO product_customer_prices ("
                            + "  product_id,"
                            + "  customer_id,"
                            + "  discount_type,"
                            + "  BreakQty1, DiscountMarkupPriceRate1,"
                            + "  BreakQty2, DiscountMarkupPriceRate2,"
                            + "  BreakQty3, DiscountMarkupPriceRate3,"
                            + "  BreakQty4, DiscountMarkupPriceRate4,"
                            + "  BreakQty5, DiscountMarkupPriceRate5"
                            + ") VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                            itemCode, customerNo, pricingMethod,
                            breakQuantity1, discountMarkUp1,
                            breakQuantity2, discountMarkUp2,
                            breakQuantity3, discountMarkUp3,
                            breakQuantity4, discountMarkUp4,
                            breakQuantity5, discountMarkUp5
                    );
                } else {
                    throw new IllegalStateException("Unknown PriceCodeRecord in IMB_PriceCode: " + priceCode);
                }
                // Mas90 bug handling: https://github.com/pthiry/TurboInternational/issues/5#issuecomment-29331951
                h2db.update("UPDATE price_level_prices SET price_level = 2 WHERE price_level = ' '");
            }
        });

    }

}
