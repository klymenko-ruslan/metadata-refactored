
package com.turbointernational.metadata.util.dto;

/**
 *
 * @author jrodriguez
 */
public class MagmiBasicProduct {
    
    private final Long sku;
    
    private final String imageFile;
    
    private final String turboType;
    
    private final String turboModel;
    
    private final String finderTurbo;
    
    private final String finderApplication;
    
    private final String applicationDetail;

    public MagmiBasicProduct(Long sku, String imageFile, String turboType, String turboModel, String finderTurbo, String finderApplication, String applicationDetail) {
        this.sku = sku;
        this.imageFile = imageFile;
        this.turboType = turboType;
        this.turboModel = turboModel;
        this.finderTurbo = finderTurbo;
        this.finderApplication = finderApplication;
        this.applicationDetail = applicationDetail;
    }

    public Long getSku() {
        return sku;
    }
    
    public String getImageFile() {
        return imageFile;
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
