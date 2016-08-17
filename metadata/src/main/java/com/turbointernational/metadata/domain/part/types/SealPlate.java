package com.turbointernational.metadata.domain.part.types;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.domain.criticaldimension.CriticalDimensionEnumVal;
import com.turbointernational.metadata.domain.part.Part;
import com.turbointernational.metadata.web.View;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;


/**
 * Created by dmytro.trunykov@zorallabs.com on 2016-08-01 17:06:08.760482.
 */
@Entity
@Table(name = "seal_plate")
@PrimaryKeyJoinColumn(name = "part_id")
public class SealPlate extends Part {

    //<editor-fold defaultstate="collapsed" desc="Properties: critical dimensions">

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
    @JsonProperty("oalE")
    @Column(name = "oalE")
    private Double oalE;

    @JsonView(View.Summary.class)
    @JsonProperty("oalETol")
    @Column(name = "oalETol")
    private Double oalETol;

    @JsonView(View.Summary.class)
    @JsonProperty("boreDia")
    @Column(name = "boreDia")
    private Double boreDia;

    @JsonView(View.Summary.class)
    @JsonProperty("boreDiaTol")
    @Column(name = "boreDiaTol")
    private Double boreDiaTol;

    @JsonView(View.Summary.class)
    @JsonProperty("prBoreDia")
    @Column(name = "prBoreDia")
    private Double prBoreDia;

    @JsonView(View.Summary.class)
    @JsonProperty("prBoreDiaTol")
    @Column(name = "prBoreDiaTol")
    private Double prBoreDiaTol;

    @JsonView(View.Summary.class)
    @JsonProperty("cwcDia")
    @Column(name = "cwcDia")
    private Double cwcDia;

    @JsonView(View.Summary.class)
    @JsonProperty("cwcDiaTol")
    @Column(name = "cwcDiaTol")
    private Double cwcDiaTol;

    @JsonView(View.Summary.class)
    @JsonProperty("oRingGrooveDia")
    @Column(name = "oRingGrooveDia")
    private Double oRingGrooveDia;

    @JsonView(View.Summary.class)
    @JsonProperty("oRingGrooveDiaTol")
    @Column(name = "oRingGrooveDiaTol")
    private Double oRingGrooveDiaTol;

    @JsonView(View.Summary.class)
    @JsonProperty("oRingGrooveWidth")
    @Column(name = "oRingGrooveWidth")
    private Double oRingGrooveWidth;

    @JsonView(View.Summary.class)
    @JsonProperty("oRingGrooveWidthTol")
    @Column(name = "oRingGrooveWidthTol")
    private Double oRingGrooveWidthTol;

    @JsonView(View.Summary.class)
    @JsonProperty("mountingHoles")
    @Column(name = "mountingHoles")
    private Integer mountingHoles;

    @JsonView(View.Summary.class)
    @JsonProperty("mountingHoleDia")
    @Column(name = "mountingHoleDia")
    private Double mountingHoleDia;

    @JsonView(View.Summary.class)
    @JsonProperty("hubPosF")
    @Column(name = "hubPosF")
    private Double hubPosF;

    @JsonView(View.Summary.class)
    @JsonProperty("hubPosFTol")
    @Column(name = "hubPosFTol")
    private Double hubPosFTol;

    @JsonView(View.Summary.class)
    @JsonProperty("leadInChmfr05Angle")
    @Column(name = "leadInChmfr05Angle")
    private Double leadInChmfr05Angle;

    @JsonView(View.Summary.class)
    @JsonProperty("leadInChmfrLen")
    @Column(name = "leadInChmfrLen")
    private Double leadInChmfrLen;

    @JsonView(View.Summary.class)
    @JsonProperty("clampedWidthG")
    @Column(name = "clampedWidthG")
    private Double clampedWidthG;

    @JsonView(View.Summary.class)
    @JsonProperty("clampedWidthGTol")
    @Column(name = "clampedWidthGTol")
    private Double clampedWidthGTol;

    @JsonView(View.Summary.class)
    @JsonProperty("superbackFlatbackSpecial")
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "superbackFlatbackSpecial")
    private CriticalDimensionEnumVal superbackFlatbackSpecial;

    @JsonView(View.Summary.class)
    @JsonProperty("dynamicCarbonSeal")
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "dynamicCarbonSeal")
    private CriticalDimensionEnumVal dynamicCarbonSeal;

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

    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Getters and setters: critical dimensions">

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

    public Double getOalE() {
        return oalE;
    }

    public void setOalE(Double oalE) {
        this.oalE = oalE;
    }

    public Double getOalETol() {
        return oalETol;
    }

    public void setOalETol(Double oalETol) {
        this.oalETol = oalETol;
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

    public Double getORingGrooveDia() {
        return oRingGrooveDia;
    }

    public void setORingGrooveDia(Double oRingGrooveDia) {
        this.oRingGrooveDia = oRingGrooveDia;
    }

    public Double getORingGrooveDiaTol() {
        return oRingGrooveDiaTol;
    }

    public void setORingGrooveDiaTol(Double oRingGrooveDiaTol) {
        this.oRingGrooveDiaTol = oRingGrooveDiaTol;
    }

    public Double getORingGrooveWidth() {
        return oRingGrooveWidth;
    }

    public void setORingGrooveWidth(Double oRingGrooveWidth) {
        this.oRingGrooveWidth = oRingGrooveWidth;
    }

    public Double getORingGrooveWidthTol() {
        return oRingGrooveWidthTol;
    }

    public void setORingGrooveWidthTol(Double oRingGrooveWidthTol) {
        this.oRingGrooveWidthTol = oRingGrooveWidthTol;
    }

    public Integer getMountingHoles() {
        return mountingHoles;
    }

    public void setMountingHoles(Integer mountingHoles) {
        this.mountingHoles = mountingHoles;
    }

    public Double getMountingHoleDia() {
        return mountingHoleDia;
    }

    public void setMountingHoleDia(Double mountingHoleDia) {
        this.mountingHoleDia = mountingHoleDia;
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

    public Double getClampedWidthG() {
        return clampedWidthG;
    }

    public void setClampedWidthG(Double clampedWidthG) {
        this.clampedWidthG = clampedWidthG;
    }

    public Double getClampedWidthGTol() {
        return clampedWidthGTol;
    }

    public void setClampedWidthGTol(Double clampedWidthGTol) {
        this.clampedWidthGTol = clampedWidthGTol;
    }

    public CriticalDimensionEnumVal getSuperbackFlatbackSpecial() {
        return superbackFlatbackSpecial;
    }

    public void setSuperbackFlatbackSpecial(CriticalDimensionEnumVal superbackFlatbackSpecial) {
        this.superbackFlatbackSpecial = superbackFlatbackSpecial;
    }

    public CriticalDimensionEnumVal getDynamicCarbonSeal() {
        return dynamicCarbonSeal;
    }

    public void setDynamicCarbonSeal(CriticalDimensionEnumVal dynamicCarbonSeal) {
        this.dynamicCarbonSeal = dynamicCarbonSeal;
    }

    public CriticalDimensionEnumVal getMatL() {
        return matL;
    }

    public void setMatL(CriticalDimensionEnumVal matL) {
        this.matL = matL;
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
