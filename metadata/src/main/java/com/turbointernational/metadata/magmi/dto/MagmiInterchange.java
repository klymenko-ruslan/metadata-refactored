
package com.turbointernational.metadata.magmi.dto;

/**
 * Used in a JPA "NEW" query.
 * @author jrodriguez
 */
public class MagmiInterchange {
    
    private final Long sku;
    
    private final Long interchangePartSku;
    
    private final String interchangePartNumber;
    
    private final Long interchangePartManufacturerId;

    public MagmiInterchange(Long sku, Long interchangePartSku, String interchangePartNumber, Long interchangePartManufacturerId) {
        this.sku = sku;
        this.interchangePartSku = interchangePartSku;
        this.interchangePartNumber = interchangePartNumber;
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

    public String getInterchangePartNumber() {
        return interchangePartNumber;
    }
    
    /**
     * @return the interchangePartManufacturerId
     */
    public Long getInterchangePartManufacturerId() {
        return interchangePartManufacturerId;
    }
    
   
}
