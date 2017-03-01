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
 * Created by dmytro.trunykov@zorallabs.com on 2016-08-01 17:06:08.761641.
 */
@Entity
@Table(name = "thrust_collar")
@DiscriminatorValue("44")
@PrimaryKeyJoinColumn(name = "part_id")
public class ThrustCollar extends Part {

    private static final long serialVersionUID = 1446503588574147294L;

    //<editor-fold defaultstate="collapsed" desc="Properties: critical dimensions">

    @JsonView(View.Summary.class)
    @JsonProperty("aLengthAcrossThrustBearing")
    @Column(name = "aLengthAcrossThrustBearing")
    private Double aLengthAcrossThrustBearing;

    @JsonView(View.Summary.class)
    @JsonProperty("aLengthAcrossThrustBearingTol")
    @Column(name = "aLengthAcrossThrustBearingTol")
    private Double aLengthAcrossThrustBearingTol;

    @JsonView(View.Summary.class)
    @JsonProperty("bOal")
    @Column(name = "bOal")
    private Double bOal;

    @JsonView(View.Summary.class)
    @JsonProperty("bOalTol")
    @Column(name = "bOalTol")
    private Double bOalTol;

    @JsonView(View.Summary.class)
    @JsonProperty("cteThrustDia")
    @Column(name = "cteThrustDia")
    private Double cteThrustDia;

    @JsonView(View.Summary.class)
    @JsonProperty("cteThrustDiaTol")
    @Column(name = "cteThrustDiaTol")
    private Double cteThrustDiaTol;

    @JsonView(View.Summary.class)
    @JsonProperty("dceThrustDia")
    @Column(name = "dceThrustDia")
    private Double dceThrustDia;

    @JsonView(View.Summary.class)
    @JsonProperty("dceThrustDiaTol")
    @Column(name = "dceThrustDiaTol")
    private Double dceThrustDiaTol;

    @JsonView(View.Summary.class)
    @JsonProperty("eBoreDia")
    @Column(name = "eBoreDia")
    private Double eBoreDia;

    @JsonView(View.Summary.class)
    @JsonProperty("eBoreDiaTol")
    @Column(name = "eBoreDiaTol")
    private Double eBoreDiaTol;

    @JsonView(View.Summary.class)
    @JsonProperty("fPistonRingGroove1MajorDia")
    @Column(name = "fPistonRingGroove1MajorDia")
    private Double fPistonRingGroove1MajorDia;

    @JsonView(View.Summary.class)
    @JsonProperty("fPistonRingGroove1MajorDiaTol")
    @Column(name = "fPistonRingGroove1MajorDiaTol")
    private Double fPistonRingGroove1MajorDiaTol;

    @JsonView(View.Summary.class)
    @JsonProperty("gPistonRingGroove1MinorDia")
    @Column(name = "gPistonRingGroove1MinorDia")
    private Double gPistonRingGroove1MinorDia;

    @JsonView(View.Summary.class)
    @JsonProperty("gPistonRingGroove1MinorDiaTol")
    @Column(name = "gPistonRingGroove1MinorDiaTol")
    private Double gPistonRingGroove1MinorDiaTol;

    @JsonView(View.Summary.class)
    @JsonProperty("hPistonRingGroove1Width")
    @Column(name = "hPistonRingGroove1Width")
    private Double hPistonRingGroove1Width;

    @JsonView(View.Summary.class)
    @JsonProperty("hPistonRingGroove1WidthTol")
    @Column(name = "hPistonRingGroove1WidthTol")
    private Double hPistonRingGroove1WidthTol;

    @JsonView(View.Summary.class)
    @JsonProperty("iceFlingerDia")
    @Column(name = "iceFlingerDia")
    private Double iceFlingerDia;

    @JsonView(View.Summary.class)
    @JsonProperty("jteFlingerDia")
    @Column(name = "jteFlingerDia")
    private Double jteFlingerDia;

    @JsonView(View.Summary.class)
    @JsonProperty("kPistonRingGroove2MajorDia")
    @Column(name = "kPistonRingGroove2MajorDia")
    private Double kPistonRingGroove2MajorDia;

    @JsonView(View.Summary.class)
    @JsonProperty("kPistonRingGroove2MajorDiaTol")
    @Column(name = "kPistonRingGroove2MajorDiaTol")
    private Double kPistonRingGroove2MajorDiaTol;

    @JsonView(View.Summary.class)
    @JsonProperty("lPistonRingGroove2MinorDia")
    @Column(name = "lPistonRingGroove2MinorDia")
    private Double lPistonRingGroove2MinorDia;

    @JsonView(View.Summary.class)
    @JsonProperty("lPistonRingGroove2MinorDiaTol")
    @Column(name = "lPistonRingGroove2MinorDiaTol")
    private Double lPistonRingGroove2MinorDiaTol;

    @JsonView(View.Summary.class)
    @JsonProperty("mPistonRingGroove2Width")
    @Column(name = "mPistonRingGroove2Width")
    private Double mPistonRingGroove2Width;

    @JsonView(View.Summary.class)
    @JsonProperty("mPistonRingGroove2WidthTol")
    @Column(name = "mPistonRingGroove2WidthTol")
    private Double mPistonRingGroove2WidthTol;

    @JsonView(View.Summary.class)
    @JsonProperty("numFlingerHolesCeRow")
    @Column(name = "numFlingerHolesCeRow")
    private Integer numFlingerHolesCeRow;

    @JsonView(View.Summary.class)
    @JsonProperty("diaFlingerHolesCeRow")
    @Column(name = "diaFlingerHolesCeRow")
    private Double diaFlingerHolesCeRow;

    @JsonView(View.Summary.class)
    @JsonProperty("numFlingerHolesTeRow")
    @Column(name = "numFlingerHolesTeRow")
    private Integer numFlingerHolesTeRow;

    @JsonView(View.Summary.class)
    @JsonProperty("diaFlingerHolesTeRow")
    @Column(name = "diaFlingerHolesTeRow")
    private Double diaFlingerHolesTeRow;

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

    public Double getALengthAcrossThrustBearing() {
        return aLengthAcrossThrustBearing;
    }

    public void setALengthAcrossThrustBearing(Double aLengthAcrossThrustBearing) {
        this.aLengthAcrossThrustBearing = aLengthAcrossThrustBearing;
    }

    public Double getALengthAcrossThrustBearingTol() {
        return aLengthAcrossThrustBearingTol;
    }

    public void setALengthAcrossThrustBearingTol(Double aLengthAcrossThrustBearingTol) {
        this.aLengthAcrossThrustBearingTol = aLengthAcrossThrustBearingTol;
    }

    public Double getBOal() {
        return bOal;
    }

    public void setBOal(Double bOal) {
        this.bOal = bOal;
    }

    public Double getBOalTol() {
        return bOalTol;
    }

    public void setBOalTol(Double bOalTol) {
        this.bOalTol = bOalTol;
    }

    public Double getCteThrustDia() {
        return cteThrustDia;
    }

    public void setCteThrustDia(Double cteThrustDia) {
        this.cteThrustDia = cteThrustDia;
    }

    public Double getCteThrustDiaTol() {
        return cteThrustDiaTol;
    }

    public void setCteThrustDiaTol(Double cteThrustDiaTol) {
        this.cteThrustDiaTol = cteThrustDiaTol;
    }

    public Double getDceThrustDia() {
        return dceThrustDia;
    }

    public void setDceThrustDia(Double dceThrustDia) {
        this.dceThrustDia = dceThrustDia;
    }

    public Double getDceThrustDiaTol() {
        return dceThrustDiaTol;
    }

    public void setDceThrustDiaTol(Double dceThrustDiaTol) {
        this.dceThrustDiaTol = dceThrustDiaTol;
    }

    public Double getEBoreDia() {
        return eBoreDia;
    }

    public void setEBoreDia(Double eBoreDia) {
        this.eBoreDia = eBoreDia;
    }

    public Double getEBoreDiaTol() {
        return eBoreDiaTol;
    }

    public void setEBoreDiaTol(Double eBoreDiaTol) {
        this.eBoreDiaTol = eBoreDiaTol;
    }

    public Double getFPistonRingGroove1MajorDia() {
        return fPistonRingGroove1MajorDia;
    }

    public void setFPistonRingGroove1MajorDia(Double fPistonRingGroove1MajorDia) {
        this.fPistonRingGroove1MajorDia = fPistonRingGroove1MajorDia;
    }

    public Double getFPistonRingGroove1MajorDiaTol() {
        return fPistonRingGroove1MajorDiaTol;
    }

    public void setFPistonRingGroove1MajorDiaTol(Double fPistonRingGroove1MajorDiaTol) {
        this.fPistonRingGroove1MajorDiaTol = fPistonRingGroove1MajorDiaTol;
    }

    public Double getGPistonRingGroove1MinorDia() {
        return gPistonRingGroove1MinorDia;
    }

    public void setGPistonRingGroove1MinorDia(Double gPistonRingGroove1MinorDia) {
        this.gPistonRingGroove1MinorDia = gPistonRingGroove1MinorDia;
    }

    public Double getGPistonRingGroove1MinorDiaTol() {
        return gPistonRingGroove1MinorDiaTol;
    }

    public void setGPistonRingGroove1MinorDiaTol(Double gPistonRingGroove1MinorDiaTol) {
        this.gPistonRingGroove1MinorDiaTol = gPistonRingGroove1MinorDiaTol;
    }

    public Double getHPistonRingGroove1Width() {
        return hPistonRingGroove1Width;
    }

    public void setHPistonRingGroove1Width(Double hPistonRingGroove1Width) {
        this.hPistonRingGroove1Width = hPistonRingGroove1Width;
    }

    public Double getHPistonRingGroove1WidthTol() {
        return hPistonRingGroove1WidthTol;
    }

    public void setHPistonRingGroove1WidthTol(Double hPistonRingGroove1WidthTol) {
        this.hPistonRingGroove1WidthTol = hPistonRingGroove1WidthTol;
    }

    public Double getIceFlingerDia() {
        return iceFlingerDia;
    }

    public void setIceFlingerDia(Double iceFlingerDia) {
        this.iceFlingerDia = iceFlingerDia;
    }

    public Double getJteFlingerDia() {
        return jteFlingerDia;
    }

    public void setJteFlingerDia(Double jteFlingerDia) {
        this.jteFlingerDia = jteFlingerDia;
    }

    public Double getKPistonRingGroove2MajorDia() {
        return kPistonRingGroove2MajorDia;
    }

    public void setKPistonRingGroove2MajorDia(Double kPistonRingGroove2MajorDia) {
        this.kPistonRingGroove2MajorDia = kPistonRingGroove2MajorDia;
    }

    public Double getKPistonRingGroove2MajorDiaTol() {
        return kPistonRingGroove2MajorDiaTol;
    }

    public void setKPistonRingGroove2MajorDiaTol(Double kPistonRingGroove2MajorDiaTol) {
        this.kPistonRingGroove2MajorDiaTol = kPistonRingGroove2MajorDiaTol;
    }

    public Double getLPistonRingGroove2MinorDia() {
        return lPistonRingGroove2MinorDia;
    }

    public void setLPistonRingGroove2MinorDia(Double lPistonRingGroove2MinorDia) {
        this.lPistonRingGroove2MinorDia = lPistonRingGroove2MinorDia;
    }

    public Double getLPistonRingGroove2MinorDiaTol() {
        return lPistonRingGroove2MinorDiaTol;
    }

    public void setLPistonRingGroove2MinorDiaTol(Double lPistonRingGroove2MinorDiaTol) {
        this.lPistonRingGroove2MinorDiaTol = lPistonRingGroove2MinorDiaTol;
    }

    public Double getMPistonRingGroove2Width() {
        return mPistonRingGroove2Width;
    }

    public void setMPistonRingGroove2Width(Double mPistonRingGroove2Width) {
        this.mPistonRingGroove2Width = mPistonRingGroove2Width;
    }

    public Double getMPistonRingGroove2WidthTol() {
        return mPistonRingGroove2WidthTol;
    }

    public void setMPistonRingGroove2WidthTol(Double mPistonRingGroove2WidthTol) {
        this.mPistonRingGroove2WidthTol = mPistonRingGroove2WidthTol;
    }

    public Integer getNumFlingerHolesCeRow() {
        return numFlingerHolesCeRow;
    }

    public void setNumFlingerHolesCeRow(Integer numFlingerHolesCeRow) {
        this.numFlingerHolesCeRow = numFlingerHolesCeRow;
    }

    public Double getDiaFlingerHolesCeRow() {
        return diaFlingerHolesCeRow;
    }

    public void setDiaFlingerHolesCeRow(Double diaFlingerHolesCeRow) {
        this.diaFlingerHolesCeRow = diaFlingerHolesCeRow;
    }

    public Integer getNumFlingerHolesTeRow() {
        return numFlingerHolesTeRow;
    }

    public void setNumFlingerHolesTeRow(Integer numFlingerHolesTeRow) {
        this.numFlingerHolesTeRow = numFlingerHolesTeRow;
    }

    public Double getDiaFlingerHolesTeRow() {
        return diaFlingerHolesTeRow;
    }

    public void setDiaFlingerHolesTeRow(Double diaFlingerHolesTeRow) {
        this.diaFlingerHolesTeRow = diaFlingerHolesTeRow;
    }

    public CriticalDimensionEnumVal getMaterial() {
        return material;
    }

    public void setMaterial(CriticalDimensionEnumVal material) {
        this.material = material;
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
