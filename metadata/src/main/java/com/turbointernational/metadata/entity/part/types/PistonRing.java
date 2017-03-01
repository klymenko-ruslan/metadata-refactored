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
 * Created by dmytro.trunykov@zorallabs.com on 2016-08-01 17:06:08.760016.
 */
@Entity
@Table(name = "piston_ring")
@DiscriminatorValue("4")
@PrimaryKeyJoinColumn(name = "part_id")
public class PistonRing extends Part {

    private static final long serialVersionUID = -730969290293076980L;

    // <editor-fold defaultstate="collapsed" desc="Properties: critical
    // dimensions">

    @JsonView(View.Summary.class)
    @JsonProperty("installedDiameterA")
    @Column(name = "installedDiameterA")
    private Double installedDiameterA;

    @JsonView(View.Summary.class)
    @JsonProperty("gapBInstalledDiameter")
    @Column(name = "gapBInstalledDiameter")
    private Double gapBInstalledDiameter;

    @JsonView(View.Summary.class)
    @JsonProperty("thicknessC")
    @Column(name = "thicknessC")
    private Double thicknessC;

    @JsonView(View.Summary.class)
    @JsonProperty("thicknessCTol")
    @Column(name = "thicknessCTol")
    private Double thicknessCTol;

    @JsonView(View.Summary.class)
    @JsonProperty("widthD")
    @Column(name = "widthD")
    private Double widthD;

    @JsonView(View.Summary.class)
    @JsonProperty("widthDUpperTol")
    @Column(name = "widthDUpperTol")
    private Double widthDUpperTol;

    @JsonView(View.Summary.class)
    @JsonProperty("widthDLowerTol")
    @Column(name = "widthDLowerTol")
    private Double widthDLowerTol;

    @JsonView(View.Summary.class)
    @JsonProperty("material")
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "material")
    private CriticalDimensionEnumVal material;

    @JsonView(View.Summary.class)
    @JsonProperty("type")
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "type")
    private CriticalDimensionEnumVal type;

    @JsonView(View.Summary.class)
    @JsonProperty("weight")
    @Column(name = "weight")
    private Double weight;

    @JsonView(View.Summary.class)
    @JsonProperty("diagram")
    @Column(name = "diagram")
    private Integer diagram;

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Getters and setters: critical
    // dimensions">

    public Double getInstalledDiameterA() {
        return installedDiameterA;
    }

    public void setInstalledDiameterA(Double installedDiameterA) {
        this.installedDiameterA = installedDiameterA;
    }

    public Double getGapBInstalledDiameter() {
        return gapBInstalledDiameter;
    }

    public void setGapBInstalledDiameter(Double gapBInstalledDiameter) {
        this.gapBInstalledDiameter = gapBInstalledDiameter;
    }

    public Double getThicknessC() {
        return thicknessC;
    }

    public void setThicknessC(Double thicknessC) {
        this.thicknessC = thicknessC;
    }

    public Double getThicknessCTol() {
        return thicknessCTol;
    }

    public void setThicknessCTol(Double thicknessCTol) {
        this.thicknessCTol = thicknessCTol;
    }

    public Double getWidthD() {
        return widthD;
    }

    public void setWidthD(Double widthD) {
        this.widthD = widthD;
    }

    public Double getWidthDUpperTol() {
        return widthDUpperTol;
    }

    public void setWidthDUpperTol(Double widthDUpperTol) {
        this.widthDUpperTol = widthDUpperTol;
    }

    public Double getWidthDLowerTol() {
        return widthDLowerTol;
    }

    public void setWidthDLowerTol(Double widthDLowerTol) {
        this.widthDLowerTol = widthDLowerTol;
    }

    public CriticalDimensionEnumVal getMaterial() {
        return material;
    }

    public void setMaterial(CriticalDimensionEnumVal material) {
        this.material = material;
    }

    public CriticalDimensionEnumVal getType() {
        return type;
    }

    public void setType(CriticalDimensionEnumVal type) {
        this.type = type;
    }

    @Override
    public Double getWeight() {
        return weight;
    }

    @Override
    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Integer getDiagram() {
        return diagram;
    }

    public void setDiagram(Integer diagram) {
        this.diagram = diagram;
    }

    // </editor-fold>

}
