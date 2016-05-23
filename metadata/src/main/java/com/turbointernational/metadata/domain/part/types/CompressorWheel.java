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
@Table(name = "compressor_wheel")
@PrimaryKeyJoinColumn(name = "part_id")
public class CompressorWheel extends Part {
    //<editor-fold defaultstate="collapsed" desc="Properties: critical dimensions">
    @JsonView(View.Summary.class)
    @JsonProperty("rotation")
    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "rotation")
    private CriticalDimensionEnumVal rotation;

    @JsonView(View.Summary.class)
    @JsonProperty("flatbackSuperback")
    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "flatbackSuperback")
    private CriticalDimensionEnumVal flatbackSuperback;

    @JsonView(View.Summary.class)
    @JsonProperty("extendedTips")
    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "extendedTips")
    private CriticalDimensionEnumVal extendedTips;

    @JsonView(View.Summary.class)
    @JsonProperty("threadedBore")
    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "threadedBore")
    private CriticalDimensionEnumVal threadedBore;

    @JsonView(View.Summary.class)
    @JsonProperty("boreless")
    @ManyToOne(fetch = EAGER)
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
    @JsonProperty("exducerBDiameter")
    @Column(name = "exducerBDiameter")
    private Double exducerBDiameter;

    @JsonView(View.Summary.class)
    @JsonProperty("exducerBDiameterTol")
    @Column(name = "exducerBDiameterTol")
    private Double exducerBDiameterTol;

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
    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "bladeCount")
    private CriticalDimensionEnumVal bladeCount;

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
    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "threadCallout")
    private CriticalDimensionEnumVal threadCallout;

    @JsonView(View.Summary.class)
    @JsonProperty("threadHand")
    @ManyToOne(fetch = EAGER)
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

    public CriticalDimensionEnumVal getFlatbacksuperback() {
        return flatbackSuperback;
    }

    public void setFlatbacksuperback(CriticalDimensionEnumVal flatbackSuperback) {
        this.flatbackSuperback = flatbackSuperback;
    }

    public CriticalDimensionEnumVal getExtendedtips() {
        return extendedTips;
    }

    public void setExtendedtips(CriticalDimensionEnumVal extendedTips) {
        this.extendedTips = extendedTips;
    }

    public CriticalDimensionEnumVal getThreadedbore() {
        return threadedBore;
    }

    public void setThreadedbore(CriticalDimensionEnumVal threadedBore) {
        this.threadedBore = threadedBore;
    }

    public CriticalDimensionEnumVal getBoreless() {
        return boreless;
    }

    public void setBoreless(CriticalDimensionEnumVal boreless) {
        this.boreless = boreless;
    }

    public Double getInducerdiametera() {
        return inducerDiameterA;
    }

    public void setInducerdiametera(Double inducerDiameterA) {
        this.inducerDiameterA = inducerDiameterA;
    }

    public Double getInducerdiameteratol() {
        return inducerDiameterATol;
    }

    public void setInducerdiameteratol(Double inducerDiameterATol) {
        this.inducerDiameterATol = inducerDiameterATol;
    }

    public Double getExducerbdiameter() {
        return exducerBDiameter;
    }

    public void setExducerbdiameter(Double exducerBDiameter) {
        this.exducerBDiameter = exducerBDiameter;
    }

    public Double getExducerbdiametertol() {
        return exducerBDiameterTol;
    }

    public void setExducerbdiametertol(Double exducerBDiameterTol) {
        this.exducerBDiameterTol = exducerBDiameterTol;
    }

    public Double getTiplocation() {
        return tipLocation;
    }

    public void setTiplocation(Double tipLocation) {
        this.tipLocation = tipLocation;
    }

    public Double getTiplocationtol() {
        return tipLocationTol;
    }

    public void setTiplocationtol(Double tipLocationTol) {
        this.tipLocationTol = tipLocationTol;
    }

    public Double getTipheightd() {
        return tipHeightD;
    }

    public void setTipheightd(Double tipHeightD) {
        this.tipHeightD = tipHeightD;
    }

    public Double getTipheightdtol() {
        return tipHeightDTol;
    }

    public void setTipheightdtol(Double tipHeightDTol) {
        this.tipHeightDTol = tipHeightDTol;
    }

    public Double getPlatformheighte() {
        return platformHeightE;
    }

    public void setPlatformheighte(Double platformHeightE) {
        this.platformHeightE = platformHeightE;
    }

    public Double getPlatformheighttol() {
        return platformHeightTol;
    }

    public void setPlatformheighttol(Double platformHeightTol) {
        this.platformHeightTol = platformHeightTol;
    }

    public Double getMaxborediameter() {
        return maxBoreDiameter;
    }

    public void setMaxborediameter(Double maxBoreDiameter) {
        this.maxBoreDiameter = maxBoreDiameter;
    }

    public Double getMinborediameter() {
        return minBoreDiameter;
    }

    public void setMinborediameter(Double minBoreDiameter) {
        this.minBoreDiameter = minBoreDiameter;
    }

    public CriticalDimensionEnumVal getBladecount() {
        return bladeCount;
    }

    public void setBladecount(CriticalDimensionEnumVal bladeCount) {
        this.bladeCount = bladeCount;
    }

    public Double getPlatformthickness() {
        return platformThickness;
    }

    public void setPlatformthickness(Double platformThickness) {
        this.platformThickness = platformThickness;
    }

    public Double getPlatformthicknesstol() {
        return platformThicknessTol;
    }

    public void setPlatformthicknesstol(Double platformThicknessTol) {
        this.platformThicknessTol = platformThicknessTol;
    }

    public CriticalDimensionEnumVal getThreadcallout() {
        return threadCallout;
    }

    public void setThreadcallout(CriticalDimensionEnumVal threadCallout) {
        this.threadCallout = threadCallout;
    }

    public CriticalDimensionEnumVal getThreadhand() {
        return threadHand;
    }

    public void setThreadhand(CriticalDimensionEnumVal threadHand) {
        this.threadHand = threadHand;
    }

    public Double getPlatformdiameterf() {
        return platformDiameterF;
    }

    public void setPlatformdiameterf(Double platformDiameterF) {
        this.platformDiameterF = platformDiameterF;
    }

    public Double getPlatformdiameterftol() {
        return platformDiameterFTol;
    }

    public void setPlatformdiameterftol(Double platformDiameterFTol) {
        this.platformDiameterFTol = platformDiameterFTol;
    }

    public Double getOverallheightc() {
        return overallHeightC;
    }

    public void setOverallheightc(Double overallHeightC) {
        this.overallHeightC = overallHeightC;
    }

    public Double getNosediameterg() {
        return noseDiameterG;
    }

    public void setNosediameterg(Double noseDiameterG) {
        this.noseDiameterG = noseDiameterG;
    }

    public Double getFootdiameterh() {
        return footDiameterH;
    }

    public void setFootdiameterh(Double footDiameterH) {
        this.footDiameterH = footDiameterH;
    }

    public Double getBladeheight() {
        return bladeHeight;
    }

    public void setBladeheight(Double bladeHeight) {
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
