
package com.turbointernational.metadata.util.dto;

/**
 *
 * @author jrodriguez
 */
public class MagmiBasicProduct {
    
    private final Long sku;
    
    private final Long imageId;
    
    private final String partTurboType;
    
    private final String turboType;
    
    private final String turboModel;
    
    private final String finderTurbo;
    
    private final String finderApplication;
    
    private final String applicationDetail;

    public MagmiBasicProduct(Long sku, Long imageId, String partTurboType, String turboType, String turboModel, String finderTurbo, String finderApplication, String applicationDetail) {
        this.sku = sku;
        this.imageId = imageId;
        this.partTurboType = partTurboType;
        this.turboType = turboType;
        this.turboModel = turboModel;
        this.finderTurbo = finderTurbo;
        this.finderApplication = finderApplication;
        this.applicationDetail = applicationDetail;
    }

    public Long getSku() {
        return sku;
    }
    
    public Long getImageId() {
        return imageId;
    }

    public String getPartTurboType() {
        return partTurboType;
    }
    
    /**
     * @return the turboType
     */
    public String getTurboType() {
        return turboType;
    }

    /**
     * @return the turboModel
     */
    public String getTurboModel() {
        return turboModel;
    }

    /**
     * @return the finderTurbo
     */
    public String getFinderTurbo() {
        return finderTurbo;
    }

    /**
     * @return the finderApplication
     */
    public String getFinderApplication() {
        return finderApplication;
    }

    /**
     * @return the applicationDetal
     */
    public String getApplicationDetail() {
        return applicationDetail;
    }
    
}
