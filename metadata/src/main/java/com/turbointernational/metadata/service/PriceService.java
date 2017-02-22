package com.turbointernational.metadata.service;

import com.turbointernational.metadata.exception.PartNotFound;
import com.turbointernational.metadata.service.mas90.pricing.CalculatedPrice;
import com.turbointernational.metadata.service.mas90.pricing.Pricing;
import com.turbointernational.metadata.service.mas90.pricing.ProductPrices;
import com.turbointernational.metadata.web.dto.ProductPricesDto;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by dmytro.trunykov@zorallabs.com on 23.06.16.
 */
@Service
public class PriceService {

    private final static Logger log = LoggerFactory.getLogger(PriceService.class);

    @Autowired
    private DataSource dataSource;

    private JdbcTemplate jdbcTemplate;

    @Autowired
    @Qualifier("dataSourceMas90")
    private DataSource mssqlDataSource;

    private JdbcTemplate mssqldb;

    @PostConstruct
    public void init() {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.mssqldb = new JdbcTemplate(mssqlDataSource);
    }

    private static class PriceRow {

        private final String priceLevel;

        private final Pricing pricing;

        PriceRow(String priceLevel, Pricing pricing) {
            this.priceLevel = priceLevel;
            this.pricing = pricing;
        }

        public String getPriceLevel() {
            return priceLevel;
        }

        public Pricing getPricing() {
            return pricing;
        }

    }

    public List<ProductPricesDto> getProductsPrices(List<Long> partIds) {
        List<PriceRow> pricesRows = mssqldb.query(
                "select p.customerpricelevel as price_level, " +
                    "p.pricingmethod as discount_type, p.breakquantity1 as BreakQty1, " +
                    "p.breakquantity2 as BreakQty2, p.breakquantity3 as BreakQty3, " +
                    "p.breakquantity4 as BreakQty4, p.breakquantity5 as BreakQty5, " +
                    "p.discountmarkup1 as DiscountMarkupPriceRate1, " +
                    "p.discountmarkup2 as DiscountMarkupPriceRate2, " +
                    "p.discountmarkup3 as DiscountMarkupPriceRate3, " +
                    "p.discountmarkup4 as DiscountMarkupPriceRate4, " +
                    "p.discountmarkup5 as DiscountMarkupPriceRate5 " +
                    "from im_pricecode as p " +
                    "where p.pricecoderecord in ('', ' ', '0')",
                (rs, rowNum) -> {
                    String priceLevel = rs.getString("price_level");
                    // Mas90 bug handling: https://github.com/pthiry/TurboInternational/issues/5#issuecomment-29331951
                    if (StringUtils.isBlank(priceLevel)) {
                        priceLevel = "2";
                    }
                    Pricing pricing = Pricing.fromResultSet(rs);
                    return new PriceRow(priceLevel, pricing);
                }
        );

        List<ProductPricesDto> retVal = new ArrayList<>(partIds.size());
        for(Iterator<Long> iter = partIds.iterator(); iter.hasNext();) {
            Long partId = iter.next();
            try {
                ProductPrices pp = getProductPrices(partId, pricesRows);
                retVal.add(new ProductPricesDto(pp));
            } catch (PartNotFound e) {
                retVal.add(new ProductPricesDto(partId, e.getMessage()));
            }
        }
        return retVal;
    }

    private ProductPrices getProductPrices(Long partId, List<PriceRow> pricesRows) throws PartNotFound {
        String partNumber = jdbcTemplate.query("select manfr_part_num from part where id=?",
                new Object[] { partId }, rs -> { return rs.getString(1);});
        if (partNumber == null) {
            throw new PartNotFound(partId);
        }
        BigDecimal standardPrice;
        try {
            standardPrice = mssqldb.queryForObject("select standardunitprice from ci_item where itemcode=?",
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
        pricesRows.forEach(pr -> {
            List<CalculatedPrice> calculatedPrices = pr.getPricing().calculate(standardPrice);
            calculatedPrices.forEach(cp -> prices.put(pr.getPriceLevel(), cp.getPrice()));
        });
        ProductPrices retVal = new ProductPrices(partId, standardPrice, prices);
        return retVal;
    }



}
