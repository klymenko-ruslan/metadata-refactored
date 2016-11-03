package com.turbointernational.metadata.service.mas90.pricing;

/**
 *
 * @author jrodriguez
 */
public class UnknownDiscountCodeException extends RuntimeException {
    private final String code;
    public UnknownDiscountCodeException(String code) {
        super("Unknown discount code: " + code);
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
