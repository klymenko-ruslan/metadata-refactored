
package com.turbointernational.metadata.util.dto;

/**
 * Used in a JPA "NEW" query.
 * @author jrodriguez
 */
public class MagmiInterchange {
    
    private final Long sku;
    
    private final Long interchangePartSku;
    
    private final Long interchangePartManufacturerId;

    public MagmiInterchange(Long sku, Long interchangePartSku, Long interchangePartManufacturerId) {
        this.sku = sku;
        this.interchangePartSku = interchangePartSku;
        this.interchangePartManufacturerId = interchangePartManufacturerId;
    }

    /**
     * @return the sku
     */
    public Long getSku() {
        return sku;
    }

    /**
     * @return the interchangePartSku
     */
    public Long getInterchangePartSku() {
        return interchangePartSku;
    }

    /**
     * @return the interchangePartManufacturerId
     */
    public Long getInterchangePartManufacturerId() {
        return interchangePartManufacturerId;
    }
    
   
}
