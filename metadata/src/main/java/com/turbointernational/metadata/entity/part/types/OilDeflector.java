package com.turbointernational.metadata.entity.part.types;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.entity.CriticalDimensionEnumVal;
import com.turbointernational.metadata.entity.part.Part;
import com.turbointernational.metadata.util.View;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;


/**
 * Created by dmytro.trunykov@zorallabs.com on 2016-08-01 17:06:08.759539.
 */
@Entity
@Table(name = "oil_deflector")
@DiscriminatorValue("18")
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

    public Double getOuterDiameterA() {
        return outerDiameterA;
    }

    public void setOuterDiameterA(Double outerDiameterA) {
        this.outerDiameterA = outerDiameterA;
    }

    public Double getInnerDiameterB() {
        return innerDiameterB;
    }

    public void setInnerDiameterB(Double innerDiameterB) {
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

    public Integer getMountingHoles() {
        return mountingHoles;
    }

    public void setMountingHoles(Integer mountingHoles) {
        this.mountingHoles = mountingHoles;
    }

    public Double getMountingHoleDiaC() {
        return mountingHoleDiaC;
    }

    public void setMountingHoleDiaC(Double mountingHoleDiaC) {
        this.mountingHoleDiaC = mountingHoleDiaC;
    }

    public Double getMatLThicknessD() {
        return matLThicknessD;
    }

    public void setMatLThicknessD(Double matLThicknessD) {
        this.matLThicknessD = matLThicknessD;
    }

    public CriticalDimensionEnumVal getPlatingCoating() {
        return platingCoating;
    }

    public void setPlatingCoating(CriticalDimensionEnumVal platingCoating) {
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
