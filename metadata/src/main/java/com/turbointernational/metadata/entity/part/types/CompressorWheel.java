package com.turbointernational.metadata.entity.part.types;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.entity.CriticalDimensionEnumVal;
import com.turbointernational.metadata.entity.part.Part;
import com.turbointernational.metadata.util.View;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;


/**
 * Created by dmytro.trunykov@zorallabs.com on 2016-08-01 17:06:08.757820.
 */
@Entity
@Table(name = "compressor_wheel")
@DiscriminatorValue("11")
@PrimaryKeyJoinColumn(name = "part_id")
public class CompressorWheel extends Part {

    //<editor-fold defaultstate="collapsed" desc="Properties: critical dimensions">

    @JsonView(View.Summary.class)
    @JsonProperty("rotation")
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "rotation")
    private CriticalDimensionEnumVal rotation;

    @JsonView(View.Summary.class)
    @JsonProperty("flatbackSuperback")
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "flatbackSuperback")
    private CriticalDimensionEnumVal flatbackSuperback;

    @JsonView(View.Summary.class)
    @JsonProperty("extendedTips")
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "extendedTips")
    private CriticalDimensionEnumVal extendedTips;

    @JsonView(View.Summary.class)
    @JsonProperty("threadedBore")
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "threadedBore")
    private CriticalDimensionEnumVal threadedBore;

    @JsonView(View.Summary.class)
    @JsonProperty("boreless")
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "boreless")
    private CriticalDimensionEnumVal boreless;

    @JsonView(View.Summary.class)
    @JsonProperty("inducerDiameterA")
    @Column(name = "inducerDiameterA")
    private Double inducerDiameterA;

    @JsonView(View.Summary.class)
    @JsonProperty("inducerDiameterATol")
    @Column(name = "inducerDiameterATol")
    private Double inducerDiameterATol;

    @JsonView(View.Summary.class)
    @JsonProperty("exducerDiameterB")
    @Column(name = "exducerDiameterB")
    private Double exducerDiameterB;

    @JsonView(View.Summary.class)
    @JsonProperty("exducerDiameterBTol")
    @Column(name = "exducerDiameterBTol")
    private Double exducerDiameterBTol;

    @JsonView(View.Summary.class)
    @JsonProperty("tipLocation")
    @Column(name = "tipLocation")
    private Double tipLocation;

    @JsonView(View.Summary.class)
    @JsonProperty("tipLocationTol")
    @Column(name = "tipLocationTol")
    private Double tipLocationTol;

    @JsonView(View.Summary.class)
    @JsonProperty("tipHeightD")
    @Column(name = "tipHeightD")
    private Double tipHeightD;

    @JsonView(View.Summary.class)
    @JsonProperty("tipHeightDTol")
    @Column(name = "tipHeightDTol")
    private Double tipHeightDTol;

    @JsonView(View.Summary.class)
    @JsonProperty("platformHeightE")
    @Column(name = "platformHeightE")
    private Double platformHeightE;

    @JsonView(View.Summary.class)
    @JsonProperty("platformHeightTol")
    @Column(name = "platformHeightTol")
    private Double platformHeightTol;

    @JsonView(View.Summary.class)
    @JsonProperty("maxBoreDiameter")
    @Column(name = "maxBoreDiameter")
    private Double maxBoreDiameter;

    @JsonView(View.Summary.class)
    @JsonProperty("minBoreDiameter")
    @Column(name = "minBoreDiameter")
    private Double minBoreDiameter;

    @JsonView(View.Summary.class)
    @JsonProperty("bladeCount")
    @Column(name = "bladeCount")
    private Integer bladeCount;

    @JsonView(View.Summary.class)
    @JsonProperty("platformThickness")
    @Column(name = "platformThickness")
    private Double platformThickness;

    @JsonView(View.Summary.class)
    @JsonProperty("platformThicknessTol")
    @Column(name = "platformThicknessTol")
    private Double platformThicknessTol;

    @JsonView(View.Summary.class)
    @JsonProperty("threadCallout")
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "threadCallout")
    private CriticalDimensionEnumVal threadCallout;

    @JsonView(View.Summary.class)
    @JsonProperty("threadHand")
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "threadHand")
    private CriticalDimensionEnumVal threadHand;

    @JsonView(View.Summary.class)
    @JsonProperty("platformDiameterF")
    @Column(name = "platformDiameterF")
    private Double platformDiameterF;

    @JsonView(View.Summary.class)
    @JsonProperty("platformDiameterFTol")
    @Column(name = "platformDiameterFTol")
    private Double platformDiameterFTol;

    @JsonView(View.Summary.class)
    @JsonProperty("overallHeightC")
    @Column(name = "overallHeightC")
    private Double overallHeightC;

    @JsonView(View.Summary.class)
    @JsonProperty("noseDiameterG")
    @Column(name = "noseDiameterG")
    private Double noseDiameterG;

    @JsonView(View.Summary.class)
    @JsonProperty("footDiameterH")
    @Column(name = "footDiameterH")
    private Double footDiameterH;

    @JsonView(View.Summary.class)
    @JsonProperty("bladeHeight")
    @Column(name = "bladeHeight")
    private Double bladeHeight;

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

    public CriticalDimensionEnumVal getRotation() {
        return rotation;
    }

    public void setRotation(CriticalDimensionEnumVal rotation) {
        this.rotation = rotation;
    }

    public CriticalDimensionEnumVal getFlatbackSuperback() {
        return flatbackSuperback;
    }

    public void setFlatbackSuperback(CriticalDimensionEnumVal flatbackSuperback) {
        this.flatbackSuperback = flatbackSuperback;
    }

    public CriticalDimensionEnumVal getExtendedTips() {
        return extendedTips;
    }

    public void setExtendedTips(CriticalDimensionEnumVal extendedTips) {
        this.extendedTips = extendedTips;
    }

    public CriticalDimensionEnumVal getThreadedBore() {
        return threadedBore;
    }

    public void setThreadedBore(CriticalDimensionEnumVal threadedBore) {
        this.threadedBore = threadedBore;
    }

    public CriticalDimensionEnumVal getBoreless() {
        return boreless;
    }

    public void setBoreless(CriticalDimensionEnumVal boreless) {
        this.boreless = boreless;
    }

    public Double getInducerDiameterA() {
        return inducerDiameterA;
    }

    public void setInducerDiameterA(Double inducerDiameterA) {
        this.inducerDiameterA = inducerDiameterA;
    }

    public Double getInducerDiameterATol() {
        return inducerDiameterATol;
    }

    public void setInducerDiameterATol(Double inducerDiameterATol) {
        this.inducerDiameterATol = inducerDiameterATol;
    }

    public Double getExducerDiameterB() {
        return exducerDiameterB;
    }

    public void setExducerDiameterB(Double exducerDiameterB) {
        this.exducerDiameterB = exducerDiameterB;
    }

    public Double getExducerDiameterBTol() {
        return exducerDiameterBTol;
    }

    public void setExducerDiameterBTol(Double exducerDiameterBTol) {
        this.exducerDiameterBTol = exducerDiameterBTol;
    }

    public Double getTipLocation() {
        return tipLocation;
    }

    public void setTipLocation(Double tipLocation) {
        this.tipLocation = tipLocation;
    }

    public Double getTipLocationTol() {
        return tipLocationTol;
    }

    public void setTipLocationTol(Double tipLocationTol) {
        this.tipLocationTol = tipLocationTol;
    }

    public Double getTipHeightD() {
        return tipHeightD;
    }

    public void setTipHeightD(Double tipHeightD) {
        this.tipHeightD = tipHeightD;
    }

    public Double getTipHeightDTol() {
        return tipHeightDTol;
    }

    public void setTipHeightDTol(Double tipHeightDTol) {
        this.tipHeightDTol = tipHeightDTol;
    }

    public Double getPlatformHeightE() {
        return platformHeightE;
    }

    public void setPlatformHeightE(Double platformHeightE) {
        this.platformHeightE = platformHeightE;
    }

    public Double getPlatformHeightTol() {
        return platformHeightTol;
    }

    public void setPlatformHeightTol(Double platformHeightTol) {
        this.platformHeightTol = platformHeightTol;
    }

    public Double getMaxBoreDiameter() {
        return maxBoreDiameter;
    }

    public void setMaxBoreDiameter(Double maxBoreDiameter) {
        this.maxBoreDiameter = maxBoreDiameter;
    }

    public Double getMinBoreDiameter() {
        return minBoreDiameter;
    }

    public void setMinBoreDiameter(Double minBoreDiameter) {
        this.minBoreDiameter = minBoreDiameter;
    }

    public Integer getBladeCount() {
        return bladeCount;
    }

    public void setBladeCount(Integer bladeCount) {
        this.bladeCount = bladeCount;
    }

    public Double getPlatformThickness() {
        return platformThickness;
    }

    public void setPlatformThickness(Double platformThickness) {
        this.platformThickness = platformThickness;
    }

    public Double getPlatformThicknessTol() {
        return platformThicknessTol;
    }

    public void setPlatformThicknessTol(Double platformThicknessTol) {
        this.platformThicknessTol = platformThicknessTol;
    }

    public CriticalDimensionEnumVal getThreadCallout() {
        return threadCallout;
    }

    public void setThreadCallout(CriticalDimensionEnumVal threadCallout) {
        this.threadCallout = threadCallout;
    }

    public CriticalDimensionEnumVal getThreadHand() {
        return threadHand;
    }

    public void setThreadHand(CriticalDimensionEnumVal threadHand) {
        this.threadHand = threadHand;
    }

    public Double getPlatformDiameterF() {
        return platformDiameterF;
    }

    public void setPlatformDiameterF(Double platformDiameterF) {
        this.platformDiameterF = platformDiameterF;
    }

    public Double getPlatformDiameterFTol() {
        return platformDiameterFTol;
    }

    public void setPlatformDiameterFTol(Double platformDiameterFTol) {
        this.platformDiameterFTol = platformDiameterFTol;
    }

    public Double getOverallHeightC() {
        return overallHeightC;
    }

    public void setOverallHeightC(Double overallHeightC) {
        this.overallHeightC = overallHeightC;
    }

    public Double getNoseDiameterG() {
        return noseDiameterG;
    }

    public void setNoseDiameterG(Double noseDiameterG) {
        this.noseDiameterG = noseDiameterG;
    }

    public Double getFootDiameterH() {
        return footDiameterH;
    }

    public void setFootDiameterH(Double footDiameterH) {
        this.footDiameterH = footDiameterH;
    }

    public Double getBladeHeight() {
        return bladeHeight;
    }

    public void setBladeHeight(Double bladeHeight) {
        this.bladeHeight = bladeHeight;
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
