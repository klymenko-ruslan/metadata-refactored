package com.turbointernational.metadata.web.dto;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.ALWAYS;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.util.View;

/**
 * @author dmytro.trunykov@zorallabs.com
 */
@JsonInclude(ALWAYS)
public class AlsoBought {

    @JsonView(View.Summary.class)
    private Long partId;

    @JsonView(View.Summary.class)
    private String manufacturerPartNumber;

    @JsonView(View.Summary.class)
    private String partTypeName;

    @JsonView(View.Summary.class)
    private String name;

    @JsonView(View.Summary.class)
    private String description;

    @JsonView(View.Summary.class)
    private Integer qtyShipped;

    @JsonView(View.Summary.class)
    private BigDecimal saleAmount;

    @JsonView(View.Summary.class)
    private Integer orders;

    @JsonView(View.Summary.class)
    private List<PartDesc> interchanges;

    public AlsoBought() {
    }

    public AlsoBought(String manufacturerPartNumber, Integer qtyShipped, Integer orders, BigDecimal saleAmount) {
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

    public BigDecimal getSaleAmount() {
        return saleAmount;
    }

    public void setSaleAmount(BigDecimal saleAmount) {
        this.saleAmount = saleAmount;
    }

    public Integer getOrders() {
        return orders;
    }

    public void setOrders(Integer orders) {
        this.orders = orders;
    }

    public List<PartDesc> getInterchanges() {
        return interchanges;
    }

    public void setInterchanges(List<PartDesc> interchanges) {
        this.interchanges = interchanges;
    }

}
