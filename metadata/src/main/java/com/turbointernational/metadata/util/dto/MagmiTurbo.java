
package com.turbointernational.metadata.util.dto;

/**
 *
 * @author jrodriguez
 */
public class MagmiTurbo {
    
    public final Long sku;
    
    public final String partTurboType;
    
    public final String turboType;
    
    public final String turboModel;
    
    public final String finder;

    public MagmiTurbo(Long sku, String partTurboType, String turboType, String turboModel, String finder) {
        this.sku = sku;
        this.partTurboType = partTurboType;
        this.turboType = turboType;
        this.turboModel = turboModel;
        this.finder = finder;
    }

}
