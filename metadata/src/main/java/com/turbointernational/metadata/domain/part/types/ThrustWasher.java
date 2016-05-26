package com.turbointernational.metadata.domain.part.types;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.domain.criticaldimension.CriticalDimensionEnumVal;
import com.turbointernational.metadata.domain.part.Part;
import com.turbointernational.metadata.web.View;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;


/**
 * Created by dmytro.trunykov@zorallabs.com on 2016-05-26 10:44:43.041842.
 */
@Entity
@Table(name = "thrust_washer")
@PrimaryKeyJoinColumn(name = "part_id")
public class ThrustWasher extends Part {

    //<editor-fold defaultstate="collapsed" desc="Properties: critical dimensions">

    @JsonView(View.Summary.class)
    @JsonProperty("aThickness")
    @Column(name = "aThickness")
    private Double aThickness;

    @JsonView(View.Summary.class)
    @JsonProperty("aThicknessTol")
    @Column(name = "aThicknessTol")
    private Double aThicknessTol;

    @JsonView(View.Summary.class)
    @JsonProperty("bOd")
    @Column(name = "bOd")
    private Double bOd;

    @JsonView(View.Summary.class)
    @JsonProperty("bOdTol")
    @Column(name = "bOdTol")
    private Double bOdTol;

    @JsonView(View.Summary.class)
    @JsonProperty("cId")
    @Column(name = "cId")
    private Double cId;

    @JsonView(View.Summary.class)
    @JsonProperty("cIdTol")
    @Column(name = "cIdTol")
    private Double cIdTol;

    @JsonView(View.Summary.class)
    @JsonProperty("material")
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "material")
    private CriticalDimensionEnumVal material;

    @JsonView(View.Summary.class)
    @JsonProperty("weight")
    @Column(name = "weight")
    private Double weight;

    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Getters and setters: critical dimensions">

    public Double getAThickness() {
        return aThickness;
    }

    public void setAThickness(Double aThickness) {
        this.aThickness = aThickness;
    }

    public Double getAThicknessTol() {
        return aThicknessTol;
    }

    public void setAThicknessTol(Double aThicknessTol) {
        this.aThicknessTol = aThicknessTol;
    }

    public Double getBOd() {
        return bOd;
    }

    public void setBOd(Double bOd) {
        this.bOd = bOd;
    }

    public Double getBOdTol() {
        return bOdTol;
    }

    public void setBOdTol(Double bOdTol) {
        this.bOdTol = bOdTol;
    }

    public Double getCId() {
        return cId;
    }

    public void setCId(Double cId) {
        this.cId = cId;
    }

    public Double getCIdTol() {
        return cIdTol;
    }

    public void setCIdTol(Double cIdTol) {
        this.cIdTol = cIdTol;
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

    //</editor-fold>

}
