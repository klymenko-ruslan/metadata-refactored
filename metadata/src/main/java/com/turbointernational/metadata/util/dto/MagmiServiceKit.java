
package com.turbointernational.metadata.util.dto;

/**
 * Used in a JPA "NEW" query.
 * @author jrodriguez
 */
public class MagmiServiceKit {
    
    public final  Long sku;
    
    public final  Long kitSku;
    
    public final  String kitPartNumber;
    
    public final  String description;
    
    public final  Long tiKitSku;
    
    public final  String tiKitPartNumber;

    public MagmiServiceKit(Long sku, Long kitSku, String kitPartNumber, String description, Long tiKitSku, String tiKitPartNumber) {
        this.sku = sku;
        this.kitSku = kitSku;
        this.kitPartNumber = kitPartNumber;
        this.description = description;
        this.tiKitSku = tiKitSku;
        this.tiKitPartNumber = tiKitPartNumber;
    }
    
}
