
package com.turbointernational.metadata.magmi.dto;

/**
 *
 * @author jrodriguez
 */
public class MagmiApplication {
    
    private Long sku;
    
    private String finder;
    
    private String detail;

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

    /**
     * @return the detail
     */
    public String getDetail() {
        return detail;
    }

    /**
     * @param detail the detail to set
     */
    public void setDetail(String detail) {
        this.detail = detail;
    }
    
}
