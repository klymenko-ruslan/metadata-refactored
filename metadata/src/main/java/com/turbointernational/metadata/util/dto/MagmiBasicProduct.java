
package com.turbointernational.metadata.util.dto;

/**
 *
 * @author jrodriguez
 */
public class MagmiBasicProduct {
    
    private Long sku;
    
    private String imageFile;
    
    private String turboType;
    
    private String turboModel;
    
    private String finderTurbo;
    
    private String finderApplication;

    public MagmiBasicProduct(Long sku, String imageFile, String turboType, String turboModel, String finderTurbo, String finderApplication) {
        this.sku = sku;
        this.imageFile = imageFile;
        this.turboType = turboType;
        this.turboModel = turboModel;
        this.finderTurbo = finderTurbo;
        this.finderApplication = finderApplication;
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
    
}
