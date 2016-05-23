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
@Table(name = "journal_bearing_spacer")
@PrimaryKeyJoinColumn(name = "part_id")
public class JournalBearingSpacer extends Part {
    //<editor-fold defaultstate="collapsed" desc="Properties: critical dimensions">
    @JsonView(View.Summary.class)
    @JsonProperty("outerDiameterA")
    @Column(name = "outerDiameterA")
    private Double outerDiameterA;

    @JsonView(View.Summary.class)
    @JsonProperty("outerDiameterATol")
    @Column(name = "outerDiameterATol")
    private Double outerDiameterATol;

    @JsonView(View.Summary.class)
    @JsonProperty("innerDiameterB")
    @Column(name = "innerDiameterB")
    private Double innerDiameterB;

    @JsonView(View.Summary.class)
    @JsonProperty("innerDiameterBTol")
    @Column(name = "innerDiameterBTol")
    private Double innerDiameterBTol;

    @JsonView(View.Summary.class)
    @JsonProperty("lengthC")
    @Column(name = "lengthC")
    private Double lengthC;

    @JsonView(View.Summary.class)
    @JsonProperty("lengthCTol")
    @Column(name = "lengthCTol")
    private Double lengthCTol;

    @JsonView(View.Summary.class)
    @JsonProperty("notchesSide")
    @Column(name = "notchesSide")
    private Integer notchesSide;

    @JsonView(View.Summary.class)
    @JsonProperty("holeDiamD")
    @Column(name = "holeDiamD")
    private Double holeDiamD;

    @JsonView(View.Summary.class)
    @JsonProperty("holeDiamDTol")
    @Column(name = "holeDiamDTol")
    private Double holeDiamDTol;

    @JsonView(View.Summary.class)
    @JsonProperty("outerConfiguration")
    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "outerConfiguration")
    private CriticalDimensionEnumVal outerConfiguration;

    @JsonView(View.Summary.class)
    @JsonProperty("pressedFloating")
    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "pressedFloating")
    private CriticalDimensionEnumVal pressedFloating;

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
    public Double getOuterdiametera() {
        return outerDiameterA;
    }

    public void setOuterdiametera(Double outerDiameterA) {
        this.outerDiameterA = outerDiameterA;
    }

    public Double getOuterdiameteratol() {
        return outerDiameterATol;
    }

    public void setOuterdiameteratol(Double outerDiameterATol) {
        this.outerDiameterATol = outerDiameterATol;
    }

    public Double getInnerdiameterb() {
        return innerDiameterB;
    }

    public void setInnerdiameterb(Double innerDiameterB) {
        this.innerDiameterB = innerDiameterB;
    }

    public Double getInnerdiameterbtol() {
        return innerDiameterBTol;
    }

    public void setInnerdiameterbtol(Double innerDiameterBTol) {
        this.innerDiameterBTol = innerDiameterBTol;
    }

    public Double getLengthc() {
        return lengthC;
    }

    public void setLengthc(Double lengthC) {
        this.lengthC = lengthC;
    }

    public Double getLengthctol() {
        return lengthCTol;
    }

    public void setLengthctol(Double lengthCTol) {
        this.lengthCTol = lengthCTol;
    }

    public Integer getNotchesside() {
        return notchesSide;
    }

    public void setNotchesside(Integer notchesSide) {
        this.notchesSide = notchesSide;
    }

    public Double getHolediamd() {
        return holeDiamD;
    }

    public void setHolediamd(Double holeDiamD) {
        this.holeDiamD = holeDiamD;
    }

    public Double getHolediamdtol() {
        return holeDiamDTol;
    }

    public void setHolediamdtol(Double holeDiamDTol) {
        this.holeDiamDTol = holeDiamDTol;
    }

    public CriticalDimensionEnumVal getOuterconfiguration() {
        return outerConfiguration;
    }

    public void setOuterconfiguration(CriticalDimensionEnumVal outerConfiguration) {
        this.outerConfiguration = outerConfiguration;
    }

    public CriticalDimensionEnumVal getPressedfloating() {
        return pressedFloating;
    }

    public void setPressedfloating(CriticalDimensionEnumVal pressedFloating) {
        this.pressedFloating = pressedFloating;
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
