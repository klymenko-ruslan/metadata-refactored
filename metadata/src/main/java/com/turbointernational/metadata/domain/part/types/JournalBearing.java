package com.turbointernational.metadata.domain.part.types;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.domain.criticaldimension.CriticalDimensionEnumVal;
import com.turbointernational.metadata.domain.part.Part;
import com.turbointernational.metadata.web.View;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;


/**
 * Created by dmytro.trunykov@zorallabs.com.
 */
@Entity
@Table(name = "journal_bearing")
@PrimaryKeyJoinColumn(name = "part_id")
public class JournalBearing extends Part {

    //<editor-fold defaultstate="collapsed" desc="Properties: critical dimensions">
    @JsonView(View.Summary.class)
    @JsonProperty("maxOuterDiameter")
    @Column(name = "maxOuterDiameter")
    private Double maxOuterDiameter;

    @JsonView(View.Summary.class)
    @JsonProperty("minOuterDiameter")
    @Column(name = "minOuterDiameter")
    private Double minOuterDiameter;

    @JsonView(View.Summary.class)
    @JsonProperty("maxInnerDiameter")
    @Column(name = "maxInnerDiameter")
    private Double maxInnerDiameter;

    @JsonView(View.Summary.class)
    @JsonProperty("minInnerDiameter")
    @Column(name = "minInnerDiameter")
    private Double minInnerDiameter;

    @JsonView(View.Summary.class)
    @JsonProperty("size")
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "size")
    private CriticalDimensionEnumVal size;

    @JsonView(View.Summary.class)
    @JsonProperty("length")
    @Column(name = "length")
    private Double length;

    @JsonView(View.Summary.class)
    @JsonProperty("lengthTol")
    @Column(name = "lengthTol")
    private Double lengthTol;

    @JsonView(View.Summary.class)
    @JsonProperty("feedHoleCount")
    @Column(name = "feedHoleCount")
    private Integer feedHoleCount;

    @JsonView(View.Summary.class)
    @JsonProperty("feedHoleDiameter")
    @Column(name = "feedHoleDiameter")
    private Double feedHoleDiameter;

    @JsonView(View.Summary.class)
    @JsonProperty("freePinned")
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "freePinned")
    private CriticalDimensionEnumVal freePinned;

    @JsonView(View.Summary.class)
    @JsonProperty("bearingsPerCartridge")
    @Column(name = "bearingsPerCartridge")
    private Integer bearingsPerCartridge;

    @JsonView(View.Summary.class)
    @JsonProperty("oilFeed")
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "oilFeed")
    private CriticalDimensionEnumVal oilFeed;

    @JsonView(View.Summary.class)
    @JsonProperty("rotation")
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "rotation")
    private CriticalDimensionEnumVal rotation;

    @JsonView(View.Summary.class)
    @JsonProperty("material")
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "material")
    private CriticalDimensionEnumVal material;

    @JsonView(View.Summary.class)
    @JsonProperty("centerDiameterOd")
    @Column(name = "centerDiameterOd")
    private Double centerDiameterOd;

    @JsonView(View.Summary.class)
    @JsonProperty("endConfiguration")
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "endConfiguration")
    private CriticalDimensionEnumVal endConfiguration;

    @JsonView(View.Summary.class)
    @JsonProperty("boreConfiguration")
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "boreConfiguration")
    private CriticalDimensionEnumVal boreConfiguration;

    @JsonView(View.Summary.class)
    @JsonProperty("weight")
    @Column(name = "weight")
    private Double weight;

    @JsonView(View.Summary.class)
    @JsonProperty("specialFeatures")
    @Column(name = "specialFeatures")
    private String specialFeatures;

    @JsonView(View.Summary.class)
    @JsonProperty("specialFeaturesOnOff")
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "specialFeaturesOnOff")
    private CriticalDimensionEnumVal specialFeaturesOnOff;

    @JsonView(View.Summary.class)
    @JsonProperty("brgSurfaces")
    @Column(name = "brgSurfaces")
    private Integer brgSurfaces;

    @JsonView(View.Summary.class)
    @JsonProperty("diagram")
    @Column(name = "diagram")
    private Integer diagram;


    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Getters and setters: critical dimensions">
    public Double getMaxouterdiameter() {
        return maxOuterDiameter;
    }

    public void setMaxouterdiameter(Double maxOuterDiameter) {
        this.maxOuterDiameter = maxOuterDiameter;
    }

    public Double getMinouterdiameter() {
        return minOuterDiameter;
    }

    public void setMinouterdiameter(Double minOuterDiameter) {
        this.minOuterDiameter = minOuterDiameter;
    }

    public Double getMaxinnerdiameter() {
        return maxInnerDiameter;
    }

    public void setMaxinnerdiameter(Double maxInnerDiameter) {
        this.maxInnerDiameter = maxInnerDiameter;
    }

    public Double getMininnerdiameter() {
        return minInnerDiameter;
    }

    public void setMininnerdiameter(Double minInnerDiameter) {
        this.minInnerDiameter = minInnerDiameter;
    }

    public CriticalDimensionEnumVal getSize() {
        return size;
    }

    public void setSize(CriticalDimensionEnumVal size) {
        this.size = size;
    }

    public Double getLength() {
        return length;
    }

    public void setLength(Double length) {
        this.length = length;
    }

    public Double getLengthtol() {
        return lengthTol;
    }

    public void setLengthtol(Double lengthTol) {
        this.lengthTol = lengthTol;
    }

    public Integer getFeedholecount() {
        return feedHoleCount;
    }

    public void setFeedholecount(Integer feedHoleCount) {
        this.feedHoleCount = feedHoleCount;
    }

    public Double getFeedholediameter() {
        return feedHoleDiameter;
    }

    public void setFeedholediameter(Double feedHoleDiameter) {
        this.feedHoleDiameter = feedHoleDiameter;
    }

    public CriticalDimensionEnumVal getFreepinned() {
        return freePinned;
    }

    public void setFreepinned(CriticalDimensionEnumVal freePinned) {
        this.freePinned = freePinned;
    }

    public Integer getBearingspercartridge() {
        return bearingsPerCartridge;
    }

    public void setBearingspercartridge(Integer bearingsPerCartridge) {
        this.bearingsPerCartridge = bearingsPerCartridge;
    }

    public CriticalDimensionEnumVal getOilfeed() {
        return oilFeed;
    }

    public void setOilfeed(CriticalDimensionEnumVal oilFeed) {
        this.oilFeed = oilFeed;
    }

    public CriticalDimensionEnumVal getRotation() {
        return rotation;
    }

    public void setRotation(CriticalDimensionEnumVal rotation) {
        this.rotation = rotation;
    }

    public CriticalDimensionEnumVal getMaterial() {
        return material;
    }

    public void setMaterial(CriticalDimensionEnumVal material) {
        this.material = material;
    }

    public Double getCenterdiameterod() {
        return centerDiameterOd;
    }

    public void setCenterdiameterod(Double centerDiameterOd) {
        this.centerDiameterOd = centerDiameterOd;
    }

    public CriticalDimensionEnumVal getEndconfiguration() {
        return endConfiguration;
    }

    public void setEndconfiguration(CriticalDimensionEnumVal endConfiguration) {
        this.endConfiguration = endConfiguration;
    }

    public CriticalDimensionEnumVal getBoreconfiguration() {
        return boreConfiguration;
    }

    public void setBoreconfiguration(CriticalDimensionEnumVal boreConfiguration) {
        this.boreConfiguration = boreConfiguration;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public String getSpecialfeatures() {
        return specialFeatures;
    }

    public void setSpecialfeatures(String specialFeatures) {
        this.specialFeatures = specialFeatures;
    }

    public CriticalDimensionEnumVal getSpecialfeaturesonoff() {
        return specialFeaturesOnOff;
    }

    public void setSpecialfeaturesonoff(CriticalDimensionEnumVal specialFeaturesOnOff) {
        this.specialFeaturesOnOff = specialFeaturesOnOff;
    }

    public Integer getBrgsurfaces() {
        return brgSurfaces;
    }

    public void setBrgsurfaces(Integer brgSurfaces) {
        this.brgSurfaces = brgSurfaces;
    }

    public Integer getDiagram() {
        return diagram;
    }

    public void setDiagram(Integer diagram) {
        this.diagram = diagram;
    }


    //</editor-fold>
}
