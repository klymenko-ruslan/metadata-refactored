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
@Table(name = "backplate")
@PrimaryKeyJoinColumn(name = "part_id")
public class Backplate extends Part {

    //<editor-fold defaultstate="collapsed" desc="Properties: critical dimensions">
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
    @JsonProperty("leadInChmfr05Angle")
    @Column(name = "leadInChmfr05Angle")
    private Double leadInChmfr05Angle;

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


    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Getters and setters: critical dimensions">
    public CriticalDimensionEnumVal getDyncs() {
        return dynCs;
    }

    public void setDyncs(CriticalDimensionEnumVal dynCs) {
        this.dynCs = dynCs;
    }

    public CriticalDimensionEnumVal getSuperbackflatback() {
        return superbackFlatback;
    }

    public void setSuperbackflatback(CriticalDimensionEnumVal superbackFlatback) {
        this.superbackFlatback = superbackFlatback;
    }

    public Integer getMountingholes() {
        return mountingHoles;
    }

    public void setMountingholes(Integer mountingHoles) {
        this.mountingHoles = mountingHoles;
    }

    public CriticalDimensionEnumVal getMountingholethreadcallout() {
        return mountingHoleThreadCallout;
    }

    public void setMountingholethreadcallout(CriticalDimensionEnumVal mountingHoleThreadCallout) {
        this.mountingHoleThreadCallout = mountingHoleThreadCallout;
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

    public Double getCwcdiae() {
        return cwcDiaE;
    }

    public void setCwcdiae(Double cwcDiaE) {
        this.cwcDiaE = cwcDiaE;
    }

    public Double getCwcdiaetol() {
        return cwcDiaETol;
    }

    public void setCwcdiaetol(Double cwcDiaETol) {
        this.cwcDiaETol = cwcDiaETol;
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

    public Double getMountingholedia() {
        return mountingHoleDia;
    }

    public void setMountingholedia(Double mountingHoleDia) {
        this.mountingHoleDia = mountingHoleDia;
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

    public Double getCclocposg() {
        return ccLocPosG;
    }

    public void setCclocposg(Double ccLocPosG) {
        this.ccLocPosG = ccLocPosG;
    }

    public Double getCclocposgtol() {
        return ccLocPosGTol;
    }

    public void setCclocposgtol(Double ccLocPosGTol) {
        this.ccLocPosGTol = ccLocPosGTol;
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
