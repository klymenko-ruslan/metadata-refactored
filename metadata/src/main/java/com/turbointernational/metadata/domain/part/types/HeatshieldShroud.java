package com.turbointernational.metadata.domain.part.types;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.domain.criticaldimension.CriticalDimensionEnumVal;
import com.turbointernational.metadata.domain.part.Part;
import com.turbointernational.metadata.web.View;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;


/**
 * Created by dmytro.trunykov@zorallabs.com on 2016-08-01 17:06:08.758465.
 */
@Entity
@Table(name = "heatshield")
@PrimaryKeyJoinColumn(name = "part_id")
public class HeatshieldShroud extends Part {

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
    @JsonProperty("boreDiaE")
    @Column(name = "boreDiaE")
    private Double boreDiaE;

    @JsonView(View.Summary.class)
    @JsonProperty("boreDiaETol")
    @Column(name = "boreDiaETol")
    private Double boreDiaETol;

    @JsonView(View.Summary.class)
    @JsonProperty("boreHeightF")
    @Column(name = "boreHeightF")
    private Double boreHeightF;

    @JsonView(View.Summary.class)
    @JsonProperty("boreHeightFTol")
    @Column(name = "boreHeightFTol")
    private Double boreHeightFTol;

    @JsonView(View.Summary.class)
    @JsonProperty("oalG")
    @Column(name = "oalG")
    private Double oalG;

    @JsonView(View.Summary.class)
    @JsonProperty("oalGTol")
    @Column(name = "oalGTol")
    private Double oalGTol;

    @JsonView(View.Summary.class)
    @JsonProperty("gauge")
    @Column(name = "gauge")
    private Integer gauge;

    @JsonView(View.Summary.class)
    @JsonProperty("matL")
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "matL")
    private CriticalDimensionEnumVal matL;

    @JsonView(View.Summary.class)
    @JsonProperty("specialFeatures")
    @Column(name = "specialFeatures")
    private String specialFeatures;

    @JsonView(View.Summary.class)
    @JsonProperty("rolledLip")
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "rolledLip")
    private CriticalDimensionEnumVal rolledLip;

    @JsonView(View.Summary.class)
    @JsonProperty("weight")
    @Column(name = "weight")
    private Double weight;

    @JsonView(View.Summary.class)
    @JsonProperty("diagram")
    @Column(name = "diagram")
    private Integer diagram;

    @JsonView(View.Summary.class)
    @JsonProperty("thickness")
    @Column(name = "thickness")
    private Double thickness;

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

    public Double getBoreDiaE() {
        return boreDiaE;
    }

    public void setBoreDiaE(Double boreDiaE) {
        this.boreDiaE = boreDiaE;
    }

    public Double getBoreDiaETol() {
        return boreDiaETol;
    }

    public void setBoreDiaETol(Double boreDiaETol) {
        this.boreDiaETol = boreDiaETol;
    }

    public Double getBoreHeightF() {
        return boreHeightF;
    }

    public void setBoreHeightF(Double boreHeightF) {
        this.boreHeightF = boreHeightF;
    }

    public Double getBoreHeightFTol() {
        return boreHeightFTol;
    }

    public void setBoreHeightFTol(Double boreHeightFTol) {
        this.boreHeightFTol = boreHeightFTol;
    }

    public Double getOalG() {
        return oalG;
    }

    public void setOalG(Double oalG) {
        this.oalG = oalG;
    }

    public Double getOalGTol() {
        return oalGTol;
    }

    public void setOalGTol(Double oalGTol) {
        this.oalGTol = oalGTol;
    }

    public Integer getGauge() {
        return gauge;
    }

    public void setGauge(Integer gauge) {
        this.gauge = gauge;
    }

    public CriticalDimensionEnumVal getMatL() {
        return matL;
    }

    public void setMatL(CriticalDimensionEnumVal matL) {
        this.matL = matL;
    }

    public String getSpecialFeatures() {
        return specialFeatures;
    }

    public void setSpecialFeatures(String specialFeatures) {
        this.specialFeatures = specialFeatures;
    }

    public CriticalDimensionEnumVal getRolledLip() {
        return rolledLip;
    }

    public void setRolledLip(CriticalDimensionEnumVal rolledLip) {
        this.rolledLip = rolledLip;
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

    public Double getThickness() {
        return thickness;
    }

    public void setThickness(Double thickness) {
        this.thickness = thickness;
    }

    //</editor-fold>

}
