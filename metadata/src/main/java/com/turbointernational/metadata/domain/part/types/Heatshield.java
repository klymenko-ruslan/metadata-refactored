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
@Table(name = "heatshield")
@PrimaryKeyJoinColumn(name = "part_id")
public class Heatshield extends Part {

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
    @JsonProperty("materialThickness")
    @Column(name = "materialThickness")
    private Double materialThickness;


    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Getters and setters: critical dimensions">
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

    public Double getBorediae() {
        return boreDiaE;
    }

    public void setBorediae(Double boreDiaE) {
        this.boreDiaE = boreDiaE;
    }

    public Double getBorediaetol() {
        return boreDiaETol;
    }

    public void setBorediaetol(Double boreDiaETol) {
        this.boreDiaETol = boreDiaETol;
    }

    public Double getBoreheightf() {
        return boreHeightF;
    }

    public void setBoreheightf(Double boreHeightF) {
        this.boreHeightF = boreHeightF;
    }

    public Double getBoreheightftol() {
        return boreHeightFTol;
    }

    public void setBoreheightftol(Double boreHeightFTol) {
        this.boreHeightFTol = boreHeightFTol;
    }

    public Double getOalg() {
        return oalG;
    }

    public void setOalg(Double oalG) {
        this.oalG = oalG;
    }

    public Double getOalgtol() {
        return oalGTol;
    }

    public void setOalgtol(Double oalGTol) {
        this.oalGTol = oalGTol;
    }

    public Integer getGauge() {
        return gauge;
    }

    public void setGauge(Integer gauge) {
        this.gauge = gauge;
    }

    public CriticalDimensionEnumVal getMatl() {
        return matL;
    }

    public void setMatl(CriticalDimensionEnumVal matL) {
        this.matL = matL;
    }

    public String getSpecialfeatures() {
        return specialFeatures;
    }

    public void setSpecialfeatures(String specialFeatures) {
        this.specialFeatures = specialFeatures;
    }

    public CriticalDimensionEnumVal getRolledlip() {
        return rolledLip;
    }

    public void setRolledlip(CriticalDimensionEnumVal rolledLip) {
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

    public Double getMaterialthickness() {
        return materialThickness;
    }

    public void setMaterialthickness(Double materialThickness) {
        this.materialThickness = materialThickness;
    }


    //</editor-fold>
}
