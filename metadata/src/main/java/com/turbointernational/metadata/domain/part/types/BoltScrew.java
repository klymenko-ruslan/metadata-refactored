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
@Table(name = "bolt_screw")
@PrimaryKeyJoinColumn(name = "part_id")
public class BoltScrew extends Part {
    //<editor-fold defaultstate="collapsed" desc="Properties: critical dimensions">
    @JsonView(View.Summary.class)
    @JsonProperty("threadCalloutA")
    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "threadCalloutA")
    private CriticalDimensionEnumVal threadCalloutA;

    @JsonView(View.Summary.class)
    @JsonProperty("threadLengthB")
    @Column(name = "threadLengthB")
    private Double threadLengthB;

    @JsonView(View.Summary.class)
    @JsonProperty("threadHand")
    @ManyToOne(fetch = EAGER)
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
    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "headType")
    private CriticalDimensionEnumVal headType;

    @JsonView(View.Summary.class)
    @JsonProperty("driveType")
    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "driveType")
    private CriticalDimensionEnumVal driveType;

    @JsonView(View.Summary.class)
    @JsonProperty("driveSize")
    @ManyToOne(fetch = EAGER)
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
    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "prevailingTorqueYn")
    private CriticalDimensionEnumVal prevailingTorqueYn;

    @JsonView(View.Summary.class)
    @JsonProperty("threadLockCompoundType")
    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "threadLockCompoundType")
    private CriticalDimensionEnumVal threadLockCompoundType;

    @JsonView(View.Summary.class)
    @JsonProperty("platingCoating")
    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "platingCoating")
    private CriticalDimensionEnumVal platingCoating;

    @JsonView(View.Summary.class)
    @JsonProperty("propertyClassOrGrade")
    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "propertyClassOrGrade")
    private CriticalDimensionEnumVal propertyClassOrGrade;

    @JsonView(View.Summary.class)
    @JsonProperty("specialFeatures")
    @Column(name = "specialFeatures")
    private String specialFeatures;

    @JsonView(View.Summary.class)
    @JsonProperty("specialFeaturesOnOff")
    @ManyToOne(fetch = EAGER)
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
    public CriticalDimensionEnumVal getThreadcallouta() {
        return threadCalloutA;
    }

    public void setThreadcallouta(CriticalDimensionEnumVal threadCalloutA) {
        this.threadCalloutA = threadCalloutA;
    }

    public Double getThreadlengthb() {
        return threadLengthB;
    }

    public void setThreadlengthb(Double threadLengthB) {
        this.threadLengthB = threadLengthB;
    }

    public CriticalDimensionEnumVal getThreadhand() {
        return threadHand;
    }

    public void setThreadhand(CriticalDimensionEnumVal threadHand) {
        this.threadHand = threadHand;
    }

    public Double getOveralllength() {
        return overallLength;
    }

    public void setOveralllength(Double overallLength) {
        this.overallLength = overallLength;
    }

    public Double getHeadheightc() {
        return headHeightC;
    }

    public void setHeadheightc(Double headHeightC) {
        this.headHeightC = headHeightC;
    }

    public Double getAcrossflatsd() {
        return acrossFlatsD;
    }

    public void setAcrossflatsd(Double acrossFlatsD) {
        this.acrossFlatsD = acrossFlatsD;
    }

    public CriticalDimensionEnumVal getHeadtype() {
        return headType;
    }

    public void setHeadtype(CriticalDimensionEnumVal headType) {
        this.headType = headType;
    }

    public CriticalDimensionEnumVal getDrivetype() {
        return driveType;
    }

    public void setDrivetype(CriticalDimensionEnumVal driveType) {
        this.driveType = driveType;
    }

    public CriticalDimensionEnumVal getDrivesize() {
        return driveSize;
    }

    public void setDrivesize(CriticalDimensionEnumVal driveSize) {
        this.driveSize = driveSize;
    }

    public Double getHeaddiameter() {
        return headDiameter;
    }

    public void setHeaddiameter(Double headDiameter) {
        this.headDiameter = headDiameter;
    }

    public Double getFlangediameter() {
        return flangeDiameter;
    }

    public void setFlangediameter(Double flangeDiameter) {
        this.flangeDiameter = flangeDiameter;
    }

    public CriticalDimensionEnumVal getPrevailingtorqueyn() {
        return prevailingTorqueYn;
    }

    public void setPrevailingtorqueyn(CriticalDimensionEnumVal prevailingTorqueYn) {
        this.prevailingTorqueYn = prevailingTorqueYn;
    }

    public CriticalDimensionEnumVal getThreadlockcompoundtype() {
        return threadLockCompoundType;
    }

    public void setThreadlockcompoundtype(CriticalDimensionEnumVal threadLockCompoundType) {
        this.threadLockCompoundType = threadLockCompoundType;
    }

    public CriticalDimensionEnumVal getPlatingcoating() {
        return platingCoating;
    }

    public void setPlatingcoating(CriticalDimensionEnumVal platingCoating) {
        this.platingCoating = platingCoating;
    }

    public CriticalDimensionEnumVal getPropertyclassorgrade() {
        return propertyClassOrGrade;
    }

    public void setPropertyclassorgrade(CriticalDimensionEnumVal propertyClassOrGrade) {
        this.propertyClassOrGrade = propertyClassOrGrade;
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
