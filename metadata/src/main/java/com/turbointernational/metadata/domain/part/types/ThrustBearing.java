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
    public Double getMaxthicknessa() {
        return maxThicknessA;
    }

    public void setMaxthicknessa(Double maxThicknessA) {
        this.maxThicknessA = maxThicknessA;
    }

    public Double getMinthicknessa() {
        return minThicknessA;
    }

    public void setMinthicknessa(Double minThicknessA) {
        this.minThicknessA = minThicknessA;
    }

    public Double getOdb() {
        return odB;
    }

    public void setOdb(Double odB) {
        this.odB = odB;
    }

    public Double getOdbtol() {
        return odBTol;
    }

    public void setOdbtol(Double odBTol) {
        this.odBTol = odBTol;
    }

    public Double getIdc() {
        return idC;
    }

    public void setIdc(Double idC) {
        this.idC = idC;
    }

    public Double getIdctol() {
        return idCTol;
    }

    public void setIdctol(Double idCTol) {
        this.idCTol = idCTol;
    }

    public Double getTerampdiad() {
        return teRampDiaD;
    }

    public void setTerampdiad(Double teRampDiaD) {
        this.teRampDiaD = teRampDiaD;
    }

    public Double getTerampdiadtol() {
        return teRampDiaDTol;
    }

    public void setTerampdiadtol(Double teRampDiaDTol) {
        this.teRampDiaDTol = teRampDiaDTol;
    }

    public Double getCerampdiae() {
        return ceRampDiaE;
    }

    public void setCerampdiae(Double ceRampDiaE) {
        this.ceRampDiaE = ceRampDiaE;
    }

    public Double getCerampdiaetol() {
        return ceRampDiaETol;
    }

    public void setCerampdiaetol(Double ceRampDiaETol) {
        this.ceRampDiaETol = ceRampDiaETol;
    }

    public Integer getNumrampste() {
        return numRampsTe;
    }

    public void setNumrampste(Integer numRampsTe) {
        this.numRampsTe = numRampsTe;
    }

    public Integer getNumrampsce() {
        return numRampsCe;
    }

    public void setNumrampsce(Integer numRampsCe) {
        this.numRampsCe = numRampsCe;
    }

    public Double getTerampdraindiaf() {
        return teRampDrainDiaF;
    }

    public void setTerampdraindiaf(Double teRampDrainDiaF) {
        this.teRampDrainDiaF = teRampDrainDiaF;
    }

    public Double getTerampdraindiaftol() {
        return teRampDrainDiaFTol;
    }

    public void setTerampdraindiaftol(Double teRampDrainDiaFTol) {
        this.teRampDrainDiaFTol = teRampDrainDiaFTol;
    }

    public Double getCerampdraindiag() {
        return ceRampDrainDiaG;
    }

    public void setCerampdraindiag(Double ceRampDrainDiaG) {
        this.ceRampDrainDiaG = ceRampDrainDiaG;
    }

    public Double getCerampdraindiagtol() {
        return ceRampDrainDiaGTol;
    }

    public void setCerampdraindiagtol(Double ceRampDrainDiaGTol) {
        this.ceRampDrainDiaGTol = ceRampDrainDiaGTol;
    }

    public CriticalDimensionEnumVal getRotation() {
        return rotation;
    }

    public void setRotation(CriticalDimensionEnumVal rotation) {
        this.rotation = rotation;
    }

    public Integer getNumlocatingholes() {
        return numLocatingHoles;
    }

    public void setNumlocatingholes(Integer numLocatingHoles) {
        this.numLocatingHoles = numLocatingHoles;
    }

    public Double getDialocatingholes() {
        return diaLocatingHoles;
    }

    public void setDialocatingholes(Double diaLocatingHoles) {
        this.diaLocatingHoles = diaLocatingHoles;
    }

    public Integer getDegreesoframps() {
        return degreesOfRamps;
    }

    public void setDegreesoframps(Integer degreesOfRamps) {
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
