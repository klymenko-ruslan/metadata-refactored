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

    public AlsoBought(Long partId, String manufacturerPartNumber, String partType, String name, Integer qtyShipped,
            Double saleAmount, Integer orders) {
        this.setPartId(partId);
        this.manufacturerPartNumber = manufacturerPartNumber;
        this.partTypeName = partType;
        this.name = name;
        this.qtyShipped = qtyShipped;
        this.saleAmount = saleAmount;
        this.orders = orders;
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
