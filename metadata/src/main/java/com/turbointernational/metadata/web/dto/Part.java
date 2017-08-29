package com.turbointernational.metadata.web.dto;

import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.service.ArangoDbConnectorService.GetPartResponse;
import com.turbointernational.metadata.util.View;

/**
 * @author dmytro.trunykov@gmail.com
 */
public class Part {

    @JsonView({ View.Summary.class })
    private Long partId;

    @JsonView({ View.Summary.class })
    private String name;

    @JsonView({ View.Summary.class })
    private String description;

    @JsonView({ View.Summary.class })
    private String partNumber;

    @JsonView({ View.Summary.class })
    private PartType partType;

    @JsonView({ View.Summary.class })
    private Manufacturer manufacturer;

    public Part() {
    }

    public Part(Long partId, String name, String description, String partNumber, PartType partType,
            Manufacturer manufacturer) {
        this.partId = partId;
        this.name = name;
        this.description = description;
        this.partNumber = partNumber;
        this.partType = partType;
        this.manufacturer = manufacturer;
    }

    public static Part from(GetPartResponse o) {
        PartType partType = PartType.from(o.getPartType());
        Manufacturer manufacturer = Manufacturer.from(o.getManufacturer());
        return new Part(o.getPartId(), o.getName(), o.getDescritpion(), o.getPartNumber(), partType, manufacturer);
    }

    public static Part[] from(GetPartResponse[] a) {
        int n = a.length;
        Part[] retVal = new Part[n];
        for (int i = 0; i < n; i++) {
            retVal[i] = from(a[i]);
        }
        return retVal;
    }

    public Long getPartId() {
        return partId;
    }

    public void setPartId(Long partId) {
        this.partId = partId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    @Override
    public String toString() {
        return "Part [partId=" + partId + ", name=" + name + ", description=" + description + ", partNumber="
                + partNumber + ", partType=" + partType + ", manufacturer=" + manufacturer + "]";
    }

}
