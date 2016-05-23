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
@Table(name = "piston_ring")
@PrimaryKeyJoinColumn(name = "part_id")
public class PistonRing extends Part {
    //<editor-fold defaultstate="collapsed" desc="Properties: critical dimensions">
    @JsonView(View.Summary.class)
    @JsonProperty("installedDiameterA")
    @Column(name = "installedDiameterA")
    private Double installedDiameterA;

    @JsonView(View.Summary.class)
    @JsonProperty("gapInstalledDiameterB")
    @Column(name = "gapInstalledDiameterB")
    private Double gapInstalledDiameterB;

    @JsonView(View.Summary.class)
    @JsonProperty("thicknessC")
    @Column(name = "thicknessC")
    private Double thicknessC;

    @JsonView(View.Summary.class)
    @JsonProperty("thicknessCTol")
    @Column(name = "thicknessCTol")
    private Double thicknessCTol;

    @JsonView(View.Summary.class)
    @JsonProperty("widthD")
    @Column(name = "widthD")
    private Double widthD;

    @JsonView(View.Summary.class)
    @JsonProperty("widthDUpperTol")
    @Column(name = "widthDUpperTol")
    private Double widthDUpperTol;

    @JsonView(View.Summary.class)
    @JsonProperty("widthDLowerTol")
    @Column(name = "widthDLowerTol")
    private Double widthDLowerTol;

    @JsonView(View.Summary.class)
    @JsonProperty("material")
    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "material")
    private CriticalDimensionEnumVal material;

    @JsonView(View.Summary.class)
    @JsonProperty("type")
    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "type")
    private CriticalDimensionEnumVal type;

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
    public Double getInstalleddiametera() {
        return installedDiameterA;
    }

    public void setInstalleddiametera(Double installedDiameterA) {
        this.installedDiameterA = installedDiameterA;
    }

    public Double getGapinstalleddiameterb() {
        return gapInstalledDiameterB;
    }

    public void setGapinstalleddiameterb(Double gapInstalledDiameterB) {
        this.gapInstalledDiameterB = gapInstalledDiameterB;
    }

    public Double getThicknessc() {
        return thicknessC;
    }

    public void setThicknessc(Double thicknessC) {
        this.thicknessC = thicknessC;
    }

    public Double getThicknessctol() {
        return thicknessCTol;
    }

    public void setThicknessctol(Double thicknessCTol) {
        this.thicknessCTol = thicknessCTol;
    }

    public Double getWidthd() {
        return widthD;
    }

    public void setWidthd(Double widthD) {
        this.widthD = widthD;
    }

    public Double getWidthduppertol() {
        return widthDUpperTol;
    }

    public void setWidthduppertol(Double widthDUpperTol) {
        this.widthDUpperTol = widthDUpperTol;
    }

    public Double getWidthdlowertol() {
        return widthDLowerTol;
    }

    public void setWidthdlowertol(Double widthDLowerTol) {
        this.widthDLowerTol = widthDLowerTol;
    }

    public CriticalDimensionEnumVal getMaterial() {
        return material;
    }

    public void setMaterial(CriticalDimensionEnumVal material) {
        this.material = material;
    }

    public CriticalDimensionEnumVal getType() {
        return type;
    }

    public void setType(CriticalDimensionEnumVal type) {
        this.type = type;
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
