package com.turbointernational.metadata.mas90;

import com.healthmarketscience.jackcess.Database;
import com.healthmarketscience.jackcess.DatabaseBuilder;
import com.healthmarketscience.jackcess.Row;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.h2.jdbcx.JdbcDataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;

/**
 *
 * @author jrodriguez
 */
public class Mas90Prices {
    
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
        
        Mas90Prices.priceLevels = Collections.unmodifiableSortedSet(tempPriceLevels);
    }

    public static SortedSet<String> getPriceLevels() {
        return priceLevels;
    }

    /**
     * In-memory H2 database.
     */
    private final JdbcTemplate h2db = new JdbcTemplate(new JdbcDataSource() {
        {
            setURL("jdbc:h2:mem:mas90;DB_CLOSE_DELAY=-1");
        }
    }, false);
    
    private final Database mas90Db;
    
    private final Map<String, Pricing> defaultPriceLevelPricing = new HashMap();

    public Map<String, Pricing> getDefaultPriceLevelPricing() {
        return defaultPriceLevelPricing;
    }

    public Mas90Prices(File mas90DbFile) throws IOException {

        // Open the mas90 database
        mas90Db = DatabaseBuilder.open(mas90DbFile);
        
        createH2Schema();
        loadMas90Data();
        
        // Get the default price level pricings
        h2db.query("SELECT * FROM price_level_prices",
            new RowCallbackHandler() {
                @Override
                public void processRow(ResultSet rs) throws SQLException {
                    String priceLevel = rs.getString("price_level");
                    Pricing pricing = Pricing.fromResultSet(rs);
                    
                    defaultPriceLevelPricing.put(priceLevel, pricing);
                }
            });
        
    }
    
    private void createH2Schema() {
        
        h2db.update("DROP TABLE customer IF EXISTS");
        h2db.update("CREATE TABLE customer (id VARCHAR(20) NOT NULL, email VARCHAR(255))");
        h2db.update("ALTER TABLE customer ADD PRIMARY KEY (id)");
        
        h2db.update("DROP TABLE product IF EXISTS");
        h2db.execute("CREATE TABLE product (id VARCHAR(20) NOT NULL, price DECIMAL)");
        h2db.execute("ALTER TABLE product ADD PRIMARY KEY (id)");
        
        // PriceLevel prices
        h2db.update("DROP TABLE price_level_prices IF EXISTS");
        h2db.execute(
                  "CREATE TABLE price_level_prices (\n"
                + "  price_level VARCHAR(20) NOT NULL,\n"
                + "  discount_type CHAR(1) NOT NULL,\n"
                + "  BreakQty1 INTEGER,\n"
                + "  DiscountMarkupPriceRate1 DECIMAL,\n"
                + "  BreakQty2 INTEGER,\n"
                + "  DiscountMarkupPriceRate2 DECIMAL,\n"
                + "  BreakQty3 INTEGER,\n"
                + "  DiscountMarkupPriceRate3 DECIMAL,\n"
                + "  BreakQty4 INTEGER,\n"
                + "  DiscountMarkupPriceRate4 DECIMAL,\n"
                + "  BreakQty5 INTEGER,\n"
                + "  DiscountMarkupPriceRate5 DECIMAL\n"
                + ")");
        h2db.execute("ALTER TABLE price_level_prices ADD PRIMARY KEY (price_level)");
        
        // Product-PriceLevel
        h2db.update("DROP TABLE product_price_level_prices IF EXISTS");
        h2db.execute(
                  "CREATE TABLE product_price_level_prices (\n"
                + "  product_id VARCHAR(20) NOT NULL,\n"
                + "  price_level VARCHAR(20) NOT NULL,\n"
                + "  discount_type CHAR(1) NOT NULL,\n"
                + "  BreakQty1 INTEGER,\n"
                + "  DiscountMarkupPriceRate1 DECIMAL,\n"
                + "  BreakQty2 INTEGER,\n"
                + "  DiscountMarkupPriceRate2 DECIMAL,\n"
                + "  BreakQty3 INTEGER,\n"
                + "  DiscountMarkupPriceRate3 DECIMAL,\n"
                + "  BreakQty4 INTEGER,\n"
                + "  DiscountMarkupPriceRate4 DECIMAL,\n"
                + "  BreakQty5 INTEGER,\n"
                + "  DiscountMarkupPriceRate5 DECIMAL\n"
                + ")");
        h2db.execute("ALTER TABLE product_price_level_prices ADD PRIMARY KEY (product_id, price_level)");
        
        // Product-Customer
        h2db.update("DROP TABLE product_customer_prices IF EXISTS");
        h2db.execute(
                  "CREATE TABLE product_customer_prices (\n"
                + "  product_id VARCHAR(20) NOT NULL,\n"
                + "  customer_id VARCHAR(20) NOT NULL,\n"
                + "  discount_type CHAR(1) NOT NULL,\n"
                + "  BreakQty1 INTEGER,\n"
                + "  DiscountMarkupPriceRate1 DECIMAL,\n"
                + "  BreakQty2 INTEGER,\n"
                + "  DiscountMarkupPriceRate2 DECIMAL,\n"
                + "  BreakQty3 INTEGER,\n"
                + "  DiscountMarkupPriceRate3 DECIMAL,\n"
                + "  BreakQty4 INTEGER,\n"
                + "  DiscountMarkupPriceRate4 DECIMAL,\n"
                + "  BreakQty5 INTEGER,\n"
                + "  DiscountMarkupPriceRate5 DECIMAL\n"
                + ")");
        h2db.execute("ALTER TABLE product_customer_prices ADD PRIMARY KEY (product_id, customer_id)");
    }
    
    private void loadMas90Data() throws IOException {
        
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
            
            // Mas90 bug handling
            h2db.update("UPDATE price_level_prices SET price_level = 2 WHERE price_level = ' '");
        }
    }
    
    public BigDecimal getStandardPrice(String itemNumber) throws IOException {
        return h2db.queryForObject("SELECT price FROM product WHERE id = ?",
                BigDecimal.class, itemNumber);
    }

    public ItemPricing getItemPricing(String itemNumber) throws IOException {
        final ItemPricing itemPricing = new ItemPricing(itemNumber, getStandardPrice(itemNumber));
        
        // Get the Product-Customer pricing
        h2db.query(
                  "SELECT\n"
                + "  c.email,\n"
                + "  p.*\n"
                + "FROM\n"
                + "  product_customer_prices p\n"
                + "  JOIN customer c ON c.id = p.customer_id\n"
                + "WHERE p.product_id = ?",
            new RowCallbackHandler() {
                @Override
                public void processRow(ResultSet rs) throws SQLException {
                    String email = rs.getString("email");
                    Pricing pricing = Pricing.fromResultSet(rs);
                    itemPricing.getCustomerPricings().put(email, pricing);
                }
            },
            itemNumber);
        
        // Get the Product-PriceLevel pricing
        h2db.query("SELECT * FROM product_price_level_prices WHERE product_id = ?",
            new RowCallbackHandler() {
                @Override
                public void processRow(ResultSet rs) throws SQLException {
                    String priceLevel = rs.getString("price_level");
                    Pricing pricing = Pricing.fromResultSet(rs);
                    
                    itemPricing.getPriceLevelPricings().put(priceLevel, pricing);
                }
            },
            itemNumber);
        
        return itemPricing;
    }
    
    public List<CalculatedPrice> calculatePriceLevelPrices(final String priceLevel, final ItemPricing item) {
        
        // Get the item-price-level pricing, if any
        Pricing pricing = item.getPriceLevelPricings().get(priceLevel);
        
        // Fall back to price-level pricing
        if (pricing == null) {
            pricing = defaultPriceLevelPricing.get(priceLevel);
        }
        
        return pricing.calculate(item.getStandardPrice());
    }
    
    public static void main(String[] args) throws Exception {
        Mas90Prices instance = new Mas90Prices(new File("/home/jrodriguez/Downloads/MAS90_pricing_model.accdb"));
        for (Map<String, Object> row : instance.h2db.queryForList("SELECT * FROM customer")) {
            System.out.println("Customer: " + row);
        }
        
        for (Map<String, Object> row : instance.h2db.queryForList(
                  "SELECT\n"
                + "  c.email,\n"
                + "  p.*\n"
                + "FROM\n"
                + "  product_customer_prices p\n"
                + "  JOIN customer c ON c.id = p.customer_id\n")) {
            System.out.println("Row: " + row);
        }
        
        ItemPricing itemPricing = instance.getItemPricing("1-F-5066");
        System.out.println(itemPricing);
    }
}
