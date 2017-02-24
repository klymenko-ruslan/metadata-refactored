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
import org.springframework.beans.factory.annotation.Value;
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

    /**
     * The value specifies in millis a time-to-live in a cache an entry for the list of prices rows.
     *
     * @see #getPricesRows()
     */
    @Value("${prices.cache.ttl}")
    private long cacheTtl = 5 * 60000; // default 5 minutes

    private List<PriceRow> cachedPricesRows = null;

    private long pricesRowsInitedAt = 0L;

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

    public List<ProductPricesDto> getProductsPricesByIds(List<Long> partIds) {
        List<PriceRow> prows = getPricesRows();
        List<ProductPricesDto> retVal = new ArrayList<>(partIds.size());
        for(Iterator<Long> iter = partIds.iterator(); iter.hasNext();) {
            Long partId = iter.next();
            try {
                ProductPrices pp = getProductPrices(partId, prows);
                retVal.add(new ProductPricesDto(pp));
            } catch (PartNotFound e) {
                retVal.add(new ProductPricesDto(partId, null, e.getMessage()));
            }
        }
        return retVal;
    }

    public List<ProductPricesDto> getProductsPricesByNums(List<String> manfrPartNums) {
        List<PriceRow> prows = getPricesRows();
        List<ProductPricesDto> retVal = new ArrayList<>(manfrPartNums.size());
        for(Iterator<String> iter = manfrPartNums.iterator(); iter.hasNext();) {
            String partNum = iter.next();
            ProductPrices pp = getProductPrices(null, partNum, prows);
            retVal.add(new ProductPricesDto(pp));
        }
        return retVal;
    }

    private synchronized List<PriceRow> getPricesRows() {
        long now = System.currentTimeMillis();
        if (cachedPricesRows == null || now - pricesRowsInitedAt > cacheTtl) {
            cachedPricesRows = mssqldb.query(
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
            pricesRowsInitedAt = now;
        }
        return cachedPricesRows;
    }

    private ProductPrices getProductPrices(Long partId, List<PriceRow> pricesRows) throws PartNotFound {
        // Resolve partId to a manufacturer part number.
        String partNumber = jdbcTemplate.queryForObject("select manfr_part_num from part where id=?", String.class,
                partId);
        if (partNumber == null) {
            throw new PartNotFound(partId);
        }
        return getProductPrices(partId, pricesRows);

    }

    private ProductPrices getProductPrices(Long partId, String partNumber, List<PriceRow> pricesRows) {
        BigDecimal standardPrice;
        try {
            standardPrice = mssqldb.queryForObject("select standardunitprice from ci_item where itemcode=?",
                    BigDecimal.class, partNumber);
        } catch(EmptyResultDataAccessException e) {
            log.warn("Standard unit price for the part {} not found.", partNumber);
            return new ProductPrices(null, partNumber,"Standard unit price not found.");
        } catch(DataAccessException e) {
            log.warn("Calculation of a standard unit price for the part [{}] failed: {}",
                    partId, e.getMessage());
            return new ProductPrices(partId, partNumber, String.format("Calculation of a standard " +
                    "unit price failed: {%s}", e.getMessage()));
        }

        Map<String, BigDecimal> prices = new HashMap(50);
        pricesRows.forEach(pr -> {
            List<CalculatedPrice> calculatedPrices = pr.getPricing().calculate(standardPrice);
            calculatedPrices.forEach(cp -> prices.put(pr.getPriceLevel(), cp.getPrice()));
        });
        ProductPrices retVal = new ProductPrices(partId, partNumber, standardPrice, prices);
        return retVal;
    }

}
