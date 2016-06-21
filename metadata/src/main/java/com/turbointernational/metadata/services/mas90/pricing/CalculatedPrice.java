package com.turbointernational.metadata.services.mas90.pricing;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.web.View;

import java.math.BigDecimal;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.ALWAYS;

/**
 *
 * @author jrodriguez
 */
public class CalculatedPrice {

    @JsonView(View.Summary.class)
    @JsonInclude(ALWAYS)
    private int breakLevel;

    @JsonView(View.Summary.class)
    @JsonInclude(ALWAYS)
    private int quantity;

    @JsonView(View.Summary.class)
    @JsonInclude(ALWAYS)
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

    @Override
    public String toString() {
        return "CalculatedPrice{" +
                "breakLevel=" + breakLevel +
                ", quantity=" + quantity +
                ", price=" + price +
                '}';
    }
}
