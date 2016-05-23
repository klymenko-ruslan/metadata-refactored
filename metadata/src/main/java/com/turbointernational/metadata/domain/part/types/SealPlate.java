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
@Table(name = "seal_plate")
@PrimaryKeyJoinColumn(name = "part_id")
public class SealPlate extends Part {
    //<editor-fold defaultstate="collapsed" desc="Properties: critical dimensions">
    @JsonView(View.Summary.class)
    @JsonProperty("leadInChmfr05Angle")
    @Column(name = "leadInChmfr05Angle")
    private Double leadInChmfr05Angle;

    @JsonView(View.Summary.class)
    @JsonProperty("leadInChmfrLen")
    @Column(name = "leadInChmfrLen")
    private Double leadInChmfrLen;

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
    @JsonProperty("clampedWidthG")
    @Column(name = "clampedWidthG")
    private Double clampedWidthG;

    @JsonView(View.Summary.class)
    @JsonProperty("clampedWidthGTol")
    @Column(name = "clampedWidthGTol")
    private Double clampedWidthGTol;

    @JsonView(View.Summary.class)
    @JsonProperty("superbackFlatbackSpecial")
    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "superbackFlatbackSpecial")
    private CriticalDimensionEnumVal superbackFlatbackSpecial;

    @JsonView(View.Summary.class)
    @JsonProperty("dynamicCarbonSeal")
    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "dynamicCarbonSeal")
    private CriticalDimensionEnumVal dynamicCarbonSeal;

    @JsonView(View.Summary.class)
    @JsonProperty("dynCs")
    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "dynCs")
    private CriticalDimensionEnumVal dynCs;

    @JsonView(View.Summary.class)
    @JsonProperty("matL")
    @ManyToOne(fetch = EAGER)
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

    public Double getDiaa() {
        return diaA;
    }

    public void setDiaa(Double diaA) {
        this.diaA = diaA;
    }

    public Double getDiaatol() {
        return diaATol;
    }

    public void setDiaatol(Double diaATol) {
        this.diaATol = diaATol;
    }

    public Double getDiab() {
        return diaB;
    }

    public void setDiab(Double diaB) {
        this.diaB = diaB;
    }

    public Double getDiabtol() {
        return diaBTol;
    }

    public void setDiabtol(Double diaBTol) {
        this.diaBTol = diaBTol;
    }

    public Double getDiac() {
        return diaC;
    }

    public void setDiac(Double diaC) {
        this.diaC = diaC;
    }

    public Double getDiactol() {
        return diaCTol;
    }

    public void setDiactol(Double diaCTol) {
        this.diaCTol = diaCTol;
    }

    public Double getDiad() {
        return diaD;
    }

    public void setDiad(Double diaD) {
        this.diaD = diaD;
    }

    public Double getDiadtol() {
        return diaDTol;
    }

    public void setDiadtol(Double diaDTol) {
        this.diaDTol = diaDTol;
    }

    public Double getOale() {
        return oalE;
    }

    public void setOale(Double oalE) {
        this.oalE = oalE;
    }

    public Double getOaletol() {
        return oalETol;
    }

    public void setOaletol(Double oalETol) {
        this.oalETol = oalETol;
    }

    public Double getBoredia() {
        return boreDia;
    }

    public void setBoredia(Double boreDia) {
        this.boreDia = boreDia;
    }

    public Double getBorediatol() {
        return boreDiaTol;
    }

    public void setBorediatol(Double boreDiaTol) {
        this.boreDiaTol = boreDiaTol;
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

    public Double getOringgroovedia() {
        return oRingGrooveDia;
    }

    public void setOringgroovedia(Double oRingGrooveDia) {
        this.oRingGrooveDia = oRingGrooveDia;
    }

    public Double getOringgroovediatol() {
        return oRingGrooveDiaTol;
    }

    public void setOringgroovediatol(Double oRingGrooveDiaTol) {
        this.oRingGrooveDiaTol = oRingGrooveDiaTol;
    }

    public Double getOringgroovewidth() {
        return oRingGrooveWidth;
    }

    public void setOringgroovewidth(Double oRingGrooveWidth) {
        this.oRingGrooveWidth = oRingGrooveWidth;
    }

    public Double getOringgroovewidthtol() {
        return oRingGrooveWidthTol;
    }

    public void setOringgroovewidthtol(Double oRingGrooveWidthTol) {
        this.oRingGrooveWidthTol = oRingGrooveWidthTol;
    }

    public Integer getMountingholes() {
        return mountingHoles;
    }

    public void setMountingholes(Integer mountingHoles) {
        this.mountingHoles = mountingHoles;
    }

    public Double getMountingholedia() {
        return mountingHoleDia;
    }

    public void setMountingholedia(Double mountingHoleDia) {
        this.mountingHoleDia = mountingHoleDia;
    }

    public Double getHubposf() {
        return hubPosF;
    }

    public void setHubposf(Double hubPosF) {
        this.hubPosF = hubPosF;
    }

    public Double getHubposftol() {
        return hubPosFTol;
    }

    public void setHubposftol(Double hubPosFTol) {
        this.hubPosFTol = hubPosFTol;
    }

    public Double getClampedwidthg() {
        return clampedWidthG;
    }

    public void setClampedwidthg(Double clampedWidthG) {
        this.clampedWidthG = clampedWidthG;
    }

    public Double getClampedwidthgtol() {
        return clampedWidthGTol;
    }

    public void setClampedwidthgtol(Double clampedWidthGTol) {
        this.clampedWidthGTol = clampedWidthGTol;
    }

    public CriticalDimensionEnumVal getSuperbackflatbackspecial() {
        return superbackFlatbackSpecial;
    }

    public void setSuperbackflatbackspecial(CriticalDimensionEnumVal superbackFlatbackSpecial) {
        this.superbackFlatbackSpecial = superbackFlatbackSpecial;
    }

    public CriticalDimensionEnumVal getDynamiccarbonseal() {
        return dynamicCarbonSeal;
    }

    public void setDynamiccarbonseal(CriticalDimensionEnumVal dynamicCarbonSeal) {
        this.dynamicCarbonSeal = dynamicCarbonSeal;
    }

    public CriticalDimensionEnumVal getDyncs() {
        return dynCs;
    }

    public void setDyncs(CriticalDimensionEnumVal dynCs) {
        this.dynCs = dynCs;
    }

    public CriticalDimensionEnumVal getMatl() {
        return matL;
    }

    public void setMatl(CriticalDimensionEnumVal matL) {
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
