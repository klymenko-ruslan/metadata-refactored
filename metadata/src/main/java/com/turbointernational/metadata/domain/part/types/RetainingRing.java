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
    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "internalExternal")
    private CriticalDimensionEnumVal internalExternal;

    @JsonView(View.Summary.class)
    @JsonProperty("ringType")
    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "ringType")
    private CriticalDimensionEnumVal ringType;

    @JsonView(View.Summary.class)
    @JsonProperty("taperedConstantSection")
    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "taperedConstantSection")
    private CriticalDimensionEnumVal taperedConstantSection;

    @JsonView(View.Summary.class)
    @JsonProperty("axiallyRadiallyAssembled")
    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "axiallyRadiallyAssembled")
    private CriticalDimensionEnumVal axiallyRadiallyAssembled;

    @JsonView(View.Summary.class)
    @JsonProperty("selfLocking")
    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "selfLocking")
    private CriticalDimensionEnumVal selfLocking;

    @JsonView(View.Summary.class)
    @JsonProperty("beveled")
    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "beveled")
    private CriticalDimensionEnumVal beveled;

    @JsonView(View.Summary.class)
    @JsonProperty("bowed")
    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "bowed")
    private CriticalDimensionEnumVal bowed;

    @JsonView(View.Summary.class)
    @JsonProperty("freeDiaA")
    @Column(name = "freeDiaA")
    private Double freeDiaA;

    @JsonView(View.Summary.class)
    @JsonProperty("freeDiaLowerATol")
    @Column(name = "freeDiaLowerATol")
    private Double freeDiaLowerATol;

    @JsonView(View.Summary.class)
    @JsonProperty("freeDiaUpperATol")
    @Column(name = "freeDiaUpperATol")
    private Double freeDiaUpperATol;

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
    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "material")
    private CriticalDimensionEnumVal material;

    @JsonView(View.Summary.class)
    @JsonProperty("finishPlating")
    @ManyToOne(fetch = EAGER)
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
    public String getReferencenumber() {
        return referenceNumber;
    }

    public void setReferencenumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public CriticalDimensionEnumVal getInternalexternal() {
        return internalExternal;
    }

    public void setInternalexternal(CriticalDimensionEnumVal internalExternal) {
        this.internalExternal = internalExternal;
    }

    public CriticalDimensionEnumVal getRingtype() {
        return ringType;
    }

    public void setRingtype(CriticalDimensionEnumVal ringType) {
        this.ringType = ringType;
    }

    public CriticalDimensionEnumVal getTaperedconstantsection() {
        return taperedConstantSection;
    }

    public void setTaperedconstantsection(CriticalDimensionEnumVal taperedConstantSection) {
        this.taperedConstantSection = taperedConstantSection;
    }

    public CriticalDimensionEnumVal getAxiallyradiallyassembled() {
        return axiallyRadiallyAssembled;
    }

    public void setAxiallyradiallyassembled(CriticalDimensionEnumVal axiallyRadiallyAssembled) {
        this.axiallyRadiallyAssembled = axiallyRadiallyAssembled;
    }

    public CriticalDimensionEnumVal getSelflocking() {
        return selfLocking;
    }

    public void setSelflocking(CriticalDimensionEnumVal selfLocking) {
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

    public Double getFreediaa() {
        return freeDiaA;
    }

    public void setFreediaa(Double freeDiaA) {
        this.freeDiaA = freeDiaA;
    }

    public Double getFreedialoweratol() {
        return freeDiaLowerATol;
    }

    public void setFreedialoweratol(Double freeDiaLowerATol) {
        this.freeDiaLowerATol = freeDiaLowerATol;
    }

    public Double getFreediaupperatol() {
        return freeDiaUpperATol;
    }

    public void setFreediaupperatol(Double freeDiaUpperATol) {
        this.freeDiaUpperATol = freeDiaUpperATol;
    }

    public Double getThicknessb() {
        return thicknessB;
    }

    public void setThicknessb(Double thicknessB) {
        this.thicknessB = thicknessB;
    }

    public Double getThicknessbtol() {
        return thicknessBTol;
    }

    public void setThicknessbtol(Double thicknessBTol) {
        this.thicknessBTol = thicknessBTol;
    }

    public Double getHolediac() {
        return holeDiaC;
    }

    public void setHolediac(Double holeDiaC) {
        this.holeDiaC = holeDiaC;
    }

    public Integer getNumberofprongs() {
        return numberOfProngs;
    }

    public void setNumberofprongs(Integer numberOfProngs) {
        this.numberOfProngs = numberOfProngs;
    }

    public Double getHousingborediae() {
        return housingBoreDiaE;
    }

    public void setHousingborediae(Double housingBoreDiaE) {
        this.housingBoreDiaE = housingBoreDiaE;
    }

    public Double getShaftdiad() {
        return shaftDiaD;
    }

    public void setShaftdiad(Double shaftDiaD) {
        this.shaftDiaD = shaftDiaD;
    }

    public Double getGroovedia() {
        return grooveDia;
    }

    public void setGroovedia(Double grooveDia) {
        this.grooveDia = grooveDia;
    }

    public Double getGroovediauppertol() {
        return grooveDiaUpperTol;
    }

    public void setGroovediauppertol(Double grooveDiaUpperTol) {
        this.grooveDiaUpperTol = grooveDiaUpperTol;
    }

    public Double getGroovedialowertol() {
        return grooveDiaLowerTol;
    }

    public void setGroovedialowertol(Double grooveDiaLowerTol) {
        this.grooveDiaLowerTol = grooveDiaLowerTol;
    }

    public Double getGroovewidth() {
        return grooveWidth;
    }

    public void setGroovewidth(Double grooveWidth) {
        this.grooveWidth = grooveWidth;
    }

    public Double getGroovewidthuppertol() {
        return grooveWidthUpperTol;
    }

    public void setGroovewidthuppertol(Double grooveWidthUpperTol) {
        this.grooveWidthUpperTol = grooveWidthUpperTol;
    }

    public Double getGroovewidthlowertol() {
        return grooveWidthLowerTol;
    }

    public void setGroovewidthlowertol(Double grooveWidthLowerTol) {
        this.grooveWidthLowerTol = grooveWidthLowerTol;
    }

    public CriticalDimensionEnumVal getMaterial() {
        return material;
    }

    public void setMaterial(CriticalDimensionEnumVal material) {
        this.material = material;
    }

    public CriticalDimensionEnumVal getFinishplating() {
        return finishPlating;
    }

    public void setFinishplating(CriticalDimensionEnumVal finishPlating) {
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
