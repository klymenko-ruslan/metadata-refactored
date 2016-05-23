package com.turbointernational.metadata.domain.part.types;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.domain.criticaldimension.CriticalDimensionEnumVal;
import com.turbointernational.metadata.domain.part.Part;
import com.turbointernational.metadata.web.View;

import javax.persistence.*;

import static javax.persistence.FetchType.EAGER;


/**
 * Created by dmytro.trunykov@zorallabs.com.
 */
@Entity
@Table(name = "o_ring")
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
    @ManyToOne(fetch = EAGER)
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
    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "matL")
    private CriticalDimensionEnumVal matL;

    @JsonView(View.Summary.class)
    @JsonProperty("dashNo")
    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "dashNo")
    private CriticalDimensionEnumVal dashNo;

    @JsonView(View.Summary.class)
    @JsonProperty("specialFeatures")
    @Column(name = "specialFeatures")
    private String specialFeatures;

    @JsonView(View.Summary.class)
    @JsonProperty("showSpecialFeatures")
    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "showSpecialFeatures")
    private CriticalDimensionEnumVal showSpecialFeatures;

    @JsonView(View.Summary.class)
    @JsonProperty("weight")
    @Column(name = "weight")
    private Double weight;


    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Getters and setters: critical dimensions">
    public Double getInnerdiaa() {
        return innerDiaA;
    }

    public void setInnerdiaa(Double innerDiaA) {
        this.innerDiaA = innerDiaA;
    }

    public Double getInnerdiaatol() {
        return innerDiaATol;
    }

    public void setInnerdiaatol(Double innerDiaATol) {
        this.innerDiaATol = innerDiaATol;
    }

    public CriticalDimensionEnumVal getCrosssectiontype() {
        return crossSectionType;
    }

    public void setCrosssectiontype(CriticalDimensionEnumVal crossSectionType) {
        this.crossSectionType = crossSectionType;
    }

    public Double getXsecdiab() {
        return xSecDiaB;
    }

    public void setXsecdiab(Double xSecDiaB) {
        this.xSecDiaB = xSecDiaB;
    }

    public Double getXsecdiabtol() {
        return xSecDiaBTol;
    }

    public void setXsecdiabtol(Double xSecDiaBTol) {
        this.xSecDiaBTol = xSecDiaBTol;
    }

    public Double getRadialsectiondimsquare() {
        return radialSectionDimSquare;
    }

    public void setRadialsectiondimsquare(Double radialSectionDimSquare) {
        this.radialSectionDimSquare = radialSectionDimSquare;
    }

    public Double getRadialsectiondimtol() {
        return radialSectionDimTol;
    }

    public void setRadialsectiondimtol(Double radialSectionDimTol) {
        this.radialSectionDimTol = radialSectionDimTol;
    }

    public Double getAxialsectiondimsquare() {
        return axialSectionDimSquare;
    }

    public void setAxialsectiondimsquare(Double axialSectionDimSquare) {
        this.axialSectionDimSquare = axialSectionDimSquare;
    }

    public Double getAxialsectiondimtol() {
        return axialSectionDimTol;
    }

    public void setAxialsectiondimtol(Double axialSectionDimTol) {
        this.axialSectionDimTol = axialSectionDimTol;
    }

    public CriticalDimensionEnumVal getMatl() {
        return matL;
    }

    public void setMatl(CriticalDimensionEnumVal matL) {
        this.matL = matL;
    }

    public CriticalDimensionEnumVal getDashno() {
        return dashNo;
    }

    public void setDashno(CriticalDimensionEnumVal dashNo) {
        this.dashNo = dashNo;
    }

    public String getSpecialfeatures() {
        return specialFeatures;
    }

    public void setSpecialfeatures(String specialFeatures) {
        this.specialFeatures = specialFeatures;
    }

    public CriticalDimensionEnumVal getShowspecialfeatures() {
        return showSpecialFeatures;
    }

    public void setShowspecialfeatures(CriticalDimensionEnumVal showSpecialFeatures) {
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
