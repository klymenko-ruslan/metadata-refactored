package com.turbointernational.metadata.services.mas90;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by dmytro.trunykov on 12/30/15.
 */
public class MsSqlImpl extends AbstractMas90 {

    private JdbcTemplate mssqldb;

    public MsSqlImpl(DataSource dataSourceMas90) throws IOException {
        this.mssqldb = new JdbcTemplate(dataSourceMas90, true);
        super.init();
    }

    @Override
    protected void loadMas90Data() throws IOException {

        // Load customer data
        mssqldb.query("SELECT CUSTOMERNO, EMAILADDRESS FROM AR_CUSTOMER", rs -> {
            String customerNo = StringUtils.trim(rs.getString(1));
            String emailAddress = rs.getString(2);
            h2db.update("INSERT INTO customer (id, email) VALUES(?, ?)", customerNo, emailAddress);
        });

        // Load product data
        mssqldb.query("SELECT ITEMCODE, STANDARDUNITPRICE FROM CI_ITEM", rs -> {
            String itemCode = StringUtils.trim( rs.getString(1));
            String stdPrice = rs.getString(2);
            h2db.update("MERGE INTO product (id, price) VALUES(?, ?)", itemCode, stdPrice);
        });

        // Load pricing data
        mssqldb.query(
                "SELECT " +
                "   PRICECODERECORD, CUSTOMERPRICELEVEL, PRICINGMETHOD, BREAKQUANTITY1, BREAKQUANTITY2, " +
                "   BREAKQUANTITY3, BREAKQUANTITY4, BREAKQUANTITY5, DISCOUNTMARKUP1, DISCOUNTMARKUP2, " +
                "   DISCOUNTMARKUP3, DISCOUNTMARKUP4, DISCOUNTMARKUP5, ITEMCODE, CUSTOMERNO " +
                "FROM IM_PRICECODE", rs -> {
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
