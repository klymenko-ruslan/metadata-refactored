package com.turbointernational.metadata.services.mas90;

import com.turbointernational.metadata.services.Mas90Service;
import com.turbointernational.metadata.services.mas90.pricing.CalculatedPrice;
import com.turbointernational.metadata.services.mas90.pricing.ItemPricing;
import com.turbointernational.metadata.services.mas90.pricing.Pricing;
import org.springframework.jdbc.core.JdbcTemplate;
import org.h2.jdbcx.JdbcDataSource;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.*;
import java.util.logging.Logger;

/**
 * Created by dmytro.trunykov@zorallabs.com on 12/30/15.
 */
public abstract class AbstractMas90 implements Mas90Service.Mas90 {

    private final Map<String, Pricing> defaultPriceLevelPricing = new HashMap();

    private final SortedSet<String> priceLevels = new TreeSet();

    /**
     * In-memory H2 database.
     */
    protected final JdbcTemplate h2db = new JdbcTemplate(new JdbcDataSource() {

        {
            setURL("jdbc:h2:mem:mas90;DB_CLOSE_DELAY=-1");
        }

        @Override
        public Logger getParentLogger() throws SQLFeatureNotSupportedException {
            throw new SQLFeatureNotSupportedException();
        }

    }, false);

    protected AbstractMas90() {

    }

    protected abstract void loadMas90Data() throws IOException;

    protected void init() throws IOException {
        createH2Schema();
        loadMas90Data();
        loadPriceLevels();

        // Get the default price level pricings
        h2db.query("SELECT * FROM price_level_prices",
            new RowCallbackHandler() {
                @Override
                public void processRow(ResultSet rs) throws SQLException {
                    String priceLevel = rs.getString("price_level");
                    Pricing pricing = Pricing.fromResultSet(rs);
                    defaultPriceLevelPricing.put(priceLevel, pricing);
                }
            }
        );
    }

    private void loadPriceLevels() {
        // Get the price levels
        priceLevels.addAll(
            h2db.query("SELECT DISTINCT price_level FROM price_level_prices",
                new RowMapper<String>() {
                    @Override
                    public String mapRow(ResultSet rs, int rowNum) throws SQLException {
                        return rs.getString("price_level");
                    }
                }
            )
        );
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

    @Override
    public SortedSet<String> getPriceLevels() {
        return priceLevels;
    }

    @Override
    public Map<String, Pricing> getDefaultPriceLevelPricing() {
        return defaultPriceLevelPricing;
    }

    @Override
    public BigDecimal getStandardPrice(String itemNumber) {
        return h2db.queryForObject("SELECT price FROM product WHERE id = ?",
                BigDecimal.class, itemNumber);
    }

    @Override
    public ItemPricing getItemPricing(String itemNumber) {
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

    @Override
    public List<CalculatedPrice> calculatePriceLevelPrices(final String priceLevel, final ItemPricing item) {

        // Get the item-price-level pricing, if any
        Pricing pricing = item.getPriceLevelPricings().get(priceLevel);

        // Fall back to price-level pricing
        if (pricing == null) {
            pricing = defaultPriceLevelPricing.get(priceLevel);
        }

        return pricing.calculate(item.getStandardPrice());
    }

}
