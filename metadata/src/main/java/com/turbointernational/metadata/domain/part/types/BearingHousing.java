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
    public CriticalDimensionEnumVal getWatercooled() {
        return waterCooled;
    }

    public void setWatercooled(CriticalDimensionEnumVal waterCooled) {
        this.waterCooled = waterCooled;
    }

    public Double getCwcdia() {
        return cwcDia;
    }

    public void setCwcdia(Double cwcDia) {
        this.cwcDia = cwcDia;
    }

    public Double getCwcdiatol() {
        return cwcDiaTol;
    }

    public void setCwcdiatol(Double cwcDiaTol) {
        this.cwcDiaTol = cwcDiaTol;
    }

    public Double getBorediamax() {
        return boreDiaMax;
    }

    public void setBorediamax(Double boreDiaMax) {
        this.boreDiaMax = boreDiaMax;
    }

    public Double getBorediamin() {
        return boreDiaMin;
    }

    public void setBorediamin(Double boreDiaMin) {
        this.boreDiaMin = boreDiaMin;
    }

    public Double getCediaa() {
        return ceDiaA;
    }

    public void setCediaa(Double ceDiaA) {
        this.ceDiaA = ceDiaA;
    }

    public Double getCediaatol() {
        return ceDiaATol;
    }

    public void setCediaatol(Double ceDiaATol) {
        this.ceDiaATol = ceDiaATol;
    }

    public Double getTediad() {
        return teDiaD;
    }

    public void setTediad(Double teDiaD) {
        this.teDiaD = teDiaD;
    }

    public Double getTediadtol() {
        return teDiaDTol;
    }

    public void setTediadtol(Double teDiaDTol) {
        this.teDiaDTol = teDiaDTol;
    }

    public Double getCediab() {
        return ceDiaB;
    }

    public void setCediab(Double ceDiaB) {
        this.ceDiaB = ceDiaB;
    }

    public Double getCediabtol() {
        return ceDiaBTol;
    }

    public void setCediabtol(Double ceDiaBTol) {
        this.ceDiaBTol = ceDiaBTol;
    }

    public Double getCediac() {
        return ceDiaC;
    }

    public void setCediac(Double ceDiaC) {
        this.ceDiaC = ceDiaC;
    }

    public Double getCediactol() {
        return ceDiaCTol;
    }

    public void setCediactol(Double ceDiaCTol) {
        this.ceDiaCTol = ceDiaCTol;
    }

    public Double getTediae() {
        return teDiaE;
    }

    public void setTediae(Double teDiaE) {
        this.teDiaE = teDiaE;
    }

    public Double getTediaetol() {
        return teDiaETol;
    }

    public void setTediaetol(Double teDiaETol) {
        this.teDiaETol = teDiaETol;
    }

    public Double getTediaf() {
        return teDiaF;
    }

    public void setTediaf(Double teDiaF) {
        this.teDiaF = teDiaF;
    }

    public Double getTediaftol() {
        return teDiaFTol;
    }

    public void setTediaftol(Double teDiaFTol) {
        this.teDiaFTol = teDiaFTol;
    }

    public Double getOal() {
        return oal;
    }

    public void setOal(Double oal) {
        this.oal = oal;
    }

    public Double getOaltol() {
        return oalTol;
    }

    public void setOaltol(Double oalTol) {
        this.oalTol = oalTol;
    }

    public CriticalDimensionEnumVal getOilinletthread() {
        return oilInletThread;
    }

    public void setOilinletthread(CriticalDimensionEnumVal oilInletThread) {
        this.oilInletThread = oilInletThread;
    }

    public CriticalDimensionEnumVal getOilinletflangethread() {
        return oilInletFlangeThread;
    }

    public void setOilinletflangethread(CriticalDimensionEnumVal oilInletFlangeThread) {
        this.oilInletFlangeThread = oilInletFlangeThread;
    }

    public CriticalDimensionEnumVal getOildrainthread() {
        return oilDrainThread;
    }

    public void setOildrainthread(CriticalDimensionEnumVal oilDrainThread) {
        this.oilDrainThread = oilDrainThread;
    }

    public CriticalDimensionEnumVal getOildrainflangethread() {
        return oilDrainFlangeThread;
    }

    public void setOildrainflangethread(CriticalDimensionEnumVal oilDrainFlangeThread) {
        this.oilDrainFlangeThread = oilDrainFlangeThread;
    }

    public CriticalDimensionEnumVal getCoolantportthread1() {
        return coolantPortThread1;
    }

    public void setCoolantportthread1(CriticalDimensionEnumVal coolantPortThread1) {
        this.coolantPortThread1 = coolantPortThread1;
    }

    public CriticalDimensionEnumVal getCoolantportthread2() {
        return coolantPortThread2;
    }

    public void setCoolantportthread2(CriticalDimensionEnumVal coolantPortThread2) {
        this.coolantPortThread2 = coolantPortThread2;
    }

    public Double getPrboredia() {
        return prBoreDia;
    }

    public void setPrboredia(Double prBoreDia) {
        this.prBoreDia = prBoreDia;
    }

    public Double getPrborediatol() {
        return prBoreDiaTol;
    }

    public void setPrborediatol(Double prBoreDiaTol) {
        this.prBoreDiaTol = prBoreDiaTol;
    }

    public Double getLeadinchmfr05angle() {
        return leadInChmfr05Angle;
    }

    public void setLeadinchmfr05angle(Double leadInChmfr05Angle) {
        this.leadInChmfr05Angle = leadInChmfr05Angle;
    }

    public Double getLeadinchmfrlen() {
        return leadInChmfrLen;
    }

    public void setLeadinchmfrlen(Double leadInChmfrLen) {
        this.leadInChmfrLen = leadInChmfrLen;
    }

    public CriticalDimensionEnumVal getQuadrant() {
        return quadrant;
    }

    public void setQuadrant(CriticalDimensionEnumVal quadrant) {
        this.quadrant = quadrant;
    }

    public Double getArmangle() {
        return armAngle;
    }

    public void setArmangle(Double armAngle) {
        this.armAngle = armAngle;
    }

    public CriticalDimensionEnumVal getSingledualoilfeed() {
        return singleDualOilFeed;
    }

    public void setSingledualoilfeed(CriticalDimensionEnumVal singleDualOilFeed) {
        this.singleDualOilFeed = singleDualOilFeed;
    }

    public CriticalDimensionEnumVal getSpinningbearing() {
        return spinningBearing;
    }

    public void setSpinningbearing(CriticalDimensionEnumVal spinningBearing) {
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
