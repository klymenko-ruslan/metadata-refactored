package mas90magmi;

import com.healthmarketscience.jackcess.Row;

/**
 *
 * @author jrodriguez
 */
public class Customer implements Comparable<Customer> {
    
    /**
     * Creates a new customer from an IMB_PriceCode Row.
     */
    public static Customer fromRow(Row row) {
        String customerNumber = (String) row.get("CustomerNumber");
        String division       = (String) row.get("Division");

        return new Customer(division, customerNumber);
    }
    private final String division;
    
    private final String customerNumber;

    public Customer(String division, String customerNumber) {
        this.division = division;
        this.customerNumber = customerNumber;
    }
    
    public String getDivision() {
        return division;
    }

    public String getCustomerNumber() {
        return customerNumber;
    }

    public int compareTo(Customer o) {
        if (o == null) {
            return 1;
        }
        
        return this.customerNumber.compareTo(o.customerNumber);
    }
    
}
