package com.turbointernational.metadata.domain.part.types;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.domain.criticaldimension.CriticalDimensionEnumVal;
import com.turbointernational.metadata.domain.part.Part;
import com.turbointernational.metadata.web.View;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;


/**
 * Created by dmytro.trunykov@zorallabs.com on 2016-06-14 17:51:47.771081.
 */
@Entity
@Table(name = "washer")
@PrimaryKeyJoinColumn(name = "part_id")
public class Washer extends Part {

    //<editor-fold defaultstate="collapsed" desc="Properties: critical dimensions">

    @JsonView(View.Summary.class)
    @JsonProperty("od")
    @Column(name = "od")
    private Double od;

    @JsonView(View.Summary.class)
    @JsonProperty("odTol")
    @Column(name = "odTol")
    private Double odTol;

    @JsonView(View.Summary.class)
    @JsonProperty("id_")
    @Column(name = "id_")
    private Double id_;

    @JsonView(View.Summary.class)
    @JsonProperty("idTol")
    @Column(name = "idTol")
    private Double idTol;

    @JsonView(View.Summary.class)
    @JsonProperty("thickness")
    @Column(name = "thickness")
    private Double thickness;

    @JsonView(View.Summary.class)
    @JsonProperty("thicknessTol")
    @Column(name = "thicknessTol")
    private Double thicknessTol;

    @JsonView(View.Summary.class)
    @JsonProperty("type")
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "type")
    private CriticalDimensionEnumVal type;

    @JsonView(View.Summary.class)
    @JsonProperty("material")
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "material")
    private CriticalDimensionEnumVal material;

    @JsonView(View.Summary.class)
    @JsonProperty("platingCoating")
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "platingCoating")
    private CriticalDimensionEnumVal platingCoating;

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

    public Double getOd() {
        return od;
    }

    public void setOd(Double od) {
        this.od = od;
    }

    public Double getOdTol() {
        return odTol;
    }

    public void setOdTol(Double odTol) {
        this.odTol = odTol;
    }

    public Double getId_() {
        return id_;
    }

    public void setId_(Double id_) {
        this.id_ = id_;
    }

    public Double getIdTol() {
        return idTol;
    }

    public void setIdTol(Double idTol) {
        this.idTol = idTol;
    }

    public Double getThickness() {
        return thickness;
    }

    public void setThickness(Double thickness) {
        this.thickness = thickness;
    }

    public Double getThicknessTol() {
        return thicknessTol;
    }

    public void setThicknessTol(Double thicknessTol) {
        this.thicknessTol = thicknessTol;
    }

    public CriticalDimensionEnumVal getType() {
        return type;
    }

    public void setType(CriticalDimensionEnumVal type) {
        this.type = type;
    }

    public CriticalDimensionEnumVal getMaterial() {
        return material;
    }

    public void setMaterial(CriticalDimensionEnumVal material) {
        this.material = material;
    }

    public CriticalDimensionEnumVal getPlatingCoating() {
        return platingCoating;
    }

    public void setPlatingCoating(CriticalDimensionEnumVal platingCoating) {
        this.platingCoating = platingCoating;
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
