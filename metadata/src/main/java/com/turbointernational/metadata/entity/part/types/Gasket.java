package com.turbointernational.metadata.entity.part.types;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.entity.CriticalDimensionEnumVal;
import com.turbointernational.metadata.entity.part.Part;
import com.turbointernational.metadata.util.View;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;


/**
 * Created by dmytro.trunykov@zorallabs.com on 2016-08-01 17:06:08.758266.
 */
@Entity
@Table(name = "gasket")
@PrimaryKeyJoinColumn(name = "part_id")
public class Gasket extends Part {

    //<editor-fold defaultstate="collapsed" desc="Properties: critical dimensions">

    @JsonView(View.Summary.class)
    @JsonProperty("type")
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "type")
    private CriticalDimensionEnumVal type;

    @JsonView(View.Summary.class)
    @JsonProperty("splitSinglePassage")
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "splitSinglePassage")
    private CriticalDimensionEnumVal splitSinglePassage;

    @JsonView(View.Summary.class)
    @JsonProperty("shape")
    @Column(name = "shape")
    private String shape;

    @JsonView(View.Summary.class)
    @JsonProperty("boltHoles")
    @Column(name = "boltHoles")
    private Integer boltHoles;

    @JsonView(View.Summary.class)
    @JsonProperty("passageA")
    @Column(name = "passageA")
    private Double passageA;

    @JsonView(View.Summary.class)
    @JsonProperty("b")
    @Column(name = "b")
    private Double b;

    @JsonView(View.Summary.class)
    @JsonProperty("c")
    @Column(name = "c")
    private Double c;

    @JsonView(View.Summary.class)
    @JsonProperty("d")
    @Column(name = "d")
    private Double d;

    @JsonView(View.Summary.class)
    @JsonProperty("e")
    @Column(name = "e")
    private Double e;

    @JsonView(View.Summary.class)
    @JsonProperty("f")
    @Column(name = "f")
    private Double f;

    @JsonView(View.Summary.class)
    @JsonProperty("g")
    @Column(name = "g")
    private Double g;

    @JsonView(View.Summary.class)
    @JsonProperty("h")
    @Column(name = "h")
    private Double h;

    @JsonView(View.Summary.class)
    @JsonProperty("thickness")
    @Column(name = "thickness")
    private Double thickness;

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

    public CriticalDimensionEnumVal getType() {
        return type;
    }

    public void setType(CriticalDimensionEnumVal type) {
        this.type = type;
    }

    public CriticalDimensionEnumVal getSplitSinglePassage() {
        return splitSinglePassage;
    }

    public void setSplitSinglePassage(CriticalDimensionEnumVal splitSinglePassage) {
        this.splitSinglePassage = splitSinglePassage;
    }

    public String getShape() {
        return shape;
    }

    public void setShape(String shape) {
        this.shape = shape;
    }

    public Integer getBoltHoles() {
        return boltHoles;
    }

    public void setBoltHoles(Integer boltHoles) {
        this.boltHoles = boltHoles;
    }

    public Double getPassageA() {
        return passageA;
    }

    public void setPassageA(Double passageA) {
        this.passageA = passageA;
    }

    public Double getB() {
        return b;
    }

    public void setB(Double b) {
        this.b = b;
    }

    public Double getC() {
        return c;
    }

    public void setC(Double c) {
        this.c = c;
    }

    public Double getD() {
        return d;
    }

    public void setD(Double d) {
        this.d = d;
    }

    public Double getE() {
        return e;
    }

    public void setE(Double e) {
        this.e = e;
    }

    public Double getF() {
        return f;
    }

    public void setF(Double f) {
        this.f = f;
    }

    public Double getG() {
        return g;
    }

    public void setG(Double g) {
        this.g = g;
    }

    public Double getH() {
        return h;
    }

    public void setH(Double h) {
        this.h = h;
    }

    public Double getThickness() {
        return thickness;
    }

    public void setThickness(Double thickness) {
        this.thickness = thickness;
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
