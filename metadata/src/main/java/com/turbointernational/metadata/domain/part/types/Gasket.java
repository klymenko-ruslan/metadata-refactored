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
@Table(name = "gasket")
@PrimaryKeyJoinColumn(name = "part_id")
public class Gasket extends Part {
    //<editor-fold defaultstate="collapsed" desc="Properties: critical dimensions">
    @JsonView(View.Summary.class)
    @JsonProperty("type")
    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "type")
    private CriticalDimensionEnumVal type;

    @JsonView(View.Summary.class)
    @JsonProperty("splitSinglePassage")
    @ManyToOne(fetch = EAGER)
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
    @JsonProperty("a")
    @Column(name = "a")
    private Double a;

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
    @JsonProperty("thicknessJ")
    @Column(name = "thicknessJ")
    private Double thicknessJ;

    @JsonView(View.Summary.class)
    @JsonProperty("material")
    @ManyToOne(fetch = EAGER)
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

    public CriticalDimensionEnumVal getSplitsinglepassage() {
        return splitSinglePassage;
    }

    public void setSplitsinglepassage(CriticalDimensionEnumVal splitSinglePassage) {
        this.splitSinglePassage = splitSinglePassage;
    }

    public String getShape() {
        return shape;
    }

    public void setShape(String shape) {
        this.shape = shape;
    }

    public Integer getBoltholes() {
        return boltHoles;
    }

    public void setBoltholes(Integer boltHoles) {
        this.boltHoles = boltHoles;
    }

    public Double getA() {
        return a;
    }

    public void setA(Double a) {
        this.a = a;
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

    public Double getThicknessj() {
        return thicknessJ;
    }

    public void setThicknessj(Double thicknessJ) {
        this.thicknessJ = thicknessJ;
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
