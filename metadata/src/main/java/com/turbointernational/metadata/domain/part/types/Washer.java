package com.turbointernational.metadata.domain.part.types;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.domain.criticaldimension.CriticalDimensionEnumVal;
import com.turbointernational.metadata.domain.part.Part;
import com.turbointernational.metadata.web.View;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;


/**
 * Created by dmytro.trunykov@zorallabs.com on 2016-05-26 10:44:43.041980.
 */
@Entity
@Table(name = "washer")
@PrimaryKeyJoinColumn(name = "part_id")
public class Washer extends Part {

    //<editor-fold defaultstate="collapsed" desc="Properties: critical dimensions">

    @JsonView(View.Summary.class)
    @JsonProperty("odA")
    @Column(name = "odA")
    private Double odA;

    @JsonView(View.Summary.class)
    @JsonProperty("odATol")
    @Column(name = "odATol")
    private Double odATol;

    @JsonView(View.Summary.class)
    @JsonProperty("idB")
    @Column(name = "idB")
    private Double idB;

    @JsonView(View.Summary.class)
    @JsonProperty("idTolB")
    @Column(name = "idTolB")
    private Double idTolB;

    @JsonView(View.Summary.class)
    @JsonProperty("thicknessC")
    @Column(name = "thicknessC")
    private Double thicknessC;

    @JsonView(View.Summary.class)
    @JsonProperty("thicknessTolC")
    @Column(name = "thicknessTolC")
    private Double thicknessTolC;

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

    public Double getOdA() {
        return odA;
    }

    public void setOdA(Double odA) {
        this.odA = odA;
    }

    public Double getOdATol() {
        return odATol;
    }

    public void setOdATol(Double odATol) {
        this.odATol = odATol;
    }

    public Double getIdB() {
        return idB;
    }

    public void setIdB(Double idB) {
        this.idB = idB;
    }

    public Double getIdTolB() {
        return idTolB;
    }

    public void setIdTolB(Double idTolB) {
        this.idTolB = idTolB;
    }

    public Double getThicknessC() {
        return thicknessC;
    }

    public void setThicknessC(Double thicknessC) {
        this.thicknessC = thicknessC;
    }

    public Double getThicknessTolC() {
        return thicknessTolC;
    }

    public void setThicknessTolC(Double thicknessTolC) {
        this.thicknessTolC = thicknessTolC;
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
