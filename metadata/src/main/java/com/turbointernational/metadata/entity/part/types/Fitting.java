package com.turbointernational.metadata.entity.part.types;

import static javax.persistence.FetchType.LAZY;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.entity.CriticalDimensionEnumVal;
import com.turbointernational.metadata.entity.part.Part;
import com.turbointernational.metadata.util.View;


/**
 * Created by dmytro.trunykov@zorallabs.com on 2016-08-01 17:06:08.758117.
 */
@Entity
@Table(name = "fitting")
@DiscriminatorValue("36")
@PrimaryKeyJoinColumn(name = "part_id")
public class Fitting extends Part {

    private static final long serialVersionUID = 8521724113036926123L;

    //<editor-fold defaultstate="collapsed" desc="Properties: critical dimensions">

    @JsonView(View.Summary.class)
    @JsonProperty("diameterA")
    @Column(name = "diameterA")
    private Double diameterA;

    @JsonView(View.Summary.class)
    @JsonProperty("diameterB")
    @Column(name = "diameterB")
    private Double diameterB;

    @JsonView(View.Summary.class)
    @JsonProperty("diameterC")
    @Column(name = "diameterC")
    private Double diameterC;

    @JsonView(View.Summary.class)
    @JsonProperty("lengthD")
    @Column(name = "lengthD")
    private Double lengthD;

    @JsonView(View.Summary.class)
    @JsonProperty("angle")
    @Column(name = "angle")
    private Double angle;

    @JsonView(View.Summary.class)
    @JsonProperty("thread")
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "thread")
    private CriticalDimensionEnumVal thread;

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

    public Double getDiameterA() {
        return diameterA;
    }

    public void setDiameterA(Double diameterA) {
        this.diameterA = diameterA;
    }

    public Double getDiameterB() {
        return diameterB;
    }

    public void setDiameterB(Double diameterB) {
        this.diameterB = diameterB;
    }

    public Double getDiameterC() {
        return diameterC;
    }

    public void setDiameterC(Double diameterC) {
        this.diameterC = diameterC;
    }

    public Double getLengthD() {
        return lengthD;
    }

    public void setLengthD(Double lengthD) {
        this.lengthD = lengthD;
    }

    public Double getAngle() {
        return angle;
    }

    public void setAngle(Double angle) {
        this.angle = angle;
    }

    public CriticalDimensionEnumVal getThread() {
        return thread;
    }

    public void setThread(CriticalDimensionEnumVal thread) {
        this.thread = thread;
    }

    @Override
    public Double getWeight() {
        return weight;
    }

    @Override
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
