package com.turbointernational.metadata.domain.part.types;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.domain.criticaldimension.CriticalDimensionEnumVal;
import com.turbointernational.metadata.domain.part.Part;
import com.turbointernational.metadata.web.View;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;


/**
 * Created by dmytro.trunykov@zorallabs.com on 2016-06-15 17:07:35.989281.
 */
@Entity
@Table(name = "retaining_ring")
@PrimaryKeyJoinColumn(name = "part_id")
public class RetainingRing extends Part {

    //<editor-fold defaultstate="collapsed" desc="Properties: critical dimensions">

    @JsonView(View.Summary.class)
    @JsonProperty("referenceNumber")
    @Column(name = "referenceNumber")
    private String referenceNumber;

    @JsonView(View.Summary.class)
    @JsonProperty("internalExternal")
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "internalExternal")
    private CriticalDimensionEnumVal internalExternal;

    @JsonView(View.Summary.class)
    @JsonProperty("ringType")
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "ringType")
    private CriticalDimensionEnumVal ringType;

    @JsonView(View.Summary.class)
    @JsonProperty("taperedConstantSection")
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "taperedConstantSection")
    private CriticalDimensionEnumVal taperedConstantSection;

    @JsonView(View.Summary.class)
    @JsonProperty("axiallyRadiallyAssembled")
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "axiallyRadiallyAssembled")
    private CriticalDimensionEnumVal axiallyRadiallyAssembled;

    @JsonView(View.Summary.class)
    @JsonProperty("selfLocking")
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "selfLocking")
    private CriticalDimensionEnumVal selfLocking;

    @JsonView(View.Summary.class)
    @JsonProperty("beveled")
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "beveled")
    private CriticalDimensionEnumVal beveled;

    @JsonView(View.Summary.class)
    @JsonProperty("bowed")
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "bowed")
    private CriticalDimensionEnumVal bowed;

    @JsonView(View.Summary.class)
    @JsonProperty("freeDiaA")
    @Column(name = "freeDiaA")
    private Double freeDiaA;

    @JsonView(View.Summary.class)
    @JsonProperty("freeDiaALowerTol")
    @Column(name = "freeDiaALowerTol")
    private Double freeDiaALowerTol;

    @JsonView(View.Summary.class)
    @JsonProperty("freeDiaAUpperTol")
    @Column(name = "freeDiaAUpperTol")
    private Double freeDiaAUpperTol;

    @JsonView(View.Summary.class)
    @JsonProperty("thicknessB")
    @Column(name = "thicknessB")
    private Double thicknessB;

    @JsonView(View.Summary.class)
    @JsonProperty("thicknessBTol")
    @Column(name = "thicknessBTol")
    private Double thicknessBTol;

    @JsonView(View.Summary.class)
    @JsonProperty("holeDiaC")
    @Column(name = "holeDiaC")
    private Double holeDiaC;

    @JsonView(View.Summary.class)
    @JsonProperty("numberOfProngs")
    @Column(name = "numberOfProngs")
    private Integer numberOfProngs;

    @JsonView(View.Summary.class)
    @JsonProperty("housingBoreDiaE")
    @Column(name = "housingBoreDiaE")
    private Double housingBoreDiaE;

    @JsonView(View.Summary.class)
    @JsonProperty("shaftDiaD")
    @Column(name = "shaftDiaD")
    private Double shaftDiaD;

    @JsonView(View.Summary.class)
    @JsonProperty("grooveDia")
    @Column(name = "grooveDia")
    private Double grooveDia;

    @JsonView(View.Summary.class)
    @JsonProperty("grooveDiaUpperTol")
    @Column(name = "grooveDiaUpperTol")
    private Double grooveDiaUpperTol;

    @JsonView(View.Summary.class)
    @JsonProperty("grooveDiaLowerTol")
    @Column(name = "grooveDiaLowerTol")
    private Double grooveDiaLowerTol;

    @JsonView(View.Summary.class)
    @JsonProperty("grooveWidth")
    @Column(name = "grooveWidth")
    private Double grooveWidth;

    @JsonView(View.Summary.class)
    @JsonProperty("grooveWidthUpperTol")
    @Column(name = "grooveWidthUpperTol")
    private Double grooveWidthUpperTol;

    @JsonView(View.Summary.class)
    @JsonProperty("grooveWidthLowerTol")
    @Column(name = "grooveWidthLowerTol")
    private Double grooveWidthLowerTol;

    @JsonView(View.Summary.class)
    @JsonProperty("material")
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "material")
    private CriticalDimensionEnumVal material;

    @JsonView(View.Summary.class)
    @JsonProperty("finishPlating")
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "finishPlating")
    private CriticalDimensionEnumVal finishPlating;

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

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public CriticalDimensionEnumVal getInternalExternal() {
        return internalExternal;
    }

    public void setInternalExternal(CriticalDimensionEnumVal internalExternal) {
        this.internalExternal = internalExternal;
    }

    public CriticalDimensionEnumVal getRingType() {
        return ringType;
    }

    public void setRingType(CriticalDimensionEnumVal ringType) {
        this.ringType = ringType;
    }

    public CriticalDimensionEnumVal getTaperedConstantSection() {
        return taperedConstantSection;
    }

    public void setTaperedConstantSection(CriticalDimensionEnumVal taperedConstantSection) {
        this.taperedConstantSection = taperedConstantSection;
    }

    public CriticalDimensionEnumVal getAxiallyRadiallyAssembled() {
        return axiallyRadiallyAssembled;
    }

    public void setAxiallyRadiallyAssembled(CriticalDimensionEnumVal axiallyRadiallyAssembled) {
        this.axiallyRadiallyAssembled = axiallyRadiallyAssembled;
    }

    public CriticalDimensionEnumVal getSelfLocking() {
        return selfLocking;
    }

    public void setSelfLocking(CriticalDimensionEnumVal selfLocking) {
        this.selfLocking = selfLocking;
    }

    public CriticalDimensionEnumVal getBeveled() {
        return beveled;
    }

    public void setBeveled(CriticalDimensionEnumVal beveled) {
        this.beveled = beveled;
    }

    public CriticalDimensionEnumVal getBowed() {
        return bowed;
    }

    public void setBowed(CriticalDimensionEnumVal bowed) {
        this.bowed = bowed;
    }

    public Double getFreeDiaA() {
        return freeDiaA;
    }

    public void setFreeDiaA(Double freeDiaA) {
        this.freeDiaA = freeDiaA;
    }

    public Double getFreeDiaALowerTol() {
        return freeDiaALowerTol;
    }

    public void setFreeDiaALowerTol(Double freeDiaALowerTol) {
        this.freeDiaALowerTol = freeDiaALowerTol;
    }

    public Double getFreeDiaAUpperTol() {
        return freeDiaAUpperTol;
    }

    public void setFreeDiaAUpperTol(Double freeDiaAUpperTol) {
        this.freeDiaAUpperTol = freeDiaAUpperTol;
    }

    public Double getThicknessB() {
        return thicknessB;
    }

    public void setThicknessB(Double thicknessB) {
        this.thicknessB = thicknessB;
    }

    public Double getThicknessBTol() {
        return thicknessBTol;
    }

    public void setThicknessBTol(Double thicknessBTol) {
        this.thicknessBTol = thicknessBTol;
    }

    public Double getHoleDiaC() {
        return holeDiaC;
    }

    public void setHoleDiaC(Double holeDiaC) {
        this.holeDiaC = holeDiaC;
    }

    public Integer getNumberOfProngs() {
        return numberOfProngs;
    }

    public void setNumberOfProngs(Integer numberOfProngs) {
        this.numberOfProngs = numberOfProngs;
    }

    public Double getHousingBoreDiaE() {
        return housingBoreDiaE;
    }

    public void setHousingBoreDiaE(Double housingBoreDiaE) {
        this.housingBoreDiaE = housingBoreDiaE;
    }

    public Double getShaftDiaD() {
        return shaftDiaD;
    }

    public void setShaftDiaD(Double shaftDiaD) {
        this.shaftDiaD = shaftDiaD;
    }

    public Double getGrooveDia() {
        return grooveDia;
    }

    public void setGrooveDia(Double grooveDia) {
        this.grooveDia = grooveDia;
    }

    public Double getGrooveDiaUpperTol() {
        return grooveDiaUpperTol;
    }

    public void setGrooveDiaUpperTol(Double grooveDiaUpperTol) {
        this.grooveDiaUpperTol = grooveDiaUpperTol;
    }

    public Double getGrooveDiaLowerTol() {
        return grooveDiaLowerTol;
    }

    public void setGrooveDiaLowerTol(Double grooveDiaLowerTol) {
        this.grooveDiaLowerTol = grooveDiaLowerTol;
    }

    public Double getGrooveWidth() {
        return grooveWidth;
    }

    public void setGrooveWidth(Double grooveWidth) {
        this.grooveWidth = grooveWidth;
    }

    public Double getGrooveWidthUpperTol() {
        return grooveWidthUpperTol;
    }

    public void setGrooveWidthUpperTol(Double grooveWidthUpperTol) {
        this.grooveWidthUpperTol = grooveWidthUpperTol;
    }

    public Double getGrooveWidthLowerTol() {
        return grooveWidthLowerTol;
    }

    public void setGrooveWidthLowerTol(Double grooveWidthLowerTol) {
        this.grooveWidthLowerTol = grooveWidthLowerTol;
    }

    public CriticalDimensionEnumVal getMaterial() {
        return material;
    }

    public void setMaterial(CriticalDimensionEnumVal material) {
        this.material = material;
    }

    public CriticalDimensionEnumVal getFinishPlating() {
        return finishPlating;
    }

    public void setFinishPlating(CriticalDimensionEnumVal finishPlating) {
        this.finishPlating = finishPlating;
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
