package com.turbointernational.metadata.entity.part.types;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.entity.CriticalDimensionEnumVal;
import com.turbointernational.metadata.entity.part.Part;
import com.turbointernational.metadata.util.View;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;


/**
 * Created by dmytro.trunykov@zorallabs.com on 2016-08-01 17:06:08.759693.
 */
@Entity
@Table(name = "o_ring")
@DiscriminatorValue("17")
@PrimaryKeyJoinColumn(name = "part_id")
public class ORing extends Part {

    //<editor-fold defaultstate="collapsed" desc="Properties: critical dimensions">

    @JsonView(View.Summary.class)
    @JsonProperty("innerDiaA")
    @Column(name = "innerDiaA")
    private Double innerDiaA;

    @JsonView(View.Summary.class)
    @JsonProperty("innerDiaATol")
    @Column(name = "innerDiaATol")
    private Double innerDiaATol;

    @JsonView(View.Summary.class)
    @JsonProperty("crossSectionType")
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "crossSectionType")
    private CriticalDimensionEnumVal crossSectionType;

    @JsonView(View.Summary.class)
    @JsonProperty("xSecDiaB")
    @Column(name = "xSecDiaB")
    private Double xSecDiaB;

    @JsonView(View.Summary.class)
    @JsonProperty("xSecDiaBTol")
    @Column(name = "xSecDiaBTol")
    private Double xSecDiaBTol;

    @JsonView(View.Summary.class)
    @JsonProperty("radialSectionDimSquare")
    @Column(name = "radialSectionDimSquare")
    private Double radialSectionDimSquare;

    @JsonView(View.Summary.class)
    @JsonProperty("radialSectionDimTol")
    @Column(name = "radialSectionDimTol")
    private Double radialSectionDimTol;

    @JsonView(View.Summary.class)
    @JsonProperty("axialSectionDimSquare")
    @Column(name = "axialSectionDimSquare")
    private Double axialSectionDimSquare;

    @JsonView(View.Summary.class)
    @JsonProperty("axialSectionDimTol")
    @Column(name = "axialSectionDimTol")
    private Double axialSectionDimTol;

    @JsonView(View.Summary.class)
    @JsonProperty("matL")
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "matL")
    private CriticalDimensionEnumVal matL;

    @JsonView(View.Summary.class)
    @JsonProperty("dashNo")
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "dashNo")
    private CriticalDimensionEnumVal dashNo;

    @JsonView(View.Summary.class)
    @JsonProperty("specialFeatures")
    @Column(name = "specialFeatures")
    private String specialFeatures;

    @JsonView(View.Summary.class)
    @JsonProperty("showSpecialFeatures")
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "showSpecialFeatures")
    private CriticalDimensionEnumVal showSpecialFeatures;

    @JsonView(View.Summary.class)
    @JsonProperty("weight")
    @Column(name = "weight")
    private Double weight;

    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Getters and setters: critical dimensions">

    public Double getInnerDiaA() {
        return innerDiaA;
    }

    public void setInnerDiaA(Double innerDiaA) {
        this.innerDiaA = innerDiaA;
    }

    public Double getInnerDiaATol() {
        return innerDiaATol;
    }

    public void setInnerDiaATol(Double innerDiaATol) {
        this.innerDiaATol = innerDiaATol;
    }

    public CriticalDimensionEnumVal getCrossSectionType() {
        return crossSectionType;
    }

    public void setCrossSectionType(CriticalDimensionEnumVal crossSectionType) {
        this.crossSectionType = crossSectionType;
    }

    public Double getxSecDiaB() {
        return xSecDiaB;
    }

    public void setxSecDiaB(Double xSecDiaB) {
        this.xSecDiaB = xSecDiaB;
    }

    public Double getxSecDiaBTol() {
        return xSecDiaBTol;
    }

    public void setxSecDiaBTol(Double xSecDiaBTol) {
        this.xSecDiaBTol = xSecDiaBTol;
    }

    public Double getRadialSectionDimSquare() {
        return radialSectionDimSquare;
    }

    public void setRadialSectionDimSquare(Double radialSectionDimSquare) {
        this.radialSectionDimSquare = radialSectionDimSquare;
    }

    public Double getRadialSectionDimTol() {
        return radialSectionDimTol;
    }

    public void setRadialSectionDimTol(Double radialSectionDimTol) {
        this.radialSectionDimTol = radialSectionDimTol;
    }

    public Double getAxialSectionDimSquare() {
        return axialSectionDimSquare;
    }

    public void setAxialSectionDimSquare(Double axialSectionDimSquare) {
        this.axialSectionDimSquare = axialSectionDimSquare;
    }

    public Double getAxialSectionDimTol() {
        return axialSectionDimTol;
    }

    public void setAxialSectionDimTol(Double axialSectionDimTol) {
        this.axialSectionDimTol = axialSectionDimTol;
    }

    public CriticalDimensionEnumVal getMatL() {
        return matL;
    }

    public void setMatL(CriticalDimensionEnumVal matL) {
        this.matL = matL;
    }

    public CriticalDimensionEnumVal getDashNo() {
        return dashNo;
    }

    public void setDashNo(CriticalDimensionEnumVal dashNo) {
        this.dashNo = dashNo;
    }

    public String getSpecialFeatures() {
        return specialFeatures;
    }

    public void setSpecialFeatures(String specialFeatures) {
        this.specialFeatures = specialFeatures;
    }

    public CriticalDimensionEnumVal getShowSpecialFeatures() {
        return showSpecialFeatures;
    }

    public void setShowSpecialFeatures(CriticalDimensionEnumVal showSpecialFeatures) {
        this.showSpecialFeatures = showSpecialFeatures;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    //</editor-fold>

}
