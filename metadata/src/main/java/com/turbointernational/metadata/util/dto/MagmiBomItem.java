
package com.turbointernational.metadata.util.dto;

import org.apache.commons.lang3.ObjectUtils;

/**
 *
 * @author jrodriguez
 */
public class MagmiBomItem {
    
    private final Long sku;

    public Long getSku() {
        return sku;
    }
    
    private final Integer quantity;

    public Integer getQuantity() {
        return quantity;
    }
    
    private final Long altSku;

    public Long getAltSku() {
        return altSku;
    }
    
    private final Long altSkuMfrId;

    public Long getAltSkuMfrId() {
        return altSkuMfrId;
    }
    
    private final Long intSku;

    public Long getIntSku() {
        return intSku;
    }
    
    private final Long intSkuMfrId;

    public Long getIntSkuMfrId() {
        return intSkuMfrId;
    }

    public MagmiBomItem(Long sku, Integer quantity, Long altSku, Long altSkuMfrId, Long intSku, Long intSkuMfrId) {
        this.sku = sku;
        this.quantity = quantity;
        this.altSku = altSku;
        this.altSkuMfrId = altSkuMfrId;
        this.intSku = intSku;
        this.intSkuMfrId = intSkuMfrId;
    }
}
