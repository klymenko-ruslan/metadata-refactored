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
@Table(name = "oil_deflector")
@PrimaryKeyJoinColumn(name = "part_id")
public class OilDeflector extends Part {

    //<editor-fold defaultstate="collapsed" desc="Properties: critical dimensions">
    @JsonView(View.Summary.class)
    @JsonProperty("outerDiameterA")
    @Column(name = "outerDiameterA")
    private Double outerDiameterA;

    @JsonView(View.Summary.class)
    @JsonProperty("innerDiameterB")
    @Column(name = "innerDiameterB")
    private Double innerDiameterB;

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
    @JsonProperty("mountingHoles")
    @Column(name = "mountingHoles")
    private Integer mountingHoles;

    @JsonView(View.Summary.class)
    @JsonProperty("mountingHoleDiaC")
    @Column(name = "mountingHoleDiaC")
    private Double mountingHoleDiaC;

    @JsonView(View.Summary.class)
    @JsonProperty("matLThicknessD")
    @Column(name = "matLThicknessD")
    private Double matLThicknessD;

    @JsonView(View.Summary.class)
    @JsonProperty("platingCoating")
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "platingCoating")
    private CriticalDimensionEnumVal platingCoating;

    @JsonView(View.Summary.class)
    @JsonProperty("diagram")
    @Column(name = "diagram")
    private Integer diagram;


    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Getters and setters: critical dimensions">
    public Double getOuterdiametera() {
        return outerDiameterA;
    }

    public void setOuterdiametera(Double outerDiameterA) {
        this.outerDiameterA = outerDiameterA;
    }

    public Double getInnerdiameterb() {
        return innerDiameterB;
    }

    public void setInnerdiameterb(Double innerDiameterB) {
        this.innerDiameterB = innerDiameterB;
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

    public Integer getMountingholes() {
        return mountingHoles;
    }

    public void setMountingholes(Integer mountingHoles) {
        this.mountingHoles = mountingHoles;
    }

    public Double getMountingholediac() {
        return mountingHoleDiaC;
    }

    public void setMountingholediac(Double mountingHoleDiaC) {
        this.mountingHoleDiaC = mountingHoleDiaC;
    }

    public Double getMatlthicknessd() {
        return matLThicknessD;
    }

    public void setMatlthicknessd(Double matLThicknessD) {
        this.matLThicknessD = matLThicknessD;
    }

    public CriticalDimensionEnumVal getPlatingcoating() {
        return platingCoating;
    }

    public void setPlatingcoating(CriticalDimensionEnumVal platingCoating) {
        this.platingCoating = platingCoating;
    }

    public Integer getDiagram() {
        return diagram;
    }

    public void setDiagram(Integer diagram) {
        this.diagram = diagram;
    }


    //</editor-fold>
}
