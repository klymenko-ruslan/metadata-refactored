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
 * Created by dmytro.trunykov@zorallabs.com on 2016-08-01 17:06:08.758950.
 */
@Entity
@Table(name = "journal_bearing_spacer")
@DiscriminatorValue("37")
@PrimaryKeyJoinColumn(name = "part_id")
public class JournalBearingSpacer extends Part {

    private static final long serialVersionUID = 2151934650356564134L;

    // <editor-fold defaultstate="collapsed" desc="Properties: critical
    // dimensions">

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
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "outerConfiguration")
    private CriticalDimensionEnumVal outerConfiguration;

    @JsonView(View.Summary.class)
    @JsonProperty("pressedFloating")
    @ManyToOne(fetch = LAZY)
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

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Getters and setters: critical
    // dimensions">

    public Double getOuterDiameterA() {
        return outerDiameterA;
    }

    public void setOuterDiameterA(Double outerDiameterA) {
        this.outerDiameterA = outerDiameterA;
    }

    public Double getOuterDiameterATol() {
        return outerDiameterATol;
    }

    public void setOuterDiameterATol(Double outerDiameterATol) {
        this.outerDiameterATol = outerDiameterATol;
    }

    public Double getInnerDiameterB() {
        return innerDiameterB;
    }

    public void setInnerDiameterB(Double innerDiameterB) {
        this.innerDiameterB = innerDiameterB;
    }

    public Double getInnerDiameterBTol() {
        return innerDiameterBTol;
    }

    public void setInnerDiameterBTol(Double innerDiameterBTol) {
        this.innerDiameterBTol = innerDiameterBTol;
    }

    public Double getLengthC() {
        return lengthC;
    }

    public void setLengthC(Double lengthC) {
        this.lengthC = lengthC;
    }

    public Double getLengthCTol() {
        return lengthCTol;
    }

    public void setLengthCTol(Double lengthCTol) {
        this.lengthCTol = lengthCTol;
    }

    public Integer getNotchesSide() {
        return notchesSide;
    }

    public void setNotchesSide(Integer notchesSide) {
        this.notchesSide = notchesSide;
    }

    public Double getHoleDiamD() {
        return holeDiamD;
    }

    public void setHoleDiamD(Double holeDiamD) {
        this.holeDiamD = holeDiamD;
    }

    public Double getHoleDiamDTol() {
        return holeDiamDTol;
    }

    public void setHoleDiamDTol(Double holeDiamDTol) {
        this.holeDiamDTol = holeDiamDTol;
    }

    public CriticalDimensionEnumVal getOuterConfiguration() {
        return outerConfiguration;
    }

    public void setOuterConfiguration(CriticalDimensionEnumVal outerConfiguration) {
        this.outerConfiguration = outerConfiguration;
    }

    public CriticalDimensionEnumVal getPressedFloating() {
        return pressedFloating;
    }

    public void setPressedFloating(CriticalDimensionEnumVal pressedFloating) {
        this.pressedFloating = pressedFloating;
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

    // </editor-fold>

}
