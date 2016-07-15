package com.turbointernational.metadata.domain.part.types;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.domain.criticaldimension.CriticalDimensionEnumVal;
import com.turbointernational.metadata.domain.part.Part;
import com.turbointernational.metadata.web.View;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;


/**
 * Created by dmytro.trunykov@zorallabs.com on 2016-07-15 14:03:20.418983.
 */
@Entity
@Table(name = "bolt_screw")
@PrimaryKeyJoinColumn(name = "part_id")
public class BoltScrew extends Part {

    //<editor-fold defaultstate="collapsed" desc="Properties: critical dimensions">

    @JsonView(View.Summary.class)
    @JsonProperty("threadCalloutA")
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "threadCalloutA")
    private CriticalDimensionEnumVal threadCalloutA;

    @JsonView(View.Summary.class)
    @JsonProperty("threadLengthB")
    @Column(name = "threadLengthB")
    private Double threadLengthB;

    @JsonView(View.Summary.class)
    @JsonProperty("threadHand")
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "threadHand")
    private CriticalDimensionEnumVal threadHand;

    @JsonView(View.Summary.class)
    @JsonProperty("overallLength")
    @Column(name = "overallLength")
    private Double overallLength;

    @JsonView(View.Summary.class)
    @JsonProperty("headHeightC")
    @Column(name = "headHeightC")
    private Double headHeightC;

    @JsonView(View.Summary.class)
    @JsonProperty("acrossFlatsD")
    @Column(name = "acrossFlatsD")
    private Double acrossFlatsD;

    @JsonView(View.Summary.class)
    @JsonProperty("headType")
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "headType")
    private CriticalDimensionEnumVal headType;

    @JsonView(View.Summary.class)
    @JsonProperty("driveType")
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "driveType")
    private CriticalDimensionEnumVal driveType;

    @JsonView(View.Summary.class)
    @JsonProperty("driveSize")
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "driveSize")
    private CriticalDimensionEnumVal driveSize;

    @JsonView(View.Summary.class)
    @JsonProperty("headDiameter")
    @Column(name = "headDiameter")
    private Double headDiameter;

    @JsonView(View.Summary.class)
    @JsonProperty("flangeDiameter")
    @Column(name = "flangeDiameter")
    private Double flangeDiameter;

    @JsonView(View.Summary.class)
    @JsonProperty("prevailingTorqueYn")
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "prevailingTorqueYn")
    private CriticalDimensionEnumVal prevailingTorqueYn;

    @JsonView(View.Summary.class)
    @JsonProperty("threadLockCompoundType")
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "threadLockCompoundType")
    private CriticalDimensionEnumVal threadLockCompoundType;

    @JsonView(View.Summary.class)
    @JsonProperty("platingCoating")
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "platingCoating")
    private CriticalDimensionEnumVal platingCoating;

    @JsonView(View.Summary.class)
    @JsonProperty("propertyClassOrGrade")
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "propertyClassOrGrade")
    private CriticalDimensionEnumVal propertyClassOrGrade;

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
    @JsonProperty("weight")
    @Column(name = "weight")
    private Double weight;

    @JsonView(View.Summary.class)
    @JsonProperty("diagram")
    @Column(name = "diagram")
    private Integer diagram;

    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Getters and setters: critical dimensions">

    public CriticalDimensionEnumVal getThreadCalloutA() {
        return threadCalloutA;
    }

    public void setThreadCalloutA(CriticalDimensionEnumVal threadCalloutA) {
        this.threadCalloutA = threadCalloutA;
    }

    public Double getThreadLengthB() {
        return threadLengthB;
    }

    public void setThreadLengthB(Double threadLengthB) {
        this.threadLengthB = threadLengthB;
    }

    public CriticalDimensionEnumVal getThreadHand() {
        return threadHand;
    }

    public void setThreadHand(CriticalDimensionEnumVal threadHand) {
        this.threadHand = threadHand;
    }

    public Double getOverallLength() {
        return overallLength;
    }

    public void setOverallLength(Double overallLength) {
        this.overallLength = overallLength;
    }

    public Double getHeadHeightC() {
        return headHeightC;
    }

    public void setHeadHeightC(Double headHeightC) {
        this.headHeightC = headHeightC;
    }

    public Double getAcrossFlatsD() {
        return acrossFlatsD;
    }

    public void setAcrossFlatsD(Double acrossFlatsD) {
        this.acrossFlatsD = acrossFlatsD;
    }

    public CriticalDimensionEnumVal getHeadType() {
        return headType;
    }

    public void setHeadType(CriticalDimensionEnumVal headType) {
        this.headType = headType;
    }

    public CriticalDimensionEnumVal getDriveType() {
        return driveType;
    }

    public void setDriveType(CriticalDimensionEnumVal driveType) {
        this.driveType = driveType;
    }

    public CriticalDimensionEnumVal getDriveSize() {
        return driveSize;
    }

    public void setDriveSize(CriticalDimensionEnumVal driveSize) {
        this.driveSize = driveSize;
    }

    public Double getHeadDiameter() {
        return headDiameter;
    }

    public void setHeadDiameter(Double headDiameter) {
        this.headDiameter = headDiameter;
    }

    public Double getFlangeDiameter() {
        return flangeDiameter;
    }

    public void setFlangeDiameter(Double flangeDiameter) {
        this.flangeDiameter = flangeDiameter;
    }

    public CriticalDimensionEnumVal getPrevailingTorqueYn() {
        return prevailingTorqueYn;
    }

    public void setPrevailingTorqueYn(CriticalDimensionEnumVal prevailingTorqueYn) {
        this.prevailingTorqueYn = prevailingTorqueYn;
    }

    public CriticalDimensionEnumVal getThreadLockCompoundType() {
        return threadLockCompoundType;
    }

    public void setThreadLockCompoundType(CriticalDimensionEnumVal threadLockCompoundType) {
        this.threadLockCompoundType = threadLockCompoundType;
    }

    public CriticalDimensionEnumVal getPlatingCoating() {
        return platingCoating;
    }

    public void setPlatingCoating(CriticalDimensionEnumVal platingCoating) {
        this.platingCoating = platingCoating;
    }

    public CriticalDimensionEnumVal getPropertyClassOrGrade() {
        return propertyClassOrGrade;
    }

    public void setPropertyClassOrGrade(CriticalDimensionEnumVal propertyClassOrGrade) {
        this.propertyClassOrGrade = propertyClassOrGrade;
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
