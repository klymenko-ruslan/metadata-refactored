package com.turbointernational.metadata.domain.part.types;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.domain.criticaldimension.CriticalDimensionEnumVal;
import com.turbointernational.metadata.domain.part.Part;
import com.turbointernational.metadata.web.View;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;


/**
 * Created by dmytro.trunykov@zorallabs.com on 2016-06-14 17:51:47.552816.
 */
@Entity
@Table(name = "bearing_housing")
@PrimaryKeyJoinColumn(name = "part_id")
public class BearingHousing extends Part {

    //<editor-fold defaultstate="collapsed" desc="Properties: critical dimensions">

    @JsonView(View.Summary.class)
    @JsonProperty("waterCooled")
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "waterCooled")
    private CriticalDimensionEnumVal waterCooled;

    @JsonView(View.Summary.class)
    @JsonProperty("cwcDia")
    @Column(name = "cwcDia")
    private Double cwcDia;

    @JsonView(View.Summary.class)
    @JsonProperty("cwcDiaTol")
    @Column(name = "cwcDiaTol")
    private Double cwcDiaTol;

    @JsonView(View.Summary.class)
    @JsonProperty("boreDiaMax")
    @Column(name = "boreDiaMax")
    private Double boreDiaMax;

    @JsonView(View.Summary.class)
    @JsonProperty("boreDiaMin")
    @Column(name = "boreDiaMin")
    private Double boreDiaMin;

    @JsonView(View.Summary.class)
    @JsonProperty("ceDiaA")
    @Column(name = "ceDiaA")
    private Double ceDiaA;

    @JsonView(View.Summary.class)
    @JsonProperty("ceDiaATol")
    @Column(name = "ceDiaATol")
    private Double ceDiaATol;

    @JsonView(View.Summary.class)
    @JsonProperty("teDiaD")
    @Column(name = "teDiaD")
    private Double teDiaD;

    @JsonView(View.Summary.class)
    @JsonProperty("teDiaDTol")
    @Column(name = "teDiaDTol")
    private Double teDiaDTol;

    @JsonView(View.Summary.class)
    @JsonProperty("ceDiaB")
    @Column(name = "ceDiaB")
    private Double ceDiaB;

    @JsonView(View.Summary.class)
    @JsonProperty("ceDiaBTol")
    @Column(name = "ceDiaBTol")
    private Double ceDiaBTol;

    @JsonView(View.Summary.class)
    @JsonProperty("ceDiaC")
    @Column(name = "ceDiaC")
    private Double ceDiaC;

    @JsonView(View.Summary.class)
    @JsonProperty("ceDiaCTol")
    @Column(name = "ceDiaCTol")
    private Double ceDiaCTol;

    @JsonView(View.Summary.class)
    @JsonProperty("teDiaE")
    @Column(name = "teDiaE")
    private Double teDiaE;

    @JsonView(View.Summary.class)
    @JsonProperty("teDiaETol")
    @Column(name = "teDiaETol")
    private Double teDiaETol;

    @JsonView(View.Summary.class)
    @JsonProperty("teDiaF")
    @Column(name = "teDiaF")
    private Double teDiaF;

    @JsonView(View.Summary.class)
    @JsonProperty("teDiaFTol")
    @Column(name = "teDiaFTol")
    private Double teDiaFTol;

    @JsonView(View.Summary.class)
    @JsonProperty("oal")
    @Column(name = "oal")
    private Double oal;

    @JsonView(View.Summary.class)
    @JsonProperty("oalTol")
    @Column(name = "oalTol")
    private Double oalTol;

    @JsonView(View.Summary.class)
    @JsonProperty("oilInletThread")
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "oilInletThread")
    private CriticalDimensionEnumVal oilInletThread;

    @JsonView(View.Summary.class)
    @JsonProperty("oilInletFlangeThread")
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "oilInletFlangeThread")
    private CriticalDimensionEnumVal oilInletFlangeThread;

    @JsonView(View.Summary.class)
    @JsonProperty("oilDrainThread")
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "oilDrainThread")
    private CriticalDimensionEnumVal oilDrainThread;

    @JsonView(View.Summary.class)
    @JsonProperty("oilDrainFlangeThread")
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "oilDrainFlangeThread")
    private CriticalDimensionEnumVal oilDrainFlangeThread;

    @JsonView(View.Summary.class)
    @JsonProperty("coolantPortThread1")
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "coolantPortThread1")
    private CriticalDimensionEnumVal coolantPortThread1;

    @JsonView(View.Summary.class)
    @JsonProperty("coolantPortThread2")
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "coolantPortThread2")
    private CriticalDimensionEnumVal coolantPortThread2;

    @JsonView(View.Summary.class)
    @JsonProperty("prBoreDia")
    @Column(name = "prBoreDia")
    private Double prBoreDia;

    @JsonView(View.Summary.class)
    @JsonProperty("prBoreDiaTol")
    @Column(name = "prBoreDiaTol")
    private Double prBoreDiaTol;

    @JsonView(View.Summary.class)
    @JsonProperty("leadInChmfr05Angle")
    @Column(name = "leadInChmfr05Angle")
    private Double leadInChmfr05Angle;

    @JsonView(View.Summary.class)
    @JsonProperty("leadInChmfrLen")
    @Column(name = "leadInChmfrLen")
    private Double leadInChmfrLen;

    @JsonView(View.Summary.class)
    @JsonProperty("quadrant")
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "quadrant")
    private CriticalDimensionEnumVal quadrant;

    @JsonView(View.Summary.class)
    @JsonProperty("armAngle")
    @Column(name = "armAngle")
    private Double armAngle;

    @JsonView(View.Summary.class)
    @JsonProperty("singleDualOilFeed")
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "singleDualOilFeed")
    private CriticalDimensionEnumVal singleDualOilFeed;

    @JsonView(View.Summary.class)
    @JsonProperty("spinningBearing")
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "spinningBearing")
    private CriticalDimensionEnumVal spinningBearing;

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

    public CriticalDimensionEnumVal getWaterCooled() {
        return waterCooled;
    }

    public void setWaterCooled(CriticalDimensionEnumVal waterCooled) {
        this.waterCooled = waterCooled;
    }

    public Double getCwcDia() {
        return cwcDia;
    }

    public void setCwcDia(Double cwcDia) {
        this.cwcDia = cwcDia;
    }

    public Double getCwcDiaTol() {
        return cwcDiaTol;
    }

    public void setCwcDiaTol(Double cwcDiaTol) {
        this.cwcDiaTol = cwcDiaTol;
    }

    public Double getBoreDiaMax() {
        return boreDiaMax;
    }

    public void setBoreDiaMax(Double boreDiaMax) {
        this.boreDiaMax = boreDiaMax;
    }

    public Double getBoreDiaMin() {
        return boreDiaMin;
    }

    public void setBoreDiaMin(Double boreDiaMin) {
        this.boreDiaMin = boreDiaMin;
    }

    public Double getCeDiaA() {
        return ceDiaA;
    }

    public void setCeDiaA(Double ceDiaA) {
        this.ceDiaA = ceDiaA;
    }

    public Double getCeDiaATol() {
        return ceDiaATol;
    }

    public void setCeDiaATol(Double ceDiaATol) {
        this.ceDiaATol = ceDiaATol;
    }

    public Double getTeDiaD() {
        return teDiaD;
    }

    public void setTeDiaD(Double teDiaD) {
        this.teDiaD = teDiaD;
    }

    public Double getTeDiaDTol() {
        return teDiaDTol;
    }

    public void setTeDiaDTol(Double teDiaDTol) {
        this.teDiaDTol = teDiaDTol;
    }

    public Double getCeDiaB() {
        return ceDiaB;
    }

    public void setCeDiaB(Double ceDiaB) {
        this.ceDiaB = ceDiaB;
    }

    public Double getCeDiaBTol() {
        return ceDiaBTol;
    }

    public void setCeDiaBTol(Double ceDiaBTol) {
        this.ceDiaBTol = ceDiaBTol;
    }

    public Double getCeDiaC() {
        return ceDiaC;
    }

    public void setCeDiaC(Double ceDiaC) {
        this.ceDiaC = ceDiaC;
    }

    public Double getCeDiaCTol() {
        return ceDiaCTol;
    }

    public void setCeDiaCTol(Double ceDiaCTol) {
        this.ceDiaCTol = ceDiaCTol;
    }

    public Double getTeDiaE() {
        return teDiaE;
    }

    public void setTeDiaE(Double teDiaE) {
        this.teDiaE = teDiaE;
    }

    public Double getTeDiaETol() {
        return teDiaETol;
    }

    public void setTeDiaETol(Double teDiaETol) {
        this.teDiaETol = teDiaETol;
    }

    public Double getTeDiaF() {
        return teDiaF;
    }

    public void setTeDiaF(Double teDiaF) {
        this.teDiaF = teDiaF;
    }

    public Double getTeDiaFTol() {
        return teDiaFTol;
    }

    public void setTeDiaFTol(Double teDiaFTol) {
        this.teDiaFTol = teDiaFTol;
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

    public CriticalDimensionEnumVal getOilInletThread() {
        return oilInletThread;
    }

    public void setOilInletThread(CriticalDimensionEnumVal oilInletThread) {
        this.oilInletThread = oilInletThread;
    }

    public CriticalDimensionEnumVal getOilInletFlangeThread() {
        return oilInletFlangeThread;
    }

    public void setOilInletFlangeThread(CriticalDimensionEnumVal oilInletFlangeThread) {
        this.oilInletFlangeThread = oilInletFlangeThread;
    }

    public CriticalDimensionEnumVal getOilDrainThread() {
        return oilDrainThread;
    }

    public void setOilDrainThread(CriticalDimensionEnumVal oilDrainThread) {
        this.oilDrainThread = oilDrainThread;
    }

    public CriticalDimensionEnumVal getOilDrainFlangeThread() {
        return oilDrainFlangeThread;
    }

    public void setOilDrainFlangeThread(CriticalDimensionEnumVal oilDrainFlangeThread) {
        this.oilDrainFlangeThread = oilDrainFlangeThread;
    }

    public CriticalDimensionEnumVal getCoolantPortThread1() {
        return coolantPortThread1;
    }

    public void setCoolantPortThread1(CriticalDimensionEnumVal coolantPortThread1) {
        this.coolantPortThread1 = coolantPortThread1;
    }

    public CriticalDimensionEnumVal getCoolantPortThread2() {
        return coolantPortThread2;
    }

    public void setCoolantPortThread2(CriticalDimensionEnumVal coolantPortThread2) {
        this.coolantPortThread2 = coolantPortThread2;
    }

    public Double getPrBoreDia() {
        return prBoreDia;
    }

    public void setPrBoreDia(Double prBoreDia) {
        this.prBoreDia = prBoreDia;
    }

    public Double getPrBoreDiaTol() {
        return prBoreDiaTol;
    }

    public void setPrBoreDiaTol(Double prBoreDiaTol) {
        this.prBoreDiaTol = prBoreDiaTol;
    }

    public Double getLeadInChmfr05Angle() {
        return leadInChmfr05Angle;
    }

    public void setLeadInChmfr05Angle(Double leadInChmfr05Angle) {
        this.leadInChmfr05Angle = leadInChmfr05Angle;
    }

    public Double getLeadInChmfrLen() {
        return leadInChmfrLen;
    }

    public void setLeadInChmfrLen(Double leadInChmfrLen) {
        this.leadInChmfrLen = leadInChmfrLen;
    }

    public CriticalDimensionEnumVal getQuadrant() {
        return quadrant;
    }

    public void setQuadrant(CriticalDimensionEnumVal quadrant) {
        this.quadrant = quadrant;
    }

    public Double getArmAngle() {
        return armAngle;
    }

    public void setArmAngle(Double armAngle) {
        this.armAngle = armAngle;
    }

    public CriticalDimensionEnumVal getSingleDualOilFeed() {
        return singleDualOilFeed;
    }

    public void setSingleDualOilFeed(CriticalDimensionEnumVal singleDualOilFeed) {
        this.singleDualOilFeed = singleDualOilFeed;
    }

    public CriticalDimensionEnumVal getSpinningBearing() {
        return spinningBearing;
    }

    public void setSpinningBearing(CriticalDimensionEnumVal spinningBearing) {
        this.spinningBearing = spinningBearing;
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
