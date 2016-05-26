package com.turbointernational.metadata.domain.part.types;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.domain.criticaldimension.CriticalDimensionEnumVal;
import com.turbointernational.metadata.domain.part.Part;
import com.turbointernational.metadata.web.View;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;


/**
 * Created by dmytro.trunykov@zorallabs.com on 2016-05-26 10:44:43.041043.
 */
@Entity
@Table(name = "thrust_bearing")
@PrimaryKeyJoinColumn(name = "part_id")
public class ThrustBearing extends Part {

    //<editor-fold defaultstate="collapsed" desc="Properties: critical dimensions">

    @JsonView(View.Summary.class)
    @JsonProperty("maxThicknessA")
    @Column(name = "maxThicknessA")
    private Double maxThicknessA;

    @JsonView(View.Summary.class)
    @JsonProperty("minThicknessA")
    @Column(name = "minThicknessA")
    private Double minThicknessA;

    @JsonView(View.Summary.class)
    @JsonProperty("odB")
    @Column(name = "odB")
    private Double odB;

    @JsonView(View.Summary.class)
    @JsonProperty("odBTol")
    @Column(name = "odBTol")
    private Double odBTol;

    @JsonView(View.Summary.class)
    @JsonProperty("idC")
    @Column(name = "idC")
    private Double idC;

    @JsonView(View.Summary.class)
    @JsonProperty("idCTol")
    @Column(name = "idCTol")
    private Double idCTol;

    @JsonView(View.Summary.class)
    @JsonProperty("teRampDiaD")
    @Column(name = "teRampDiaD")
    private Double teRampDiaD;

    @JsonView(View.Summary.class)
    @JsonProperty("teRampDiaDTol")
    @Column(name = "teRampDiaDTol")
    private Double teRampDiaDTol;

    @JsonView(View.Summary.class)
    @JsonProperty("ceRampDiaE")
    @Column(name = "ceRampDiaE")
    private Double ceRampDiaE;

    @JsonView(View.Summary.class)
    @JsonProperty("ceRampDiaETol")
    @Column(name = "ceRampDiaETol")
    private Double ceRampDiaETol;

    @JsonView(View.Summary.class)
    @JsonProperty("numRampsTe")
    @Column(name = "numRampsTe")
    private Integer numRampsTe;

    @JsonView(View.Summary.class)
    @JsonProperty("numRampsCe")
    @Column(name = "numRampsCe")
    private Integer numRampsCe;

    @JsonView(View.Summary.class)
    @JsonProperty("teRampDrainDiaF")
    @Column(name = "teRampDrainDiaF")
    private Double teRampDrainDiaF;

    @JsonView(View.Summary.class)
    @JsonProperty("teRampDrainDiaFTol")
    @Column(name = "teRampDrainDiaFTol")
    private Double teRampDrainDiaFTol;

    @JsonView(View.Summary.class)
    @JsonProperty("ceRampDrainDiaG")
    @Column(name = "ceRampDrainDiaG")
    private Double ceRampDrainDiaG;

    @JsonView(View.Summary.class)
    @JsonProperty("ceRampDrainDiaGTol")
    @Column(name = "ceRampDrainDiaGTol")
    private Double ceRampDrainDiaGTol;

    @JsonView(View.Summary.class)
    @JsonProperty("rotation")
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "rotation")
    private CriticalDimensionEnumVal rotation;

    @JsonView(View.Summary.class)
    @JsonProperty("numLocatingHoles")
    @Column(name = "numLocatingHoles")
    private Integer numLocatingHoles;

    @JsonView(View.Summary.class)
    @JsonProperty("diaLocatingHoles")
    @Column(name = "diaLocatingHoles")
    private Double diaLocatingHoles;

    @JsonView(View.Summary.class)
    @JsonProperty("degreesOfRamps")
    @Column(name = "degreesOfRamps")
    private Integer degreesOfRamps;

    @JsonView(View.Summary.class)
    @JsonProperty("material")
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "material")
    private CriticalDimensionEnumVal material;

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

    public Double getMaxThicknessA() {
        return maxThicknessA;
    }

    public void setMaxThicknessA(Double maxThicknessA) {
        this.maxThicknessA = maxThicknessA;
    }

    public Double getMinThicknessA() {
        return minThicknessA;
    }

    public void setMinThicknessA(Double minThicknessA) {
        this.minThicknessA = minThicknessA;
    }

    public Double getOdB() {
        return odB;
    }

    public void setOdB(Double odB) {
        this.odB = odB;
    }

    public Double getOdBTol() {
        return odBTol;
    }

    public void setOdBTol(Double odBTol) {
        this.odBTol = odBTol;
    }

    public Double getIdC() {
        return idC;
    }

    public void setIdC(Double idC) {
        this.idC = idC;
    }

    public Double getIdCTol() {
        return idCTol;
    }

    public void setIdCTol(Double idCTol) {
        this.idCTol = idCTol;
    }

    public Double getTeRampDiaD() {
        return teRampDiaD;
    }

    public void setTeRampDiaD(Double teRampDiaD) {
        this.teRampDiaD = teRampDiaD;
    }

    public Double getTeRampDiaDTol() {
        return teRampDiaDTol;
    }

    public void setTeRampDiaDTol(Double teRampDiaDTol) {
        this.teRampDiaDTol = teRampDiaDTol;
    }

    public Double getCeRampDiaE() {
        return ceRampDiaE;
    }

    public void setCeRampDiaE(Double ceRampDiaE) {
        this.ceRampDiaE = ceRampDiaE;
    }

    public Double getCeRampDiaETol() {
        return ceRampDiaETol;
    }

    public void setCeRampDiaETol(Double ceRampDiaETol) {
        this.ceRampDiaETol = ceRampDiaETol;
    }

    public Integer getNumRampsTe() {
        return numRampsTe;
    }

    public void setNumRampsTe(Integer numRampsTe) {
        this.numRampsTe = numRampsTe;
    }

    public Integer getNumRampsCe() {
        return numRampsCe;
    }

    public void setNumRampsCe(Integer numRampsCe) {
        this.numRampsCe = numRampsCe;
    }

    public Double getTeRampDrainDiaF() {
        return teRampDrainDiaF;
    }

    public void setTeRampDrainDiaF(Double teRampDrainDiaF) {
        this.teRampDrainDiaF = teRampDrainDiaF;
    }

    public Double getTeRampDrainDiaFTol() {
        return teRampDrainDiaFTol;
    }

    public void setTeRampDrainDiaFTol(Double teRampDrainDiaFTol) {
        this.teRampDrainDiaFTol = teRampDrainDiaFTol;
    }

    public Double getCeRampDrainDiaG() {
        return ceRampDrainDiaG;
    }

    public void setCeRampDrainDiaG(Double ceRampDrainDiaG) {
        this.ceRampDrainDiaG = ceRampDrainDiaG;
    }

    public Double getCeRampDrainDiaGTol() {
        return ceRampDrainDiaGTol;
    }

    public void setCeRampDrainDiaGTol(Double ceRampDrainDiaGTol) {
        this.ceRampDrainDiaGTol = ceRampDrainDiaGTol;
    }

    public CriticalDimensionEnumVal getRotation() {
        return rotation;
    }

    public void setRotation(CriticalDimensionEnumVal rotation) {
        this.rotation = rotation;
    }

    public Integer getNumLocatingHoles() {
        return numLocatingHoles;
    }

    public void setNumLocatingHoles(Integer numLocatingHoles) {
        this.numLocatingHoles = numLocatingHoles;
    }

    public Double getDiaLocatingHoles() {
        return diaLocatingHoles;
    }

    public void setDiaLocatingHoles(Double diaLocatingHoles) {
        this.diaLocatingHoles = diaLocatingHoles;
    }

    public Integer getDegreesOfRamps() {
        return degreesOfRamps;
    }

    public void setDegreesOfRamps(Integer degreesOfRamps) {
        this.degreesOfRamps = degreesOfRamps;
    }

    public CriticalDimensionEnumVal getMaterial() {
        return material;
    }

    public void setMaterial(CriticalDimensionEnumVal material) {
        this.material = material;
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
