
package com.turbointernational.metadata.magento;

/**
 *
 * @author jrodriguez
 */
class NoPriceException extends Exception {
    
    private Long id;

    public Long getId() {
        return id;
    }
    
    public NoPriceException(Long id) {
        super("No prices for part #" + id);
    }
    
}
