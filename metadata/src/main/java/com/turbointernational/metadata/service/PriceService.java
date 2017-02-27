package com.turbointernational.metadata.service;

import static org.apache.commons.lang3.StringUtils.isBlank;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.turbointernational.metadata.exception.PartNotFound;
import com.turbointernational.metadata.service.mas90.pricing.CalculatedPrice;
import com.turbointernational.metadata.service.mas90.pricing.Pricing;
import com.turbointernational.metadata.service.mas90.pricing.ProductPrices;
import com.turbointernational.metadata.web.dto.ProductPricesDto;

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
     * The value specifies in millis a time-to-live in a cache an entry for the
     * list of prices rows.
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

    public List<ProductPricesDto> getProductsPricesByNums(List<String> manfrPartNums) {
        List<PriceRow> prows = getPricesRows();
        List<ProductPricesDto> retVal = new ArrayList<>(manfrPartNums.size());
        for (Iterator<String> iter = manfrPartNums.iterator(); iter.hasNext();) {
            String partNum = iter.next();
            ProductPrices pp = getProductPrices(null, partNum, prows);
            retVal.add(new ProductPricesDto(pp));
        }
        return retVal;
    }

    public List<ProductPricesDto> getProductsPricesByIds(List<Long> partIds) {
        List<PriceRow> prows = getPricesRows();
        List<ProductPricesDto> retVal = new ArrayList<>(partIds.size());
        for (Iterator<Long> iter = partIds.iterator(); iter.hasNext();) {
            Long partId = iter.next();
            ProductPricesDto dto = getProductPricesById(partId, prows);
            retVal.add(dto);
        }
        return retVal;
    }

    public ProductPricesDto getProductPricesById(Long partId) {
        List<PriceRow> prows = getPricesRows();
        return getProductPricesById(partId, prows);
    }

    private ProductPricesDto getProductPricesById(Long partId, List<PriceRow> prows) {
        ProductPricesDto retVal;
        try {
            // Resolve partId to a manufacturer part number.
            String partNumber = jdbcTemplate.queryForObject("select manfr_part_num from part where id=?", String.class,
                    partId);
            if (partNumber == null) {
                throw new PartNotFound(partId);
            }
            ProductPrices pp = getProductPrices(partId, partNumber, prows);
            retVal = new ProductPricesDto(pp);
        } catch (PartNotFound e) {
            retVal = new ProductPricesDto(partId, null, e.getMessage());
        }
        return retVal;
    }

    private synchronized List<PriceRow> getPricesRows() {
        long now = System.currentTimeMillis();
        long cacheAge = now - pricesRowsInitedAt;
        if (cachedPricesRows == null || cacheAge > cacheTtl) {
            log.debug("The cachedPricesRows is beign initialized.");
            cachedPricesRows = mssqldb.query("select p.customerpricelevel as price_level, "
                    + "p.pricingmethod as discount_type, p.breakquantity1 as BreakQty1, "
                    + "p.breakquantity2 as BreakQty2, p.breakquantity3 as BreakQty3, "
                    + "p.breakquantity4 as BreakQty4, p.breakquantity5 as BreakQty5, "
                    + "p.discountmarkup1 as DiscountMarkupPriceRate1, "
                    + "p.discountmarkup2 as DiscountMarkupPriceRate2, "
                    + "p.discountmarkup3 as DiscountMarkupPriceRate3, "
                    + "p.discountmarkup4 as DiscountMarkupPriceRate4, "
                    + "p.discountmarkup5 as DiscountMarkupPriceRate5 " + "from im_pricecode as p "
                    + "where p.pricecoderecord in ('', ' ', '0')", (rs, rowNum) -> {
                        String priceLevel = rs.getString("price_level");
                        // Mas90 bug handling:
                        // https://github.com/pthiry/TurboInternational/issues/5#issuecomment-29331951
                        if (isBlank(priceLevel)) {
                            priceLevel = "2";
                        }
                        Pricing pricing = Pricing.fromResultSet(rs);
                        return new PriceRow(priceLevel, pricing);
                    });
            pricesRowsInitedAt = now;
        } else {
            log.debug("Used the cached cachedPricesRows.");
        }
        return cachedPricesRows;
    }

    private ProductPrices getProductPrices(Long partId, String partNumber, List<PriceRow> pricesRows) {
        BigDecimal standardPrice;
        try {
            standardPrice = mssqldb.queryForObject("select standardunitprice from ci_item where itemcode=?",
                    BigDecimal.class, partNumber);
        } catch (EmptyResultDataAccessException e) {
            log.warn("Standard unit price for the part (p/n '{}') not found.", partNumber);
            return new ProductPrices(null, partNumber, "Standard unit price not found.");
        } catch (DataAccessException e) {
            log.warn("Calculation of a standard unit price for the part (p/n '{}') failed: {}", partNumber,
                    e.getMessage());
            return new ProductPrices(partId, partNumber,
                    String.format("Calculation of a standard " + "unit price failed: {%s}", e.getMessage()));
        }
        Map<String, BigDecimal> prices = new HashMap<>(50);
        pricesRows.forEach(pr -> {
            List<CalculatedPrice> calculatedPrices = pr.getPricing().calculate(standardPrice);
            calculatedPrices.forEach(cp -> prices.put(pr.getPriceLevel(), cp.getPrice()));
        });
        ProductPrices retVal = new ProductPrices(partId, partNumber, standardPrice, prices);
        return retVal;
    }

}
