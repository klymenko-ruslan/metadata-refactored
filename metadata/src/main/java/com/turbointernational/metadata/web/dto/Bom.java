package com.turbointernational.metadata.web.dto;

import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.service.GraphDbService.GetBomsResponse;
import com.turbointernational.metadata.util.View;

/**
 * @author dmytro.trunykov@zorallabs.com
 */
public class Bom {

    @JsonView({ View.Summary.class })
    private Long partId;

    @JsonView({ View.Summary.class })
    private String partNumber;

    @JsonView({ View.Summary.class })
    private PartType partType;

    @JsonView({ View.Summary.class })
    private Manufacturer manufacturer;

    @JsonView({ View.Summary.class })
    private Integer qty;

    @JsonView({ View.Summary.class })
    private Part[] interchanges;

    public Bom() {
    }

    public Bom(Long partId, String partNumber, PartType partType, Manufacturer manufacturer, Integer qty,
            Part[] interchanges) {
        this.partId = partId;
        this.partNumber = partNumber;
        this.partType = partType;
        this.manufacturer = manufacturer;
        this.qty = qty;
        this.interchanges = interchanges;
    }

    public Bom(GetBomsResponse.Row o) {
        this.partId = o.getPartId();
        this.partNumber = o.getPartNumber();
        this.partType = o.getPartType();
        this.manufacturer = o.getManufacturer();
        this.qty = o.getQty();
        this.interchanges = o.getInterchanges();
    }

    public static Bom[] from(GetBomsResponse.Row[] rows) {
        int n = rows.length;
        Bom[] retVal = new Bom[n];
        for (int i = 0; i < n; i++) {
            retVal[i] = new Bom(rows[i]);
        }
        return retVal;
    }

    public Long getPartId() {
        return partId;
    }

    public void setPartId(Long partId) {
        this.partId = partId;
    }

    public String getPartNumber() {
        return partNumber;
    }

    public void setPartNumber(String partNumber) {
        this.partNumber = partNumber;
    }

    public PartType getPartType() {
        return partType;
    }

    public void setPartType(PartType partType) {
        this.partType = partType;
    }

    public Manufacturer getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(Manufacturer manufacturer) {
        this.manufacturer = manufacturer;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public Part[] getInterchanges() {
        return interchanges;
    }

    public void setInterchanges(Part[] interchanges) {
        this.interchanges = interchanges;
    }

}
