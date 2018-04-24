package com.turbointernational.metadata.service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

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
import com.turbointernational.metadata.service.mas90.pricing.Prices;
import com.turbointernational.metadata.service.mas90.pricing.Pricing;
import com.turbointernational.metadata.web.dto.ProductPrices;

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

    public List<ProductPrices> getProductsPricesByNums(List<String> manfrPartNums) {
        List<PriceRow> prows = getPricesRows();
        List<ProductPrices> retVal = new ArrayList<>(manfrPartNums.size());
        for (Iterator<String> iter = manfrPartNums.iterator(); iter.hasNext();) {
            String partNum = iter.next();
            Prices pp = getProductPrices(null, partNum, prows);
            retVal.add(roundProductPrices(new ProductPrices(pp)));
        }
        return retVal;
    }

    public List<ProductPrices> getProductsPricesByIds(List<Long> partIds) {
        List<PriceRow> prows = getPricesRows();
        List<ProductPrices> retVal = new ArrayList<>(partIds.size());
        for (Iterator<Long> iter = partIds.iterator(); iter.hasNext();) {
            Long partId = iter.next();
            ProductPrices dto = getProductPricesById(partId, prows);
            retVal.add(roundProductPrices(dto));
        }
        return retVal;
    }

    public ProductPrices getProductPricesById(Long partId) {
        List<PriceRow> prows = getPricesRows();
        return roundProductPrices(getProductPricesById(partId, prows));
    }

    private ProductPrices getProductPricesById(Long partId, List<PriceRow> prows) {
        ProductPrices retVal;
        try {
            // Resolve partId to a manufacturer part number.
            String partNumber = jdbcTemplate.queryForObject("select manfr_part_num from part where id=?", String.class,
                    partId);
            if (partNumber == null) {
                throw new PartNotFound(partId);
            }
            Prices pp = getProductPrices(partId, partNumber, prows);
            retVal = new ProductPrices(pp);
        } catch (PartNotFound e) {
            retVal = new ProductPrices(partId, null, e.getMessage());
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
                    + "p.discountmarkup5 as DiscountMarkupPriceRate5 "
                    + "from im_pricecode as p "
                    + "where p.pricecoderecord in ('', ' ', '0')", (rs, rowNum) -> {
                        String priceLevel = rs.getString("price_level");
                        Pricing pricing = Pricing.fromResultSet(rs);
                        return new PriceRow(priceLevel, pricing);
                    });
            pricesRowsInitedAt = now;
        } else {
            log.debug("Used the cached cachedPricesRows.");
        }
        return cachedPricesRows;
    }

    private Prices getProductPrices(Long partId, String partNumber, List<PriceRow> pricesRows) {
        BigDecimal standardPrice;
        try {
            standardPrice = mssqldb.queryForObject("select standardunitprice from ci_item where itemcode=?",
                    BigDecimal.class, partNumber);
        } catch (EmptyResultDataAccessException e) {
            log.debug("Standard unit price for the part (p/n '{}') not found.", partNumber);
            return new Prices(partId, partNumber, "Standard unit price not found.");
        } catch (DataAccessException e) {
            log.warn("Calculation of a standard unit price for the part (p/n '{}') failed: {}", partNumber,
                    e.getMessage());
            return new Prices(partId, partNumber,
                    String.format("Calculation of a standard " + "unit price failed: {%s}", e.getMessage()));
        }
        Map<String, BigDecimal> prices = new HashMap<>(50);
        pricesRows.forEach(pr -> {
            List<CalculatedPrice> calculatedPrices = pr.getPricing().calculate(standardPrice);
            calculatedPrices.forEach(cp -> prices.put(pr.getPriceLevel(), cp.getPrice()));
        });
        Prices retVal = new Prices(partId, partNumber, standardPrice, prices);
        return retVal;
    }

    private ProductPrices roundProductPrices(ProductPrices productPrices) {
        return new ProductPrices(new Prices(productPrices.getPartId(),
                productPrices.getPartNum(),
                productPrices.getStandardPrice().setScale(2, BigDecimal.ROUND_HALF_UP),
                productPrices.getPrices()
                             .entrySet()
                             .stream()
                             .map(it -> new AbstractMap.SimpleEntry<>(it.getKey(), it.getValue().setScale(2, BigDecimal.ROUND_HALF_UP)))
                             .collect(Collectors.toMap(entry -> entry.getKey(), entry -> entry.getValue()))));
    }

}
