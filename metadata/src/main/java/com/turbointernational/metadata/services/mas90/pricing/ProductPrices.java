package com.turbointernational.metadata.services.mas90.pricing;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.web.View;

import java.math.BigDecimal;
import java.util.Map;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.ALWAYS;
import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

/**
 * Created by dmytro.trunykov@zorallabs.com on 17.06.16.
 */
public class ProductPrices {

    @JsonView(View.Summary.class)
    private final Long partId;

    @JsonView(View.Summary.class)
    @JsonInclude(ALWAYS)
    private final BigDecimal standardPrice;

    @JsonView(View.Summary.class)
    @JsonInclude(ALWAYS)
    private final Map<String, BigDecimal> prices;  // price level => price

    @JsonView(View.Summary.class)
    @JsonInclude(NON_NULL)
    private final String warning;

    public ProductPrices(Long partId, String warning) {
        this(partId, null, null, warning);
    }

    public ProductPrices(Long partId, BigDecimal standardPrice,
                         Map<String, BigDecimal> prices) {
        this(partId, standardPrice, prices, null);
    }

    public ProductPrices(Long partId, BigDecimal standardPrice,
                         Map<String, BigDecimal> prices, String warning) {
        this.partId = partId;
        this.standardPrice = standardPrice;
        this.prices = prices;
        this.warning = warning;
    }
    public Long getPartId() {
        return partId;
    }

    public BigDecimal getStandardPrice() {
        return standardPrice;
    }

    public Map<String, BigDecimal> getPrices() {
        return prices;
    }

    public String getWarning() {
        return warning;
    }

}
