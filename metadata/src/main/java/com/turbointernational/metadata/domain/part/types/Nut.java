package com.turbointernational.metadata.domain.part.types;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.domain.criticaldimension.CriticalDimensionEnumVal;
import com.turbointernational.metadata.domain.part.Part;
import com.turbointernational.metadata.web.View;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;


/**
 * Created by dmytro.trunykov@zorallabs.com on 2016-05-26 10:44:43.039105.
 */
@Entity
@Table(name = "nut")
@PrimaryKeyJoinColumn(name = "part_id")
public class Nut extends Part {

    //<editor-fold defaultstate="collapsed" desc="Properties: critical dimensions">

    @JsonView(View.Summary.class)
    @JsonProperty("shaftNut")
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "shaftNut")
    private CriticalDimensionEnumVal shaftNut;

    @JsonView(View.Summary.class)
    @JsonProperty("type")
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "type")
    private CriticalDimensionEnumVal type;

    @JsonView(View.Summary.class)
    @JsonProperty("flange")
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "flange")
    private CriticalDimensionEnumVal flange;

    @JsonView(View.Summary.class)
    @JsonProperty("driveType")
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "driveType")
    private CriticalDimensionEnumVal driveType;

    @JsonView(View.Summary.class)
    @JsonProperty("thread")
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "thread")
    private CriticalDimensionEnumVal thread;

    @JsonView(View.Summary.class)
    @JsonProperty("thdHand")
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "thdHand")
    private CriticalDimensionEnumVal thdHand;

    @JsonView(View.Summary.class)
    @JsonProperty("threadLength")
    @Column(name = "threadLength")
    private Double threadLength;

    @JsonView(View.Summary.class)
    @JsonProperty("oal")
    @Column(name = "oal")
    private Double oal;

    @JsonView(View.Summary.class)
    @JsonProperty("oalTol")
    @Column(name = "oalTol")
    private Double oalTol;

    @JsonView(View.Summary.class)
    @JsonProperty("flangeDia")
    @Column(name = "flangeDia")
    private Double flangeDia;

    @JsonView(View.Summary.class)
    @JsonProperty("flangeDiaTol")
    @Column(name = "flangeDiaTol")
    private Double flangeDiaTol;

    @JsonView(View.Summary.class)
    @JsonProperty("grade")
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "grade")
    private CriticalDimensionEnumVal grade;

    @JsonView(View.Summary.class)
    @JsonProperty("platingCoating")
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "platingCoating")
    private CriticalDimensionEnumVal platingCoating;

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

    @JsonView(View.Summary.class)
    @JsonProperty("prevailingTorqueType")
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "prevailingTorqueType")
    private CriticalDimensionEnumVal prevailingTorqueType;

    @JsonView(View.Summary.class)
    @JsonProperty("diagram")
    @Column(name = "diagram")
    private Integer diagram;

    @JsonView(View.Summary.class)
    @JsonProperty("acrossFlats")
    @Column(name = "acrossFlats")
    private Double acrossFlats;

    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Getters and setters: critical dimensions">

    public CriticalDimensionEnumVal getShaftNut() {
        return shaftNut;
    }

    public void setShaftNut(CriticalDimensionEnumVal shaftNut) {
        this.shaftNut = shaftNut;
    }

    public CriticalDimensionEnumVal getType() {
        return type;
    }

    public void setType(CriticalDimensionEnumVal type) {
        this.type = type;
    }

    public CriticalDimensionEnumVal getFlange() {
        return flange;
    }

    public void setFlange(CriticalDimensionEnumVal flange) {
        this.flange = flange;
    }

    public CriticalDimensionEnumVal getDriveType() {
        return driveType;
    }

    public void setDriveType(CriticalDimensionEnumVal driveType) {
        this.driveType = driveType;
    }

    public CriticalDimensionEnumVal getThread() {
        return thread;
    }

    public void setThread(CriticalDimensionEnumVal thread) {
        this.thread = thread;
    }

    public CriticalDimensionEnumVal getThdHand() {
        return thdHand;
    }

    public void setThdHand(CriticalDimensionEnumVal thdHand) {
        this.thdHand = thdHand;
    }

    public Double getThreadLength() {
        return threadLength;
    }

    public void setThreadLength(Double threadLength) {
        this.threadLength = threadLength;
    }

    public Double getOal() {
        return oal;
    }

    public void setOal(Double oal) {
        this.oal = oal;
    }

    public Double getOalTol() {
        return oalTol;
    }

    public void setOalTol(Double oalTol) {
        this.oalTol = oalTol;
    }

    public Double getFlangeDia() {
        return flangeDia;
    }

    public void setFlangeDia(Double flangeDia) {
        this.flangeDia = flangeDia;
    }

    public Double getFlangeDiaTol() {
        return flangeDiaTol;
    }

    public void setFlangeDiaTol(Double flangeDiaTol) {
        this.flangeDiaTol = flangeDiaTol;
    }

    public CriticalDimensionEnumVal getGrade() {
        return grade;
    }

    public void setGrade(CriticalDimensionEnumVal grade) {
        this.grade = grade;
    }

    public CriticalDimensionEnumVal getPlatingCoating() {
        return platingCoating;
    }

    public void setPlatingCoating(CriticalDimensionEnumVal platingCoating) {
        this.platingCoating = platingCoating;
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

    public CriticalDimensionEnumVal getPrevailingTorqueType() {
        return prevailingTorqueType;
    }

    public void setPrevailingTorqueType(CriticalDimensionEnumVal prevailingTorqueType) {
        this.prevailingTorqueType = prevailingTorqueType;
    }

    public Integer getDiagram() {
        return diagram;
    }

    public void setDiagram(Integer diagram) {
        this.diagram = diagram;
    }

    public Double getAcrossFlats() {
        return acrossFlats;
    }

    public void setAcrossFlats(Double acrossFlats) {
        this.acrossFlats = acrossFlats;
    }

    //</editor-fold>

}
