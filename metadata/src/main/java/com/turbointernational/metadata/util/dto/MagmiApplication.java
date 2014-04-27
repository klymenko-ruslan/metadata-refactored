
package com.turbointernational.metadata.util.dto;

/**
 *
 * @author jrodriguez
 */
public class MagmiApplication {
    
    public final Long sku;
    
    public final String finder;
    
    public final String detail;

    public MagmiApplication(Long sku, String finder, String detail) {
        this.sku = sku;
        this.finder = finder;
        this.detail = detail;
    }
    
}
