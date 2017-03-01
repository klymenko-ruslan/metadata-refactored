package com.turbointernational.metadata.entity.part.types;

import static javax.persistence.FetchType.LAZY;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.entity.CriticalDimensionEnumVal;
import com.turbointernational.metadata.entity.part.Part;
import com.turbointernational.metadata.util.View;

/**
 * Created by dmytro.trunykov@zorallabs.com on 2016-08-01 17:06:08.761256.
 */
@Entity
@Table(name = "spring")
@DiscriminatorValue("42")
@PrimaryKeyJoinColumn(name = "part_id")
public class Spring extends Part {

    private static final long serialVersionUID = -4113372816402582608L;

    // <editor-fold defaultstate="collapsed" desc="Properties: critical
    // dimensions">

    @JsonView(View.Summary.class)
    @JsonProperty("outerDiameter")
    @Column(name = "outerDiameter")
    private Double outerDiameter;

    @JsonView(View.Summary.class)
    @JsonProperty("overallLength")
    @Column(name = "overallLength")
    private Double overallLength;

    @JsonView(View.Summary.class)
    @JsonProperty("type")
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "type")
    private CriticalDimensionEnumVal type;

    @JsonView(View.Summary.class)
    @JsonProperty("material")
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "material")
    private CriticalDimensionEnumVal material;

    @JsonView(View.Summary.class)
    @JsonProperty("diagram")
    @Column(name = "diagram")
    private Integer diagram;

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Getters and setters: critical
    // dimensions">

    public Double getOuterDiameter() {
        return outerDiameter;
    }

    public void setOuterDiameter(Double outerDiameter) {
        this.outerDiameter = outerDiameter;
    }

    public Double getOverallLength() {
        return overallLength;
    }

    public void setOverallLength(Double overallLength) {
        this.overallLength = overallLength;
    }

    public CriticalDimensionEnumVal getType() {
        return type;
    }

    public void setType(CriticalDimensionEnumVal type) {
        this.type = type;
    }

    public CriticalDimensionEnumVal getMaterial() {
        return material;
    }

    public void setMaterial(CriticalDimensionEnumVal material) {
        this.material = material;
    }

    public Integer getDiagram() {
        return diagram;
    }

    public void setDiagram(Integer diagram) {
        this.diagram = diagram;
    }

    // </editor-fold>

}
