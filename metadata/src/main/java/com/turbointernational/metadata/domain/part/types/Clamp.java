package com.turbointernational.metadata.domain.part.types;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.domain.criticaldimension.CriticalDimensionEnumVal;
import com.turbointernational.metadata.domain.part.Part;
import com.turbointernational.metadata.web.View;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;


/**
 * Created by dmytro.trunykov@zorallabs.com on 2016-06-15 17:07:35.986821.
 */
@Entity
@Table(name = "clamp")
@PrimaryKeyJoinColumn(name = "part_id")
public class Clamp extends Part {

    //<editor-fold defaultstate="collapsed" desc="Properties: critical dimensions">

    @JsonView(View.Summary.class)
    @JsonProperty("type")
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "type")
    private CriticalDimensionEnumVal type;

    @JsonView(View.Summary.class)
    @JsonProperty("numMountingHoles")
    @Column(name = "numMountingHoles")
    private Integer numMountingHoles;

    @JsonView(View.Summary.class)
    @JsonProperty("innerRadius")
    @Column(name = "innerRadius")
    private Double innerRadius;

    @JsonView(View.Summary.class)
    @JsonProperty("holeClDistanceB")
    @Column(name = "holeClDistanceB")
    private Double holeClDistanceB;

    @JsonView(View.Summary.class)
    @JsonProperty("mountingHoleDiaA")
    @Column(name = "mountingHoleDiaA")
    private Double mountingHoleDiaA;

    @JsonView(View.Summary.class)
    @JsonProperty("thicknessC")
    @Column(name = "thicknessC")
    private Double thicknessC;

    @JsonView(View.Summary.class)
    @JsonProperty("material")
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "material")
    private CriticalDimensionEnumVal material;

    @JsonView(View.Summary.class)
    @JsonProperty("platingCoating")
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "platingCoating")
    private CriticalDimensionEnumVal platingCoating;

    @JsonView(View.Summary.class)
    @JsonProperty("weight")
    @Column(name = "weight")
    private Double weight;

    @JsonView(View.Summary.class)
    @JsonProperty("diagram")
    @Column(name = "diagram")
    private Integer diagram;

    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Getters and setters: critical dimensions">

    public CriticalDimensionEnumVal getType() {
        return type;
    }

    public void setType(CriticalDimensionEnumVal type) {
        this.type = type;
    }

    public Integer getNumMountingHoles() {
        return numMountingHoles;
    }

    public void setNumMountingHoles(Integer numMountingHoles) {
        this.numMountingHoles = numMountingHoles;
    }

    public Double getInnerRadius() {
        return innerRadius;
    }

    public void setInnerRadius(Double innerRadius) {
        this.innerRadius = innerRadius;
    }

    public Double getHoleClDistanceB() {
        return holeClDistanceB;
    }

    public void setHoleClDistanceB(Double holeClDistanceB) {
        this.holeClDistanceB = holeClDistanceB;
    }

    public Double getMountingHoleDiaA() {
        return mountingHoleDiaA;
    }

    public void setMountingHoleDiaA(Double mountingHoleDiaA) {
        this.mountingHoleDiaA = mountingHoleDiaA;
    }

    public Double getThicknessC() {
        return thicknessC;
    }

    public void setThicknessC(Double thicknessC) {
        this.thicknessC = thicknessC;
    }

    public CriticalDimensionEnumVal getMaterial() {
        return material;
    }

    public void setMaterial(CriticalDimensionEnumVal material) {
        this.material = material;
    }

    public CriticalDimensionEnumVal getPlatingCoating() {
        return platingCoating;
    }

    public void setPlatingCoating(CriticalDimensionEnumVal platingCoating) {
        this.platingCoating = platingCoating;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Integer getDiagram() {
        return diagram;
    }

    public void setDiagram(Integer diagram) {
        this.diagram = diagram;
    }

    //</editor-fold>

}
