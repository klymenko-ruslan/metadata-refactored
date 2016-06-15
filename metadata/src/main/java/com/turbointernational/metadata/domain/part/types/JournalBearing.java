package com.turbointernational.metadata.domain.part.types;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.domain.criticaldimension.CriticalDimensionEnumVal;
import com.turbointernational.metadata.domain.part.Part;
import com.turbointernational.metadata.web.View;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;


/**
 * Created by dmytro.trunykov@zorallabs.com on 2016-06-15 17:07:35.987834.
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

    public Double getMaxOuterDiameter() {
        return maxOuterDiameter;
    }

    public void setMaxOuterDiameter(Double maxOuterDiameter) {
        this.maxOuterDiameter = maxOuterDiameter;
    }

    public Double getMinOuterDiameter() {
        return minOuterDiameter;
    }

    public void setMinOuterDiameter(Double minOuterDiameter) {
        this.minOuterDiameter = minOuterDiameter;
    }

    public Double getMaxInnerDiameter() {
        return maxInnerDiameter;
    }

    public void setMaxInnerDiameter(Double maxInnerDiameter) {
        this.maxInnerDiameter = maxInnerDiameter;
    }

    public Double getMinInnerDiameter() {
        return minInnerDiameter;
    }

    public void setMinInnerDiameter(Double minInnerDiameter) {
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

    public Double getLengthTol() {
        return lengthTol;
    }

    public void setLengthTol(Double lengthTol) {
        this.lengthTol = lengthTol;
    }

    public Integer getFeedHoleCount() {
        return feedHoleCount;
    }

    public void setFeedHoleCount(Integer feedHoleCount) {
        this.feedHoleCount = feedHoleCount;
    }

    public Double getFeedHoleDiameter() {
        return feedHoleDiameter;
    }

    public void setFeedHoleDiameter(Double feedHoleDiameter) {
        this.feedHoleDiameter = feedHoleDiameter;
    }

    public CriticalDimensionEnumVal getFreePinned() {
        return freePinned;
    }

    public void setFreePinned(CriticalDimensionEnumVal freePinned) {
        this.freePinned = freePinned;
    }

    public Integer getBearingsPerCartridge() {
        return bearingsPerCartridge;
    }

    public void setBearingsPerCartridge(Integer bearingsPerCartridge) {
        this.bearingsPerCartridge = bearingsPerCartridge;
    }

    public CriticalDimensionEnumVal getOilFeed() {
        return oilFeed;
    }

    public void setOilFeed(CriticalDimensionEnumVal oilFeed) {
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

    public Double getCenterDiameterOd() {
        return centerDiameterOd;
    }

    public void setCenterDiameterOd(Double centerDiameterOd) {
        this.centerDiameterOd = centerDiameterOd;
    }

    public CriticalDimensionEnumVal getEndConfiguration() {
        return endConfiguration;
    }

    public void setEndConfiguration(CriticalDimensionEnumVal endConfiguration) {
        this.endConfiguration = endConfiguration;
    }

    public CriticalDimensionEnumVal getBoreConfiguration() {
        return boreConfiguration;
    }

    public void setBoreConfiguration(CriticalDimensionEnumVal boreConfiguration) {
        this.boreConfiguration = boreConfiguration;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public String getSpecialFeatures() {
        return specialFeatures;
    }

    public void setSpecialFeatures(String specialFeatures) {
        this.specialFeatures = specialFeatures;
    }

    public CriticalDimensionEnumVal getSpecialFeaturesOnOff() {
        return specialFeaturesOnOff;
    }

    public void setSpecialFeaturesOnOff(CriticalDimensionEnumVal specialFeaturesOnOff) {
        this.specialFeaturesOnOff = specialFeaturesOnOff;
    }

    public Integer getBrgSurfaces() {
        return brgSurfaces;
    }

    public void setBrgSurfaces(Integer brgSurfaces) {
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
