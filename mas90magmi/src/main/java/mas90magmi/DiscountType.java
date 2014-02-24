package mas90magmi;

/**
 *
 * @author jrodriguez
 */
public enum DiscountType {
    
    /**
     * Hard override of the item price.
     * 
     * This IS the price, full stop.
     */
    Override("O"),
    
    /**
     * Percentage discount.
     * 
     * A percentage to apply to the price. May be positive or negative.
     */
    Percentage("D"),
    
    /**
     * Fixed amount discount.
     * 
     * A fixed amount to discount the price. May be positive or negative.
     */
    Amount("P");
    
    /**
     * The discount code.
     */
    public final String CODE;

    private DiscountType(String code) {
        this.CODE = code;
    }
    
    /**
     * Gets a discount type by it's discount code.
     * @param code the discount code.
     */
    public static DiscountType getDiscountType(String code) {
        String uppercaseCode = code.toUpperCase();
        
        // Look for the discount type
        for (DiscountType type : values()) {
            if (type.CODE.equals(uppercaseCode)) {
                return type;
            }
        }
        
        throw new IllegalArgumentException("Unknown discount code: " + code);
    }
}