package com.turbointernational.metadata.services.mas90;

import org.apache.commons.lang.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.io.IOException;

/**
 * Created by dmytro.trunykov@zorallabs.com on 12/30/15.
 */
public class MsSqlImpl extends AbstractMas90 {

    private final JdbcTemplate mssqldb;

    public MsSqlImpl(DataSource dataSourceMas90) throws IOException {
        this.mssqldb = new JdbcTemplate(dataSourceMas90, true);
        super.init();
    }


    @Override
    protected void loadMas90Data() throws IOException {

        // Load customer data
        mssqldb.query("select customerno, emailaddress from ar_customer", rs -> {
            String customerNo = StringUtils.trim(rs.getString(1));
            String emailAddress = rs.getString(2);
            h2db.update("insert into customer (id, email) values(?, ?)", customerNo, emailAddress);
        });

        // Load product data
        mssqldb.query("select itemcode, standardunitprice from ci_item", rs -> {
            String itemCode = StringUtils.trim( rs.getString(1));
            String stdPrice = rs.getString(2);
            h2db.update("merge into product (id, price) values(?, ?)", itemCode, stdPrice);
        });

        // Load pricing data
        mssqldb.query(
                "select " +
                "   pricecoderecord, customerpricelevel, pricingmethod, breakquantity1, breakquantity2, " +
                "   breakquantity3, breakquantity4, breakquantity5, discountmarkup1, discountmarkup2, " +
                "   discountmarkup3, discountmarkup4, discountmarkup5, itemcode, customerno " +
                "from im_pricecode", rs -> {
                    String priceCode = rs.getString(1);
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
                    String itemCode = rs.getString(14);
                    String customerNo = rs.getString(15);
                    insertPrices(priceCode, customerPriceLevel, pricingMethod, breakQuantity1, breakQuantity2,
                            breakQuantity3, breakQuantity4, breakQuantity5, discountMarkUp1, discountMarkUp2,
                            discountMarkUp3, discountMarkUp4, discountMarkUp5, itemCode, customerNo);
                });

    }

}
