package com.turbointernational.metadata.services;

import com.turbointernational.metadata.domain.part.Part;
import com.turbointernational.metadata.domain.part.PartDao;
import com.turbointernational.metadata.exceptions.PartNotFound;
import com.turbointernational.metadata.services.mas90.pricing.CalculatedPrice;
import com.turbointernational.metadata.services.mas90.pricing.Pricing;
import com.turbointernational.metadata.services.mas90.pricing.ProductPrices;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.support.broadcast.node.TransportBroadcastByNodeAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dmytro.trunykov@zorallabs.com on 23.06.16.
 */
@Service
public class PriceService {

    private final static Logger log = LoggerFactory.getLogger(PriceService.class);

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

    @Autowired
    private PartDao partDao;

    private JdbcTemplate mssqldb;

    @Autowired
    @Qualifier("dataSourceMas90")
    public void setDataSource(DataSource dataSource) {
        this.mssqldb = new JdbcTemplate(dataSource);
    }

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
        } catch(EmptyResultDataAccessException e) {
            log.warn("Standard unit price for the part [{}] not found.", partId);
            return new ProductPrices(partId, "Standard unit price not found.");
        } catch(DataAccessException e) {
            log.warn("Calculation of a standard unit price for the part [{}] failed: {}",
                    partId, e.getMessage());
            return new ProductPrices(partId, String.format("Calculation of a standard " +
                    "unit price failed: {%s}", e.getMessage()));
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

}
