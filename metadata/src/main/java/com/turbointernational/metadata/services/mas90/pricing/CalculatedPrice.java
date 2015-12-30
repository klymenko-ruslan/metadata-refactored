package com.turbointernational.metadata.services.mas90.pricing;

import java.math.BigDecimal;

/**
 *
 * @author jrodriguez
 */
public class CalculatedPrice {

    private int breakLevel;

    private int quantity;

    private BigDecimal price;

    public CalculatedPrice(int breakLevel, int quantity, BigDecimal price) {
        this.breakLevel = breakLevel;
        this.quantity = quantity;
        this.price = price;
    }

    /**
     * @return the breakLevel
     */
    public int getBreakLevel() {
        return breakLevel;
    }

    /**
     * @return the quantity
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * @return the price
     */
    public BigDecimal getPrice() {
        return price;
    }
    
}
