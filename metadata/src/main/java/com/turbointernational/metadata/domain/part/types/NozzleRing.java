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
@Table(name = "nozzle_ring")
@PrimaryKeyJoinColumn(name = "part_id")
public class NozzleRing extends Part {

    //<editor-fold defaultstate="collapsed" desc="Properties: critical dimensions">
    @JsonView(View.Summary.class)
    @JsonProperty("unisonRingDiaA")
    @Column(name = "unisonRingDiaA")
    private Double unisonRingDiaA;

    @JsonView(View.Summary.class)
    @JsonProperty("unisonRingDiaATol")
    @Column(name = "unisonRingDiaATol")
    private Double unisonRingDiaATol;

    @JsonView(View.Summary.class)
    @JsonProperty("outerDiameterB")
    @Column(name = "outerDiameterB")
    private Double outerDiameterB;

    @JsonView(View.Summary.class)
    @JsonProperty("outerDiameterBTol")
    @Column(name = "outerDiameterBTol")
    private Double outerDiameterBTol;

    @JsonView(View.Summary.class)
    @JsonProperty("innerDiameterC")
    @Column(name = "innerDiameterC")
    private Double innerDiameterC;

    @JsonView(View.Summary.class)
    @JsonProperty("innerDiameterCTol")
    @Column(name = "innerDiameterCTol")
    private Double innerDiameterCTol;

    @JsonView(View.Summary.class)
    @JsonProperty("numVanes")
    @Column(name = "numVanes")
    private Integer numVanes;

    @JsonView(View.Summary.class)
    @JsonProperty("numRollers")
    @Column(name = "numRollers")
    private Integer numRollers;

    @JsonView(View.Summary.class)
    @JsonProperty("specialFeatures")
    @Column(name = "specialFeatures")
    private String specialFeatures;

    @JsonView(View.Summary.class)
    @JsonProperty("weight")
    @Column(name = "weight")
    private Double weight;

    @JsonView(View.Summary.class)
    @JsonProperty("numMountingHoles")
    @Column(name = "numMountingHoles")
    private Integer numMountingHoles;

    @JsonView(View.Summary.class)
    @JsonProperty("centerHolePassageD")
    @Column(name = "centerHolePassageD")
    private Double centerHolePassageD;

    @JsonView(View.Summary.class)
    @JsonProperty("rotation")
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "rotation")
    private CriticalDimensionEnumVal rotation;

    @JsonView(View.Summary.class)
    @JsonProperty("diagram")
    @Column(name = "diagram")
    private Integer diagram;


    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Getters and setters: critical dimensions">
    public Double getUnisonringdiaa() {
        return unisonRingDiaA;
    }

    public void setUnisonringdiaa(Double unisonRingDiaA) {
        this.unisonRingDiaA = unisonRingDiaA;
    }

    public Double getUnisonringdiaatol() {
        return unisonRingDiaATol;
    }

    public void setUnisonringdiaatol(Double unisonRingDiaATol) {
        this.unisonRingDiaATol = unisonRingDiaATol;
    }

    public Double getOuterdiameterb() {
        return outerDiameterB;
    }

    public void setOuterdiameterb(Double outerDiameterB) {
        this.outerDiameterB = outerDiameterB;
    }

    public Double getOuterdiameterbtol() {
        return outerDiameterBTol;
    }

    public void setOuterdiameterbtol(Double outerDiameterBTol) {
        this.outerDiameterBTol = outerDiameterBTol;
    }

    public Double getInnerdiameterc() {
        return innerDiameterC;
    }

    public void setInnerdiameterc(Double innerDiameterC) {
        this.innerDiameterC = innerDiameterC;
    }

    public Double getInnerdiameterctol() {
        return innerDiameterCTol;
    }

    public void setInnerdiameterctol(Double innerDiameterCTol) {
        this.innerDiameterCTol = innerDiameterCTol;
    }

    public Integer getNumvanes() {
        return numVanes;
    }

    public void setNumvanes(Integer numVanes) {
        this.numVanes = numVanes;
    }

    public Integer getNumrollers() {
        return numRollers;
    }

    public void setNumrollers(Integer numRollers) {
        this.numRollers = numRollers;
    }

    public String getSpecialfeatures() {
        return specialFeatures;
    }

    public void setSpecialfeatures(String specialFeatures) {
        this.specialFeatures = specialFeatures;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Integer getNummountingholes() {
        return numMountingHoles;
    }

    public void setNummountingholes(Integer numMountingHoles) {
        this.numMountingHoles = numMountingHoles;
    }

    public Double getCenterholepassaged() {
        return centerHolePassageD;
    }

    public void setCenterholepassaged(Double centerHolePassageD) {
        this.centerHolePassageD = centerHolePassageD;
    }

    public CriticalDimensionEnumVal getRotation() {
        return rotation;
    }

    public void setRotation(CriticalDimensionEnumVal rotation) {
        this.rotation = rotation;
    }

    public Integer getDiagram() {
        return diagram;
    }

    public void setDiagram(Integer diagram) {
        this.diagram = diagram;
    }


    //</editor-fold>
}
