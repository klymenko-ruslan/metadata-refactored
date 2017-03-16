/**
 * 
 */
package com.turbointernational.metadata.web.dto;

/**
 * @author dmytro.trunykov@zorallabs.com
 */
public class AlsoBought {

    private String manufacturerPartNumber;

    private String partType;

    private String name;

    private Integer qtyShipped;

    private Double saleAmount;

    private Integer orders;

    public AlsoBought() {
    }

    public AlsoBought(String manufacturerPartNumber, String partType, String name, Integer qtyShipped,
            Double saleAmount, Integer orders) {
        super();
        this.manufacturerPartNumber = manufacturerPartNumber;
        this.partType = partType;
        this.name = name;
        this.qtyShipped = qtyShipped;
        this.saleAmount = saleAmount;
        this.orders = orders;
    }

    public String getManufacturerPartNumber() {
        return manufacturerPartNumber;
    }

    public void setManufacturerPartNumber(String manufacturerPartNumber) {
        this.manufacturerPartNumber = manufacturerPartNumber;
    }

    public String getPartType() {
        return partType;
    }

    public void setPartType(String partType) {
        this.partType = partType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getQtyShipped() {
        return qtyShipped;
    }

    public void setQtyShipped(Integer qtyShipped) {
        this.qtyShipped = qtyShipped;
    }

    public Double getSaleAmount() {
        return saleAmount;
    }

    public void setSaleAmount(Double saleAmount) {
        this.saleAmount = saleAmount;
    }

    public Integer getOrders() {
        return orders;
    }

    public void setOrders(Integer orders) {
        this.orders = orders;
    }

}
