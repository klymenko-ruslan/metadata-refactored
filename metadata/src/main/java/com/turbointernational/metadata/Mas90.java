package com.turbointernational.metadata;

import com.google.common.base.Function;
import java.lang.String;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.ArgumentPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author jrodriguez
 */
public class Mas90 {
    
    @Autowired(required=true)
    JdbcTemplate db;
    
    /**
     * test the pricing algorithm
     */
    @RequestMapping("/mas90/test")
    @ResponseBody
    public String test() {
        String company = "4WHEEL";
        String partNumber = "1-A-0577";
        String currency = "EUR";
        
        return partPriceCalc(company, partNumber, currency).toString();
    }
    
    /**
     * Top level function to calculate a price for a user and currency.
     * @param company
     * @param partNumber
     * @param currency
     * @return 
     */
    public Double partPriceCalc(final String company, final String partNumber, final String currency) {
        
        Double partPriceCalc = partPriceLevelCalc(partNumber, currency, company);
        if (partPriceCalc != null) {
            return partPriceCalc;
        }
        
        // The rest of this function is based on default user price levels
        // calculate standard price based on provided currency
        final Double stdPrice = partStdPrice(partNumber, currency);
        
        // calc discounted price based on if foreign price exists or not4
        if (!"USD".equals(currency) && stdPrice != null) {
            
            // no price level or exch rate is used in the case where (1) foreign price exists and (2) foreign part price level does not exist
            return stdPrice;
        } else {
            // use USD if foreign currency price does not exist and apply exchange rate

            // return discount parameters for default user price level
            String query =
                      "SELECT\n"
                    + "  PC.Method as discountType,\n"
                    + "  PC.DiscountMarkupPriceRate1\n"
                    + "FROM IMB_PriceCode AS PC\n"
                    + "WHERE\n"
                    + "  PC.PriceCode='0001'\n"
                    + "  AND PC.CustomerPriceLevel= ?";

            Double discountedPrice = db.queryForObject(query, new RowMapper<Double>() {
                @Override
                public Double mapRow(ResultSet rs, int rowNum) throws SQLException {
                    String discountType = rs.getString("discountType");
                    Double DiscountMarkupPriceRate1 = rs.getDouble("DiscountMarkupPriceRate1");
                    return discountPriceCalc(discountType, stdPrice, DiscountMarkupPriceRate1);
                }
            }, Double.class);
            
            return discountedPrice * Exch_Rate(currency);
        }
    }
    
    // determines price based on price level defined for specific part and currency
    private Double partPriceLevelCalc(final String partNumber, final String currency, final String user) {
 
        // check to see if price is calcualted based on customer part parameters
        Double Part_PL_Calc = partUserCalc(partNumber, user, currency);
        if (Part_PL_Calc != null) {
            return Part_PL_Calc;
        }
        
        // The rest of this function is based on part price levels
        String part_price_level = defaultUserPriceLevel(user);
    
        String qryStr =
                  "SELECT\n"
                + "  FPC.ItemMethod AS discountType,\n"
                + "  FPC.DiscountMarkupPriceRate1\n"
                + "FROM IM_MB_MCPriceCode AS FPC\n"
                + "WHERE"
                + "  FPC.ItemNumber = ?\n"
                + "  AND FPC.CurrencyCode= ?\n"
                + "  AND FPC.ItemCustomerPriceLevel = ?\n"
                + "  AND FPC.PriceCodeRecord = '1'\n"
                + "  AND FPC.CurrencyCode <> 'USD'";
        
        return db.queryForObject(qryStr, new RowMapper<Double>() {
            @Override
            public Double mapRow(ResultSet rs, int rowNum) throws SQLException {
                String discountType = rs.getString("discountType");
                
                Double DiscountMarkupPriceRate1 = rs.getDouble("DiscountMarkupPriceRate1");
                
                Double partStandardPrice = partStdPrice(partNumber, currency);
                
                return discountPriceCalc(discountType, partStandardPrice, DiscountMarkupPriceRate1);
            }
        }, partNumber, currency, part_price_level);
    }
 
    /**
     * Calculates price if a customized price is defined for a customer on a part and currency. Does not return any values if curency is USD
     */
    public Double partUserCalc(final String partNumber, final String company, final String currency) {
    
        String query =
                  "SELECT\n"
                + "  FPC.ItemCustomerRecordMethod,\n"
                + "  FPC.DiscountMarkupPriceRate1\n"
                + "FROM IM_MB_MCPriceCode AS FPC\n"
                + "WHERE\n"
                + "  FPC.ItemNumber = ?\n"
                + "  AND FPC.CurrencyCode = ?\n"
                + "  AND FPC.PriceCodeRecord = '2'\n"
                + "  AND FPC.CurrencyCode <> 'USD'\n"
                + "  AND FPC.CustomerNumber = ?";
        
        return db.queryForObject(query, new RowMapper<Double>() {
            @Override
            public Double mapRow(ResultSet rs, int rowNum) throws SQLException {
                String discountType = rs.getString("discountType");
                Double DiscountMarkupPriceRate1 = rs.getDouble("DiscountMarkupPriceRate1");
                return discountPriceCalc(discountType, partStdPrice(partNumber, currency), DiscountMarkupPriceRate1);
            }
        }, partNumber, currency, company);
    }

    /**
     * calculates the price based on the type of discount
     */
    public Double discountPriceCalc(String discountType, Double price, Double discountAmount) {
        switch (discountType) {
            case "D":
                return price * (1 - discountAmount / 100);
            case "P":
                return price - discountAmount;
            default:
                return null;
        }
    }
    
    /**
     * return standard price for part starting with foreign currency then USD
     */
    public Double partStdPrice(String partNumber, String currency) {
        if ("USD".equals(currency)) {

            // use IM1_InventoryMasterfile  table
            String query =
                      "SELECT IM1.StdPrice\n"
                    + "FROM IM1_InventoryMasterfile AS IM1\n"
                    + "WHERE IM1.ItemNumber = ?";
            return db.queryForObject(query, Double.class, partNumber);
        } else {
            
            // use IM_M1_MCItemForeignPriceCost table
            String query =
                      "SELECT\n"
                    + "  FPC.ForeignPrice\n"
                    + "FROM IM_M1_MCItemForeignPriceCost AS FPC\n"
                    + "WHERE\n"
                    + "  FPC.CurrencyCode = ?\n"
                    + "  AND FPC.ItemNumber = ?\n"
                    + "  AND FPC.UseForeignPriceInSO = 'Y'";
            
            return db.queryForObject(query, Double.class, currency, partNumber);
        }
    }
    
    /**
     * returns the default Price Level for user
     * @param company
     * @return 
     */
    public String defaultUserPriceLevel(String company) {
        String query =
                  "SELECT c.PriceLevel\n"
                + "FROM AR_Customer AS c\n"
                + "WHERE\n"
                + "  c.CustomerNo= ?\n"
                + "  AND c.PriceLevel IS NOT NULL";
        
        return db.queryForObject(query, String.class, company);
    }
    
    /**
     * returns the exchange rate for desired currency in ratio to USD.  Eg.  1 USD = Exch_Rate * EUR
     * @param currency
     * @return 
     */
    public Double Exch_Rate(String currency) {

        if ("USD".equals(currency)) {
            return 1.0;
        }
        
        String query =
                  "SELECT CX1.AR_SO_ExchRate\n"
                + "FROM\n"
                + "  Foreign_exch_1 AS FE_1 \n"
                + "  INNER JOIN CX1_CurrencyMasterfile AS CX1 ON\n"
                + "    FE_1.CurrencyCode = CX1.CurrencyCode AND FE_1.MaxOfDate = CX1.Date\n"
                + "WHERE\n"
                + "  FE_1.CurrencyCode = ?";
        
        return db.queryForObject(query, Double.class, currency);
    }
}
