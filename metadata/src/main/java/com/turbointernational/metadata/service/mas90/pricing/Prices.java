package com.turbointernational.metadata.service.mas90.pricing;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.util.View;

import java.math.BigDecimal;
import java.util.Map;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.ALWAYS;
import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

/**
 * Created by dmytro.trunykov@zorallabs.com on 17.06.16.
 */
public class Prices {

    @JsonView(View.Summary.class)
    private final Long partId;

    @JsonView(View.Summary.class)
    private final String partNum;

    @JsonView(View.Summary.class)
    @JsonInclude(ALWAYS)
    private final BigDecimal standardPrice;

    @JsonView(View.Summary.class)
    @JsonInclude(ALWAYS)
    private final Map<String, BigDecimal> prices;  // price level => price

    @JsonView(View.Summary.class)
    @JsonInclude(NON_NULL)
    private final String warning;

    public Prices(Long partId, String partNum, String warning) {
        this(partId, partNum,null, null, warning);
    }

    public Prices(Long partId, String partNum, BigDecimal standardPrice,
                         Map<String, BigDecimal> prices) {
        this(partId, partNum, standardPrice, prices, null);
    }

    public Prices(Long partId, String partNum, BigDecimal standardPrice,
                         Map<String, BigDecimal> prices, String warning) {
        this.partId = partId;
        this.partNum = partNum;
        this.standardPrice = standardPrice;
        this.prices = prices;
        this.warning = warning;
    }
    public Long getPartId() {
        return partId;
    }

    public String getPartNum() {
        return partNum;
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
