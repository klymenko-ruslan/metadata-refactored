package com.turbointernational.metadata.services.mas90;

import com.turbointernational.metadata.domain.part.Part;
import com.turbointernational.metadata.domain.part.PartDao;
import com.turbointernational.metadata.exceptions.PartNotFound;
import com.turbointernational.metadata.services.mas90.pricing.CalculatedPrice;
import com.turbointernational.metadata.services.mas90.pricing.Pricing;
import com.turbointernational.metadata.services.mas90.pricing.ProductPrices;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dmytro.trunykov@zorallabs.com on 12/30/15.
 */
public class MsSqlImpl extends AbstractMas90 {

    private final static Logger log = LoggerFactory.getLogger(MsSqlImpl.class);

    @Autowired
    private PartDao partDao;

    private final JdbcTemplate mssqldb;

    private final static String PRICING_COLS = "p.pricingmethod as discount_type, "
        + "p.breakquantity1 as BreakQty1, "
        + "p.breakquantity2 as BreakQty2, "
        + "p.breakquantity3 as BreakQty3, "
        + "p.breakquantity4 as BreakQty4, "
        + "p.breakquantity5 as BreakQty5, "
        + "p.discountmarkup1 as DiscountMarkupPriceRate1, "
        + "p.discountmarkup2 as DiscountMarkupPriceRate2, "
        + "p.discountmarkup3 as DiscountMarkupPriceRate3, "
        + "p.discountmarkup4 as DiscountMarkupPriceRate4, "
        + "p.discountmarkup5 as DiscountMarkupPriceRate5";

    private final static String SQL_ITEM_PRICING = "select "
        + "p.customerpricelevel as price_level, " + PRICING_COLS + ", p.itemcode, p.customerno, c.emailaddress "
        + "from "
        + "im_pricecode as p left outer join ar_customer as c on p.customerno = c.customerno "
        + "where "
        + "p.pricecoderecord = ? "
        + "and p.itemcode = ?";

    public MsSqlImpl(PartDao partDao, DataSource dataSourceMas90) throws IOException {
        this.partDao = partDao;
        this.mssqldb = new JdbcTemplate(dataSourceMas90, true);
        super.init();
    }

    @Override
    public ProductPrices getProductPrices(Long partId) throws PartNotFound {
        Part part = partDao.findOne(partId);
        if (part == null) {
            throw new PartNotFound(partId);
        }
        String partNumber = part.getManufacturerPartNumber();
        BigDecimal standardPrice;
        try {
            standardPrice = mssqldb.queryForObject(
                    "select standardunitprice from ci_item where itemcode=?",
                    BigDecimal.class, partNumber);
        } catch(DataAccessException e) {
            log.warn("Product prices calculation failed for the part [{}]: {}", partId, e.getMessage());
            return new ProductPrices(partId);
        }

        Map<String, BigDecimal> prices = new HashMap(50);
        mssqldb.query("select p.customerpricelevel as price_level, " + PRICING_COLS + " from im_pricecode as p " +
                "where p.pricecoderecord in ('', ' ', '0')",
                rs -> {
                    String priceLevel = rs.getString("price_level");
                    // Mas90 bug handling: https://github.com/pthiry/TurboInternational/issues/5#issuecomment-29331951
                    if (StringUtils.isBlank(priceLevel)) {
                        priceLevel = "2";
                    }
                    Pricing pricing = Pricing.fromResultSet(rs);
                    List<CalculatedPrice> calculatedPrices = pricing.calculate(standardPrice);
                    for(CalculatedPrice cp: calculatedPrices) {
                        prices.put(priceLevel, cp.getPrice());
                    }
                });

        ProductPrices retVal = new ProductPrices(partId, standardPrice, prices);
        return retVal;
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
