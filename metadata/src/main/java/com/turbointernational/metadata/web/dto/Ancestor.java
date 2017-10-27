package com.turbointernational.metadata.web.dto;

import java.util.Arrays;
import java.util.Comparator;

import org.apache.commons.collections.comparators.ComparatorChain;

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
    private String description;

    @JsonView({ View.Summary.class })
    private String partNumber;

    @JsonView({ View.Summary.class })
    private PartType partType;

    @JsonView({ View.Summary.class })
    private Manufacturer manufacturer;

    @JsonView({ View.Summary.class })
    private int relationDistance;

    /**
     * Relation type:
     *  true - direct
     *  false  - interchange
     */
    @JsonView({ View.Summary.class })
    private Boolean relationType;

    private static Comparator<Ancestor> cmpDistance = (a0, a1) -> {return a0.getRelationDistance() - a1.getRelationDistance();};
    private static Comparator<Ancestor> cmpRelationType = (a0, a1) -> /* reverse */ a1.getRelationType().compareTo(a0.getRelationType());
    private static Comparator<Ancestor> cmpPartNumber = (a0, a1) -> a0.getPartNumber().compareTo(a1.getPartNumber());
    @SuppressWarnings("unchecked")
    public final static Comparator<Ancestor> cmpComplex = new ComparatorChain(Arrays.asList(cmpDistance, cmpRelationType, cmpPartNumber));

    public Ancestor() {
    }

    public Ancestor(Long partId, String name, String description, String partNumber, PartType partType,
            Manufacturer manufacturer, int relationDistance, Boolean isDirect) {
        this.partId = partId;
        this.name = name;
        this.description = description;
        this.partNumber = partNumber;
        this.partType = partType;
        this.manufacturer = manufacturer;
        this.relationDistance = relationDistance;
        this.relationType = isDirect;
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

    public void setDescription(String descritption) {
        this.description = descritption;
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

    public int getRelationDistance() {
        return relationDistance;
    }

    public void setRelationDistance(int relationDistance) {
        this.relationDistance = relationDistance;
    }

    public Boolean getRelationType() {
        return relationType;
    }

    public void setRelationType(Boolean isInterchange) {
        this.relationType = isInterchange;
    }

}
