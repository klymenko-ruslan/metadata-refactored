package com.turbointernational.metadata.domain.part.types;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.domain.criticaldimension.CriticalDimensionEnumVal;
import com.turbointernational.metadata.domain.part.Part;
import com.turbointernational.metadata.web.View;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;


/**
 * Created by dmytro.trunykov@zorallabs.com on 2016-05-26 10:44:43.040587.
 */
@Entity
@Table(name = "turbine_wheel")
@PrimaryKeyJoinColumn(name = "part_id")
public class TurbineWheel extends Part {

    //<editor-fold defaultstate="collapsed" desc="Properties: critical dimensions">

    @JsonView(View.Summary.class)
    @JsonProperty("inducerDiameterA")
    @Column(name = "inducerDiameterA")
    private Double inducerDiameterA;

    @JsonView(View.Summary.class)
    @JsonProperty("inducerDiameterATol")
    @Column(name = "inducerDiameterATol")
    private Double inducerDiameterATol;

    @JsonView(View.Summary.class)
    @JsonProperty("exducerDiameterB")
    @Column(name = "exducerDiameterB")
    private Double exducerDiameterB;

    @JsonView(View.Summary.class)
    @JsonProperty("exducerDiameterBTol")
    @Column(name = "exducerDiameterBTol")
    private Double exducerDiameterBTol;

    @JsonView(View.Summary.class)
    @JsonProperty("tipHeightC")
    @Column(name = "tipHeightC")
    private Double tipHeightC;

    @JsonView(View.Summary.class)
    @JsonProperty("tipHeightCTol")
    @Column(name = "tipHeightCTol")
    private Double tipHeightCTol;

    @JsonView(View.Summary.class)
    @JsonProperty("maxJournalDiameterD")
    @Column(name = "maxJournalDiameterD")
    private Double maxJournalDiameterD;

    @JsonView(View.Summary.class)
    @JsonProperty("minJournalDiameterD")
    @Column(name = "minJournalDiameterD")
    private Double minJournalDiameterD;

    @JsonView(View.Summary.class)
    @JsonProperty("minStemDiameterE")
    @Column(name = "minStemDiameterE")
    private Double minStemDiameterE;

    @JsonView(View.Summary.class)
    @JsonProperty("maxStemDiameterE")
    @Column(name = "maxStemDiameterE")
    private Double maxStemDiameterE;

    @JsonView(View.Summary.class)
    @JsonProperty("stemLengthF")
    @Column(name = "stemLengthF")
    private Double stemLengthF;

    @JsonView(View.Summary.class)
    @JsonProperty("stemLengthFTol")
    @Column(name = "stemLengthFTol")
    private Double stemLengthFTol;

    @JsonView(View.Summary.class)
    @JsonProperty("platformHeightH")
    @Column(name = "platformHeightH")
    private Double platformHeightH;

    @JsonView(View.Summary.class)
    @JsonProperty("bladeHeight")
    @Column(name = "bladeHeight")
    private Double bladeHeight;

    @JsonView(View.Summary.class)
    @JsonProperty("threadLengthG")
    @Column(name = "threadLengthG")
    private Double threadLengthG;

    @JsonView(View.Summary.class)
    @JsonProperty("thread")
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "thread")
    private CriticalDimensionEnumVal thread;

    @JsonView(View.Summary.class)
    @JsonProperty("threadHand")
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "threadHand")
    private CriticalDimensionEnumVal threadHand;

    @JsonView(View.Summary.class)
    @JsonProperty("pistonRingGrooveMajorDiameterI")
    @Column(name = "pistonRingGrooveMajorDiameterI")
    private Double pistonRingGrooveMajorDiameterI;

    @JsonView(View.Summary.class)
    @JsonProperty("pistonRingGrooveMajorDiaITol")
    @Column(name = "pistonRingGrooveMajorDiaITol")
    private Double pistonRingGrooveMajorDiaITol;

    @JsonView(View.Summary.class)
    @JsonProperty("pistonRingGrooveMinorDiameterJ")
    @Column(name = "pistonRingGrooveMinorDiameterJ")
    private Double pistonRingGrooveMinorDiameterJ;

    @JsonView(View.Summary.class)
    @JsonProperty("pistonRingGrooveMinorJDiaTol")
    @Column(name = "pistonRingGrooveMinorJDiaTol")
    private Double pistonRingGrooveMinorJDiaTol;

    @JsonView(View.Summary.class)
    @JsonProperty("pistonRingGrooveWidthK")
    @Column(name = "pistonRingGrooveWidthK")
    private Double pistonRingGrooveWidthK;

    @JsonView(View.Summary.class)
    @JsonProperty("pistonRingGrooveWidthKTol")
    @Column(name = "pistonRingGrooveWidthKTol")
    private Double pistonRingGrooveWidthKTol;

    @JsonView(View.Summary.class)
    @JsonProperty("the2ndPistonRingGrooveMajorDia")
    @Column(name = "the2ndPistonRingGrooveMajorDia")
    private Double the2ndPistonRingGrooveMajorDia;

    @JsonView(View.Summary.class)
    @JsonProperty("the2ndPistonRingGrooveMajorDiaTol")
    @Column(name = "the2ndPistonRingGrooveMajorDiaTol")
    private Double the2ndPistonRingGrooveMajorDiaTol;

    @JsonView(View.Summary.class)
    @JsonProperty("the2ndPistonRingGrooveMinorDia")
    @Column(name = "the2ndPistonRingGrooveMinorDia")
    private Double the2ndPistonRingGrooveMinorDia;

    @JsonView(View.Summary.class)
    @JsonProperty("the2ndPistonRingGrooveMinorDiaTol")
    @Column(name = "the2ndPistonRingGrooveMinorDiaTol")
    private Double the2ndPistonRingGrooveMinorDiaTol;

    @JsonView(View.Summary.class)
    @JsonProperty("the2ndPistonRingGrooveWidth")
    @Column(name = "the2ndPistonRingGrooveWidth")
    private Double the2ndPistonRingGrooveWidth;

    @JsonView(View.Summary.class)
    @JsonProperty("the2ndPistonRingGrooveWidthTol")
    @Column(name = "the2ndPistonRingGrooveWidthTol")
    private Double the2ndPistonRingGrooveWidthTol;

    @JsonView(View.Summary.class)
    @JsonProperty("rotation")
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "rotation")
    private CriticalDimensionEnumVal rotation;

    @JsonView(View.Summary.class)
    @JsonProperty("bladeCount")
    @Column(name = "bladeCount")
    private Integer bladeCount;

    @JsonView(View.Summary.class)
    @JsonProperty("shroudType")
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "shroudType")
    private CriticalDimensionEnumVal shroudType;

    @JsonView(View.Summary.class)
    @JsonProperty("journalType")
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "journalType")
    private CriticalDimensionEnumVal journalType;

    @JsonView(View.Summary.class)
    @JsonProperty("extendedTips")
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "extendedTips")
    private CriticalDimensionEnumVal extendedTips;

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

    public Double getInducerDiameterA() {
        return inducerDiameterA;
    }

    public void setInducerDiameterA(Double inducerDiameterA) {
        this.inducerDiameterA = inducerDiameterA;
    }

    public Double getInducerDiameterATol() {
        return inducerDiameterATol;
    }

    public void setInducerDiameterATol(Double inducerDiameterATol) {
        this.inducerDiameterATol = inducerDiameterATol;
    }

    public Double getExducerDiameterB() {
        return exducerDiameterB;
    }

    public void setExducerDiameterB(Double exducerDiameterB) {
        this.exducerDiameterB = exducerDiameterB;
    }

    public Double getExducerDiameterBTol() {
        return exducerDiameterBTol;
    }

    public void setExducerDiameterBTol(Double exducerDiameterBTol) {
        this.exducerDiameterBTol = exducerDiameterBTol;
    }

    public Double getTipHeightC() {
        return tipHeightC;
    }

    public void setTipHeightC(Double tipHeightC) {
        this.tipHeightC = tipHeightC;
    }

    public Double getTipHeightCTol() {
        return tipHeightCTol;
    }

    public void setTipHeightCTol(Double tipHeightCTol) {
        this.tipHeightCTol = tipHeightCTol;
    }

    public Double getMaxJournalDiameterD() {
        return maxJournalDiameterD;
    }

    public void setMaxJournalDiameterD(Double maxJournalDiameterD) {
        this.maxJournalDiameterD = maxJournalDiameterD;
    }

    public Double getMinJournalDiameterD() {
        return minJournalDiameterD;
    }

    public void setMinJournalDiameterD(Double minJournalDiameterD) {
        this.minJournalDiameterD = minJournalDiameterD;
    }

    public Double getMinStemDiameterE() {
        return minStemDiameterE;
    }

    public void setMinStemDiameterE(Double minStemDiameterE) {
        this.minStemDiameterE = minStemDiameterE;
    }

    public Double getMaxStemDiameterE() {
        return maxStemDiameterE;
    }

    public void setMaxStemDiameterE(Double maxStemDiameterE) {
        this.maxStemDiameterE = maxStemDiameterE;
    }

    public Double getStemLengthF() {
        return stemLengthF;
    }

    public void setStemLengthF(Double stemLengthF) {
        this.stemLengthF = stemLengthF;
    }

    public Double getStemLengthFTol() {
        return stemLengthFTol;
    }

    public void setStemLengthFTol(Double stemLengthFTol) {
        this.stemLengthFTol = stemLengthFTol;
    }

    public Double getPlatformHeightH() {
        return platformHeightH;
    }

    public void setPlatformHeightH(Double platformHeightH) {
        this.platformHeightH = platformHeightH;
    }

    public Double getBladeHeight() {
        return bladeHeight;
    }

    public void setBladeHeight(Double bladeHeight) {
        this.bladeHeight = bladeHeight;
    }

    public Double getThreadLengthG() {
        return threadLengthG;
    }

    public void setThreadLengthG(Double threadLengthG) {
        this.threadLengthG = threadLengthG;
    }

    public CriticalDimensionEnumVal getThread() {
        return thread;
    }

    public void setThread(CriticalDimensionEnumVal thread) {
        this.thread = thread;
    }

    public CriticalDimensionEnumVal getThreadHand() {
        return threadHand;
    }

    public void setThreadHand(CriticalDimensionEnumVal threadHand) {
        this.threadHand = threadHand;
    }

    public Double getPistonRingGrooveMajorDiameterI() {
        return pistonRingGrooveMajorDiameterI;
    }

    public void setPistonRingGrooveMajorDiameterI(Double pistonRingGrooveMajorDiameterI) {
        this.pistonRingGrooveMajorDiameterI = pistonRingGrooveMajorDiameterI;
    }

    public Double getPistonRingGrooveMajorDiaITol() {
        return pistonRingGrooveMajorDiaITol;
    }

    public void setPistonRingGrooveMajorDiaITol(Double pistonRingGrooveMajorDiaITol) {
        this.pistonRingGrooveMajorDiaITol = pistonRingGrooveMajorDiaITol;
    }

    public Double getPistonRingGrooveMinorDiameterJ() {
        return pistonRingGrooveMinorDiameterJ;
    }

    public void setPistonRingGrooveMinorDiameterJ(Double pistonRingGrooveMinorDiameterJ) {
        this.pistonRingGrooveMinorDiameterJ = pistonRingGrooveMinorDiameterJ;
    }

    public Double getPistonRingGrooveMinorJDiaTol() {
        return pistonRingGrooveMinorJDiaTol;
    }

    public void setPistonRingGrooveMinorJDiaTol(Double pistonRingGrooveMinorJDiaTol) {
        this.pistonRingGrooveMinorJDiaTol = pistonRingGrooveMinorJDiaTol;
    }

    public Double getPistonRingGrooveWidthK() {
        return pistonRingGrooveWidthK;
    }

    public void setPistonRingGrooveWidthK(Double pistonRingGrooveWidthK) {
        this.pistonRingGrooveWidthK = pistonRingGrooveWidthK;
    }

    public Double getPistonRingGrooveWidthKTol() {
        return pistonRingGrooveWidthKTol;
    }

    public void setPistonRingGrooveWidthKTol(Double pistonRingGrooveWidthKTol) {
        this.pistonRingGrooveWidthKTol = pistonRingGrooveWidthKTol;
    }

    public Double getThe2ndPistonRingGrooveMajorDia() {
        return the2ndPistonRingGrooveMajorDia;
    }

    public void setThe2ndPistonRingGrooveMajorDia(Double the2ndPistonRingGrooveMajorDia) {
        this.the2ndPistonRingGrooveMajorDia = the2ndPistonRingGrooveMajorDia;
    }

    public Double getThe2ndPistonRingGrooveMajorDiaTol() {
        return the2ndPistonRingGrooveMajorDiaTol;
    }

    public void setThe2ndPistonRingGrooveMajorDiaTol(Double the2ndPistonRingGrooveMajorDiaTol) {
        this.the2ndPistonRingGrooveMajorDiaTol = the2ndPistonRingGrooveMajorDiaTol;
    }

    public Double getThe2ndPistonRingGrooveMinorDia() {
        return the2ndPistonRingGrooveMinorDia;
    }

    public void setThe2ndPistonRingGrooveMinorDia(Double the2ndPistonRingGrooveMinorDia) {
        this.the2ndPistonRingGrooveMinorDia = the2ndPistonRingGrooveMinorDia;
    }

    public Double getThe2ndPistonRingGrooveMinorDiaTol() {
        return the2ndPistonRingGrooveMinorDiaTol;
    }

    public void setThe2ndPistonRingGrooveMinorDiaTol(Double the2ndPistonRingGrooveMinorDiaTol) {
        this.the2ndPistonRingGrooveMinorDiaTol = the2ndPistonRingGrooveMinorDiaTol;
    }

    public Double getThe2ndPistonRingGrooveWidth() {
        return the2ndPistonRingGrooveWidth;
    }

    public void setThe2ndPistonRingGrooveWidth(Double the2ndPistonRingGrooveWidth) {
        this.the2ndPistonRingGrooveWidth = the2ndPistonRingGrooveWidth;
    }

    public Double getThe2ndPistonRingGrooveWidthTol() {
        return the2ndPistonRingGrooveWidthTol;
    }

    public void setThe2ndPistonRingGrooveWidthTol(Double the2ndPistonRingGrooveWidthTol) {
        this.the2ndPistonRingGrooveWidthTol = the2ndPistonRingGrooveWidthTol;
    }

    public CriticalDimensionEnumVal getRotation() {
        return rotation;
    }

    public void setRotation(CriticalDimensionEnumVal rotation) {
        this.rotation = rotation;
    }

    public Integer getBladeCount() {
        return bladeCount;
    }

    public void setBladeCount(Integer bladeCount) {
        this.bladeCount = bladeCount;
    }

    public CriticalDimensionEnumVal getShroudType() {
        return shroudType;
    }

    public void setShroudType(CriticalDimensionEnumVal shroudType) {
        this.shroudType = shroudType;
    }

    public CriticalDimensionEnumVal getJournalType() {
        return journalType;
    }

    public void setJournalType(CriticalDimensionEnumVal journalType) {
        this.journalType = journalType;
    }

    public CriticalDimensionEnumVal getExtendedTips() {
        return extendedTips;
    }

    public void setExtendedTips(CriticalDimensionEnumVal extendedTips) {
        this.extendedTips = extendedTips;
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
