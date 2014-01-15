
package com.turbointernational.metadata.util.dto;

/**
 * Used in a JPA "NEW" query.
 * @author jrodriguez
 */
public class MagmiInterchangePart {
    
    private final Long id;

    public Long getId() {
        return id;
    }
    
    private final Long manufacturerId;

    public Long getManufacturerId() {
        return manufacturerId;
    }

    public MagmiInterchangePart(Long id, Long manufacturerId) {
        this.id = id;
        this.manufacturerId = manufacturerId;
    }
    
   
}
