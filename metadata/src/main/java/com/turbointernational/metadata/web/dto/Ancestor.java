package com.turbointernational.metadata.web.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.util.View;

/**
 * @author dmytro.trunykov@zorallabs.com
 */
@JsonInclude(JsonInclude.Include.ALWAYS)
public class Ancestor {

    @JsonView({ View.Summary.class })
    private Long partId;

    @JsonView({ View.Summary.class })
    private String name;

    @JsonView({ View.Summary.class })
    private String descritption;

    @JsonView({ View.Summary.class })
    private String partNumber;

    @JsonView({ View.Summary.class })
    private Manufacturer manufacturer;

    @JsonView({ View.Summary.class })
    private int relationDistance;

    public Ancestor(Long partId, String name, String descritption, String partNumber, Manufacturer manufacturer,
            int relationDistance) {
        this.partId = partId;
        this.name = name;
        this.descritption = descritption;
        this.partNumber = partNumber;
        this.manufacturer = manufacturer;
        this.relationDistance = relationDistance;
    }

    public static Ancestor from(com.turbointernational.metadata.entity.part.Part p, int distance) {
        Manufacturer manufacturer = Manufacturer.from(p.getManufacturer());
        return new Ancestor(p.getId(), p.getName(), p.getDescription(), p.getManufacturerPartNumber(), manufacturer,
                distance);
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

    public String getDescritption() {
        return descritption;
    }

    public void setDescritption(String descritption) {
        this.descritption = descritption;
    }

    public String getPartNumber() {
        return partNumber;
    }

    public void setPartNumber(String partNumber) {
        this.partNumber = partNumber;
    }

    public Manufacturer getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(Manufacturer manufacturer) {
        this.manufacturer = manufacturer;
    }

    public int getRelationDistance() {
        return relationDistance;
    }

    public void setRelationDistance(int relationDistance) {
        this.relationDistance = relationDistance;
    }

}
