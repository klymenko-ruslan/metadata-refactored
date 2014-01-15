
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
    
    private final Long tiPartSku;

    public Long getTiPartSku() {
        return tiPartSku;
    }

    public MagmiBomItem(Long sku, Integer quantity, Long altTiPartSku, Long intTiPartSku) {
        this.sku = sku;
        this.quantity = quantity;
        this.tiPartSku = ObjectUtils.firstNonNull(altTiPartSku, intTiPartSku);
    }
    
   
}
