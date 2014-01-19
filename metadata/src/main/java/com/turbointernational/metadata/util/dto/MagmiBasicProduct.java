
package com.turbointernational.metadata.util.dto;

/**
 *
 * @author jrodriguez
 */
public class MagmiBasicProduct {
    
//    + "  'simple' AS type,\n"
//    + "  'Catalog, Search' AS visibility,\n"
//    + "  'Enabled' AS status,\n";
    
    private Long sku;
    
    private String name;
    
    private String description;
    
    private String attributeSet;
    
    private String partType;
    
    private String partTypeParent;
    
    private Long manufacturerId;
    
    private String manufacturer;
    
    private String partNumber;
    
    private String imageFile;
    
    private String turboType;
    
    private String turboModel;
    
    private String finderTurbo;
    
    private String finderApplication;

    public MagmiBasicProduct(Long sku, String name, String description, String attributeSet, String partType, String partTypeParent, Long manufacturerId, String manufacturer, String partNumber, String imageFile, String turboType, String turboModel, String finderTurbo, String finderApplication) {
        this.sku = sku;
        this.name = name;
        this.description = description;
        this.attributeSet = attributeSet;
        this.partType = partType;
        this.partTypeParent = partTypeParent;
        this.manufacturerId = manufacturerId;
        this.manufacturer = manufacturer;
        this.partNumber = partNumber;
        this.imageFile = imageFile;
        this.turboType = turboType;
        this.turboModel = turboModel;
        this.finderTurbo = finderTurbo;
        this.finderApplication = finderApplication;
    }

    /**
     * @return the sku
     */
    public Long getSku() {
        return sku;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return the attributeSet
     */
    public String getAttributeSet() {
        return attributeSet;
    }

    /**
     * @return the partType
     */
    public String getPartType() {
        return partType;
    }

    public String getPartTypeParent() {
        return partTypeParent;
    }
    
    /**
     * @return the manufacturerId
     */
    public Long getManufacturerId() {
        return manufacturerId;
    }
    
    /**
     * @return the manufacturer
     */
    public String getManufacturer() {
        return manufacturer;
    }

    /**
     * @return the partNumber
     */
    public String getPartNumber() {
        return partNumber;
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
