
package com.turbointernational.metadata.util.dto;

/**
 * Used in a JPA "NEW" query.
 * @author jrodriguez
 */
public class MagmiServiceKit {
    
    private  Long sku;
    
    private  Long kitSku;
    
    private  String kitPartNumber;
    
    private  String description;
    
    private  Long tiKitSku;
    
    private  String tiKitPartNumber;

    /**
     * @return the sku
     */
    public Long getSku() {
        return sku;
    }

    /**
     * @param sku the sku to set
     */
    public void setSku(Long sku) {
        this.sku = sku;
    }

    /**
     * @return the kitSku
     */
    public Long getKitSku() {
        return kitSku;
    }

    /**
     * @param kitSku the kitSku to set
     */
    public void setKitSku(Long kitSku) {
        this.kitSku = kitSku;
    }

    /**
     * @return the kitPartNumber
     */
    public String getKitPartNumber() {
        return kitPartNumber;
    }

    /**
     * @param kitPartNumber the kitPartNumber to set
     */
    public void setKitPartNumber(String kitPartNumber) {
        this.kitPartNumber = kitPartNumber;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the tiKitSku
     */
    public Long getTiKitSku() {
        return tiKitSku;
    }

    /**
     * @param tiKitSku the tiKitSku to set
     */
    public void setTiKitSku(Long tiKitSku) {
        this.tiKitSku = tiKitSku;
    }

    /**
     * @return the tiKitPartNumber
     */
    public String getTiKitPartNumber() {
        return tiKitPartNumber;
    }

    /**
     * @param tiKitPartNumber the tiKitPartNumber to set
     */
    public void setTiKitPartNumber(String tiKitPartNumber) {
        this.tiKitPartNumber = tiKitPartNumber;
    }
    
}
