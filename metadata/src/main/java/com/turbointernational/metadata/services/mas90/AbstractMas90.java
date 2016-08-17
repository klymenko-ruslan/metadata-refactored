package com.turbointernational.metadata.services.mas90;

import com.turbointernational.metadata.services.mas90.pricing.CalculatedPrice;
import com.turbointernational.metadata.services.mas90.pricing.ItemPricing;
import com.turbointernational.metadata.services.mas90.pricing.Pricing;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.h2.jdbcx.JdbcDataSource;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLFeatureNotSupportedException;
import java.util.*;
import java.util.logging.Logger;

/**
 * Created by dmytro.trunykov@zorallabs.com on 12/30/15.
 */
public abstract class AbstractMas90 implements Mas90 {

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
        return h2db.queryForObject("select price from product where id = ?",
                BigDecimal.class, itemNumber);
    }

    @Override
    public ItemPricing getItemPricing(String itemNumber) {
        BigDecimal standardPrice = getStandardPrice(itemNumber);
        final ItemPricing itemPricing = new ItemPricing(itemNumber, standardPrice);

        // Get the Product-Customer pricing
        h2db.query(
                "select c.email, p.* " +
                        "from product_customer_prices p join customer c on c.id = p.customer_id " +
                        "where p.product_id = ?",
                rs -> {
                    String email = rs.getString("email");
                    Pricing pricing = Pricing.fromResultSet(rs);
                    itemPricing.getCustomerPricings().put(email, pricing);
                },
                itemNumber);

        // Get the Product-PriceLevel pricing
        h2db.query("select * from product_price_level_prices where product_id = ?",
                rs -> {
                    String priceLevel = rs.getString("price_level");
                    Pricing pricing = Pricing.fromResultSet(rs);
                    itemPricing.getPriceLevelPricings().put(priceLevel, pricing);
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

    protected abstract void loadMas90Data() throws IOException;

    protected void init() throws IOException {
        createH2Schema();
        loadMas90Data();
        loadPriceLevels();

        // Get the default price level pricings
        h2db.query("select * from price_level_prices",
                rs -> {
                    String priceLevel = rs.getString("price_level");
                    Pricing pricing = Pricing.fromResultSet(rs);
                    defaultPriceLevelPricing.put(priceLevel, pricing);
                }
        );
    }

    protected void insertPrices(String priceCode, String customerPriceLevel, String pricingMethod,
                                String breakQuantity1, String breakQuantity2, String breakQuantity3,
                                String breakQuantity4, String breakQuantity5, String discountMarkUp1,
                                String discountMarkUp2, String discountMarkUp3, String discountMarkUp4,
                                String discountMarkUp5, String itemCode, String customerNo) {
        // Normalization.
        priceCode = ObjectUtils.toString(priceCode);
        itemCode = StringUtils.trim(itemCode);
        customerNo = StringUtils.trim(customerNo);

        if ("0".equals(priceCode)) {
            // Mas90 bug handling: https://github.com/pthiry/TurboInternational/issues/5#issuecomment-29331951
            if (ObjectUtils.toString(customerPriceLevel).trim().equals("")) {
                customerPriceLevel = "2";
            }
            // PriceLevel Pricing
            h2db.update("insert into price_level_prices ("
                            + "  price_level,"
                            + "  discount_type,"
                            + "  BreakQty1, DiscountMarkupPriceRate1,"
                            + "  BreakQty2, DiscountMarkupPriceRate2,"
                            + "  BreakQty3, DiscountMarkupPriceRate3,"
                            + "  BreakQty4, DiscountMarkupPriceRate4,"
                            + "  BreakQty5, DiscountMarkupPriceRate5"
                            + ") values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                    customerPriceLevel, pricingMethod,
                    breakQuantity1, discountMarkUp1,
                    breakQuantity2, discountMarkUp2,
                    breakQuantity3, discountMarkUp3,
                    breakQuantity4, discountMarkUp4,
                    breakQuantity5, discountMarkUp5
            );
        } else if ("1".equals(priceCode)) {
            // Part-PriceLevel Pricing
            h2db.update("insert into product_price_level_prices ("
                            + "  product_id,"
                            + "  price_level,"
                            + "  discount_type,"
                            + "  BreakQty1, DiscountMarkupPriceRate1,"
                            + "  BreakQty2, DiscountMarkupPriceRate2,"
                            + "  BreakQty3, DiscountMarkupPriceRate3,"
                            + "  BreakQty4, DiscountMarkupPriceRate4,"
                            + "  BreakQty5, DiscountMarkupPriceRate5"
                            + ") values(?, ?, ?,  ?, ?,  ?, ?,  ?, ?,  ?, ?,  ?, ?)",
                    itemCode, customerPriceLevel, pricingMethod,
                    breakQuantity1, discountMarkUp1,
                    breakQuantity2, discountMarkUp2,
                    breakQuantity3, discountMarkUp3,
                    breakQuantity4, discountMarkUp4,
                    breakQuantity5, discountMarkUp5
            );
        } else if ("2".equals(priceCode)) {

            // Part-Customer Pricing
            h2db.update("insert into product_customer_prices ("
                            + "  product_id,"
                            + "  customer_id,"
                            + "  discount_type,"
                            + "  BreakQty1, DiscountMarkupPriceRate1,"
                            + "  BreakQty2, DiscountMarkupPriceRate2,"
                            + "  BreakQty3, DiscountMarkupPriceRate3,"
                            + "  BreakQty4, DiscountMarkupPriceRate4,"
                            + "  BreakQty5, DiscountMarkupPriceRate5"
                            + ") values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
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
    }

    private void loadPriceLevels() {
        // Get the price levels
        priceLevels.addAll(
                h2db.query("select distinct price_level from price_level_prices",
                        (rs, rowNum) -> {
                            return rs.getString("price_level");
                        }
                )
        );
    }

    private void createH2Schema() {

        h2db.update("drop table customer if exists");
        h2db.update("create table customer (id varchar(20) not null, email varchar(255))");
        h2db.update("alter table customer add primary key (id)");

        h2db.update("drop table product if exists");
        h2db.execute("create table product (id varchar(20) not null, price decimal)");
        h2db.execute("alter table product add primary key (id)");

        // PriceLevel prices
        h2db.update("drop table price_level_prices if exists");
        h2db.execute("create table price_level_prices("
                + "price_level varchar(20) not null, "
                + "discount_type char(1) not null, "
                + "BreakQty1 integer, "
                + "DiscountMarkupPriceRate1 decimal, "
                + "BreakQty2 integer, "
                + "DiscountMarkupPriceRate2 decimal, "
                + "BreakQty3 integer, "
                + "DiscountMarkupPriceRate3 decimal, "
                + "BreakQty4 integer, "
                + "DiscountMarkupPriceRate4 decimal, "
                + "BreakQty5 integer, "
                + "DiscountMarkupPriceRate5 decimal)");
        h2db.execute("alter table price_level_prices add primary key(price_level)");

        // Product-PriceLevel
        h2db.update("drop table product_price_level_prices if exists");
        h2db.execute(
                "create table product_price_level_prices("
                        + "product_id varchar(20) not null, "
                        + "price_level varchar(20) not null, "
                        + "discount_type char(1) not null, "
                        + "BreakQty1 integer, "
                        + "DiscountMarkupPriceRate1 decimal, "
                        + "BreakQty2 integer, "
                        + "DiscountMarkupPriceRate2 decimal, "
                        + "BreakQty3 integer, "
                        + "DiscountMarkupPriceRate3 decimal, "
                        + "BreakQty4 integer, "
                        + "DiscountMarkupPriceRate4 decimal, "
                        + "BreakQty5 integer, "
                        + "DiscountMarkupPriceRate5 decimal)");
        h2db.execute("alter table product_price_level_prices add primary key(product_id, price_level)");

        // Product-Customer
        h2db.update("drop table product_customer_prices if exists");
        h2db.execute(
                "create table product_customer_prices("
                        + "product_id varchar(20) not null, "
                        + "customer_id varchar(20) not null, "
                        + "discount_type char(1) not null, "
                        + "BreakQty1 integer, "
                        + "DiscountMarkupPriceRate1 decimal, "
                        + "BreakQty2 integer, "
                        + "DiscountMarkupPriceRate2 decimal, "
                        + "BreakQty3 integer, "
                        + "DiscountMarkupPriceRate3 decimal, "
                        + "BreakQty4 integer, "
                        + "DiscountMarkupPriceRate4 decimal, "
                        + "BreakQty5 integer, "
                        + "DiscountMarkupPriceRate5 decimal)");
        h2db.execute("alter table product_customer_prices add primary key(product_id, customer_id)");
    }

}
