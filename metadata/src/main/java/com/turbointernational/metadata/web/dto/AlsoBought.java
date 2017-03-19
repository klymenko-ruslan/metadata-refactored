/**
 *
 */
package com.turbointernational.metadata.web.dto;

/**
 * @author dmytro.trunykov@zorallabs.com
 */
public class AlsoBought {

    private Long partId;

    private String manufacturerPartNumber;

    private String partTypeName;

    private String name;

    private Integer qtyShipped;

    private Double saleAmount;

    private Integer orders;

    public AlsoBought() {
    }

    public AlsoBought(String manufacturerPartNumber, Integer qtyShipped, Integer orders, Double saleAmount) {
        this.manufacturerPartNumber = manufacturerPartNumber;
        this.qtyShipped = qtyShipped;
        this.orders = orders;
        this.saleAmount = saleAmount;
    }

    public Long getPartId() {
        return partId;
    }

    public void setPartId(Long partId) {
        this.partId = partId;
    }

    public String getManufacturerPartNumber() {
        return manufacturerPartNumber;
    }

    public void setManufacturerPartNumber(String manufacturerPartNumber) {
        this.manufacturerPartNumber = manufacturerPartNumber;
    }

    public String getPartTypeName() {
        return partTypeName;
    }

    public void setPartTypeName(String partTypeName) {
        this.partTypeName = partTypeName;
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
