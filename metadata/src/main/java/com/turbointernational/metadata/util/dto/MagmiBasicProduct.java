
package com.turbointernational.metadata.util.dto;

/**
 *
 * @author jrodriguez
 */
public class MagmiBasicProduct {
    
    private Long sku;
    
    private Long imageId;
    
    private String turboType;
    
    private String turboModel;
    
    private String finderTurbo;
    
    private String finderApplication;

    public MagmiBasicProduct(Long sku, Long imageId, String turboType, String turboModel, String finderTurbo, String finderApplication) {
        this.sku = sku;
        this.imageId = imageId;
        this.turboType = turboType;
        this.turboModel = turboModel;
        this.finderTurbo = finderTurbo;
        this.finderApplication = finderApplication;
    }

    public Long getSku() {
        return sku;
    }
    
    public Long getImageId() {
        return imageId;
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
    
}
