package com.turbointernational.metadata.domain.part.types;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.domain.criticaldimension.CriticalDimensionEnumVal;
import com.turbointernational.metadata.domain.part.Part;
import com.turbointernational.metadata.web.View;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;


/**
 * Created by dmytro.trunykov@zorallabs.com on 2016-07-15 14:03:20.421404.
 */
@Entity
@Table(name = "pin")
@PrimaryKeyJoinColumn(name = "part_id")
public class Pin extends Part {

    //<editor-fold defaultstate="collapsed" desc="Properties: critical dimensions">

    @JsonView(View.Summary.class)
    @JsonProperty("length")
    @Column(name = "length")
    private Double length;

    @JsonView(View.Summary.class)
    @JsonProperty("outerDiameter")
    @Column(name = "outerDiameter")
    private Double outerDiameter;

    @JsonView(View.Summary.class)
    @JsonProperty("secondaryDiameter")
    @Column(name = "secondaryDiameter")
    private Double secondaryDiameter;

    @JsonView(View.Summary.class)
    @JsonProperty("location")
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "location")
    private CriticalDimensionEnumVal location;

    @JsonView(View.Summary.class)
    @JsonProperty("material")
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "material")
    private CriticalDimensionEnumVal material;

    @JsonView(View.Summary.class)
    @JsonProperty("diagram")
    @Column(name = "diagram")
    private Integer diagram;

    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Getters and setters: critical dimensions">

    public Double getLength() {
        return length;
    }

    public void setLength(Double length) {
        this.length = length;
    }

    public Double getOuterDiameter() {
        return outerDiameter;
    }

    public void setOuterDiameter(Double outerDiameter) {
        this.outerDiameter = outerDiameter;
    }

    public Double getSecondaryDiameter() {
        return secondaryDiameter;
    }

    public void setSecondaryDiameter(Double secondaryDiameter) {
        this.secondaryDiameter = secondaryDiameter;
    }

    public CriticalDimensionEnumVal getLocation() {
        return location;
    }

    public void setLocation(CriticalDimensionEnumVal location) {
        this.location = location;
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

    //</editor-fold>

}
