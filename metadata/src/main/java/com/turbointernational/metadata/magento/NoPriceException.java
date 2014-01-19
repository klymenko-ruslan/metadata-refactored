
package com.turbointernational.metadata.magento;

/**
 *
 * @author jrodriguez
 */
class NoPriceException extends Exception {
    
    public NoPriceException(String id) {
        super("No prices for part #" + id);
    }
    
}
