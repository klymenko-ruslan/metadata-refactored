
package com.turbointernational.metadata.util.dto;

/**
 *
 * @author jrodriguez
 */
public class MagmiTurbo {
    
    private Long sku;
    
    private String partTurboType;
    
    private String turboType;
    
    private String turboModel;
    
    private String finder;

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
     * @return the partTurboType
     */
    public String getPartTurboType() {
        return partTurboType;
    }

    /**
     * @param partTurboType the partTurboType to set
     */
    public void setPartTurboType(String partTurboType) {
        this.partTurboType = partTurboType;
    }

    /**
     * @return the turboType
     */
    public String getTurboType() {
        return turboType;
    }

    /**
     * @param turboType the turboType to set
     */
    public void setTurboType(String turboType) {
        this.turboType = turboType;
    }

    /**
     * @return the turboModel
     */
    public String getTurboModel() {
        return turboModel;
    }

    /**
     * @param turboModel the turboModel to set
     */
    public void setTurboModel(String turboModel) {
        this.turboModel = turboModel;
    }

    /**
     * @return the finder
     */
    public String getFinder() {
        return finder;
    }

    /**
     * @param finder the finder to set
     */
    public void setFinder(String finder) {
        this.finder = finder;
    }

}
