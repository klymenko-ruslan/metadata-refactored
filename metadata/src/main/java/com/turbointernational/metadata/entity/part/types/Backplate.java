package com.turbointernational.metadata.entity.part.types;

import static javax.persistence.FetchType.LAZY;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.entity.CriticalDimensionEnumVal;
import com.turbointernational.metadata.entity.part.Part;
import com.turbointernational.metadata.util.View;

/**
 * Created by dmytro.trunykov@zorallabs.com on 2016-08-01 17:06:08.756806.
 */
@Entity
@Table(name = "backplate")
@DiscriminatorValue("34")
@PrimaryKeyJoinColumn(name = "part_id")
public class Backplate extends Part {

    private static final long serialVersionUID = 3513632776450780354L;

    // <editor-fold defaultstate="collapsed" desc="Properties: critical
    // dimensions">

    @JsonView(View.Summary.class)
    @JsonProperty("dynCs")
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "dynCs")
    private CriticalDimensionEnumVal dynCs;

    @JsonView(View.Summary.class)
    @JsonProperty("superbackFlatback")
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "superbackFlatback")
    private CriticalDimensionEnumVal superbackFlatback;

    @JsonView(View.Summary.class)
    @JsonProperty("mountingHoles")
    @Column(name = "mountingHoles")
    private Integer mountingHoles;

    @JsonView(View.Summary.class)
    @JsonProperty("mountingHoleThreadCallout")
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "mountingHoleThreadCallout")
    private CriticalDimensionEnumVal mountingHoleThreadCallout;

    @JsonView(View.Summary.class)
    @JsonProperty("diaA")
    @Column(name = "diaA")
    private Double diaA;

    @JsonView(View.Summary.class)
    @JsonProperty("diaATol")
    @Column(name = "diaATol")
    private Double diaATol;

    @JsonView(View.Summary.class)
    @JsonProperty("diaB")
    @Column(name = "diaB")
    private Double diaB;

    @JsonView(View.Summary.class)
    @JsonProperty("diaBTol")
    @Column(name = "diaBTol")
    private Double diaBTol;

    @JsonView(View.Summary.class)
    @JsonProperty("diaC")
    @Column(name = "diaC")
    private Double diaC;

    @JsonView(View.Summary.class)
    @JsonProperty("diaCTol")
    @Column(name = "diaCTol")
    private Double diaCTol;

    @JsonView(View.Summary.class)
    @JsonProperty("diaD")
    @Column(name = "diaD")
    private Double diaD;

    @JsonView(View.Summary.class)
    @JsonProperty("diaDTol")
    @Column(name = "diaDTol")
    private Double diaDTol;

    @JsonView(View.Summary.class)
    @JsonProperty("cwcDiaE")
    @Column(name = "cwcDiaE")
    private Double cwcDiaE;

    @JsonView(View.Summary.class)
    @JsonProperty("cwcDiaETol")
    @Column(name = "cwcDiaETol")
    private Double cwcDiaETol;

    @JsonView(View.Summary.class)
    @JsonProperty("boreDia")
    @Column(name = "boreDia")
    private Double boreDia;

    @JsonView(View.Summary.class)
    @JsonProperty("boreDiaTol")
    @Column(name = "boreDiaTol")
    private Double boreDiaTol;

    @JsonView(View.Summary.class)
    @JsonProperty("mountingHoleDia")
    @Column(name = "mountingHoleDia")
    private Double mountingHoleDia;

    @JsonView(View.Summary.class)
    @JsonProperty("oal")
    @Column(name = "oal")
    private Double oal;

    @JsonView(View.Summary.class)
    @JsonProperty("oalTol")
    @Column(name = "oalTol")
    private Double oalTol;

    @JsonView(View.Summary.class)
    @JsonProperty("hubPosF")
    @Column(name = "hubPosF")
    private Double hubPosF;

    @JsonView(View.Summary.class)
    @JsonProperty("hubPosFTol")
    @Column(name = "hubPosFTol")
    private Double hubPosFTol;

    @JsonView(View.Summary.class)
    @JsonProperty("ccLocPosG")
    @Column(name = "ccLocPosG")
    private Double ccLocPosG;

    @JsonView(View.Summary.class)
    @JsonProperty("ccLocPosGTol")
    @Column(name = "ccLocPosGTol")
    private Double ccLocPosGTol;

    @JsonView(View.Summary.class)
    @JsonProperty("leadInChmfr12Angle")
    @Column(name = "leadInChmfr12Angle")
    private Double leadInChmfr12Angle;

    @JsonView(View.Summary.class)
    @JsonProperty("leadInChmfrLen")
    @Column(name = "leadInChmfrLen")
    private Double leadInChmfrLen;

    @JsonView(View.Summary.class)
    @JsonProperty("matL")
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "matL")
    private CriticalDimensionEnumVal matL;

    @JsonView(View.Summary.class)
    @JsonProperty("weight")
    @Column(name = "weight")
    private Double weight;

    @JsonView(View.Summary.class)
    @JsonProperty("diagram")
    @Column(name = "diagram")
    private Integer diagram;

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Getters and setters: critical
    // dimensions">

    public CriticalDimensionEnumVal getDynCs() {
        return dynCs;
    }

    public void setDynCs(CriticalDimensionEnumVal dynCs) {
        this.dynCs = dynCs;
    }

    public CriticalDimensionEnumVal getSuperbackFlatback() {
        return superbackFlatback;
    }

    public void setSuperbackFlatback(CriticalDimensionEnumVal superbackFlatback) {
        this.superbackFlatback = superbackFlatback;
    }

    public Integer getMountingHoles() {
        return mountingHoles;
    }

    public void setMountingHoles(Integer mountingHoles) {
        this.mountingHoles = mountingHoles;
    }

    public CriticalDimensionEnumVal getMountingHoleThreadCallout() {
        return mountingHoleThreadCallout;
    }

    public void setMountingHoleThreadCallout(CriticalDimensionEnumVal mountingHoleThreadCallout) {
        this.mountingHoleThreadCallout = mountingHoleThreadCallout;
    }

    public Double getDiaA() {
        return diaA;
    }

    public void setDiaA(Double diaA) {
        this.diaA = diaA;
    }

    public Double getDiaATol() {
        return diaATol;
    }

    public void setDiaATol(Double diaATol) {
        this.diaATol = diaATol;
    }

    public Double getDiaB() {
        return diaB;
    }

    public void setDiaB(Double diaB) {
        this.diaB = diaB;
    }

    public Double getDiaBTol() {
        return diaBTol;
    }

    public void setDiaBTol(Double diaBTol) {
        this.diaBTol = diaBTol;
    }

    public Double getDiaC() {
        return diaC;
    }

    public void setDiaC(Double diaC) {
        this.diaC = diaC;
    }

    public Double getDiaCTol() {
        return diaCTol;
    }

    public void setDiaCTol(Double diaCTol) {
        this.diaCTol = diaCTol;
    }

    public Double getDiaD() {
        return diaD;
    }

    public void setDiaD(Double diaD) {
        this.diaD = diaD;
    }

    public Double getDiaDTol() {
        return diaDTol;
    }

    public void setDiaDTol(Double diaDTol) {
        this.diaDTol = diaDTol;
    }

    public Double getCwcDiaE() {
        return cwcDiaE;
    }

    public void setCwcDiaE(Double cwcDiaE) {
        this.cwcDiaE = cwcDiaE;
    }

    public Double getCwcDiaETol() {
        return cwcDiaETol;
    }

    public void setCwcDiaETol(Double cwcDiaETol) {
        this.cwcDiaETol = cwcDiaETol;
    }

    public Double getBoreDia() {
        return boreDia;
    }

    public void setBoreDia(Double boreDia) {
        this.boreDia = boreDia;
    }

    public Double getBoreDiaTol() {
        return boreDiaTol;
    }

    public void setBoreDiaTol(Double boreDiaTol) {
        this.boreDiaTol = boreDiaTol;
    }

    public Double getMountingHoleDia() {
        return mountingHoleDia;
    }

    public void setMountingHoleDia(Double mountingHoleDia) {
        this.mountingHoleDia = mountingHoleDia;
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

    public Double getHubPosF() {
        return hubPosF;
    }

    public void setHubPosF(Double hubPosF) {
        this.hubPosF = hubPosF;
    }

    public Double getHubPosFTol() {
        return hubPosFTol;
    }

    public void setHubPosFTol(Double hubPosFTol) {
        this.hubPosFTol = hubPosFTol;
    }

    public Double getCcLocPosG() {
        return ccLocPosG;
    }

    public void setCcLocPosG(Double ccLocPosG) {
        this.ccLocPosG = ccLocPosG;
    }

    public Double getCcLocPosGTol() {
        return ccLocPosGTol;
    }

    public void setCcLocPosGTol(Double ccLocPosGTol) {
        this.ccLocPosGTol = ccLocPosGTol;
    }

    public Double getLeadInChmfr12Angle() {
        return leadInChmfr12Angle;
    }

    public void setLeadInChmfr12Angle(Double leadInChmfr12Angle) {
        this.leadInChmfr12Angle = leadInChmfr12Angle;
    }

    public Double getLeadInChmfrLen() {
        return leadInChmfrLen;
    }

    public void setLeadInChmfrLen(Double leadInChmfrLen) {
        this.leadInChmfrLen = leadInChmfrLen;
    }

    public CriticalDimensionEnumVal getMatL() {
        return matL;
    }

    public void setMatL(CriticalDimensionEnumVal matL) {
        this.matL = matL;
    }

    @Override
    public Double getWeight() {
        return weight;
    }

    @Override
    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Integer getDiagram() {
        return diagram;
    }

    public void setDiagram(Integer diagram) {
        this.diagram = diagram;
    }

    // </editor-fold>

}
