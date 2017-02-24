package com.turbointernational.metadata.web.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.service.mas90.pricing.ProductPrices;
import com.turbointernational.metadata.util.View;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

/**
 * Created by dmytro.trunykov@zorallabs.com on 17.06.16.
 */
public class ProductPricesDto extends ProductPrices {

    @JsonView(View.Summary.class)
    @JsonInclude(NON_NULL)
    private final String error;

    public ProductPricesDto(Long partId, String partNum, String error) {
        super(partId, partNum, null);
        this.error = error;
    }

    public ProductPricesDto(ProductPrices pp) {
        super(pp.getPartId(), pp.getPartNum(), pp.getStandardPrice(), pp.getPrices(), pp.getWarning());
        this.error = null;
    }

    public String getError() {
        return error;
    }

}
