package com.turbointernational.metadata.domain.part.types;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.domain.criticaldimension.CriticalDimensionEnumVal;
import com.turbointernational.metadata.domain.part.Part;
import com.turbointernational.metadata.web.View;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;


/**
 * Created by dmytro.trunykov@zorallabs.com on 2016-06-15 17:07:35.990930.
 */
@Entity
@Table(name = "thrust_spacer")
@PrimaryKeyJoinColumn(name = "part_id")
public class ThrustSpacer extends Part {

    //<editor-fold defaultstate="collapsed" desc="Properties: critical dimensions">

    @JsonView(View.Summary.class)
    @JsonProperty("aOal")
    @Column(name = "aOal")
    private Double aOal;

    @JsonView(View.Summary.class)
    @JsonProperty("aOalTol")
    @Column(name = "aOalTol")
    private Double aOalTol;

    @JsonView(View.Summary.class)
    @JsonProperty("bThrustDia")
    @Column(name = "bThrustDia")
    private Double bThrustDia;

    @JsonView(View.Summary.class)
    @JsonProperty("bThrustDiaTol")
    @Column(name = "bThrustDiaTol")
    private Double bThrustDiaTol;

    @JsonView(View.Summary.class)
    @JsonProperty("cBoreDia")
    @Column(name = "cBoreDia")
    private Double cBoreDia;

    @JsonView(View.Summary.class)
    @JsonProperty("cBoreDiaTol")
    @Column(name = "cBoreDiaTol")
    private Double cBoreDiaTol;

    @JsonView(View.Summary.class)
    @JsonProperty("dPistonRingGroove1MajorDia")
    @Column(name = "dPistonRingGroove1MajorDia")
    private Double dPistonRingGroove1MajorDia;

    @JsonView(View.Summary.class)
    @JsonProperty("dPistonRingGroove1MajorDiaTol")
    @Column(name = "dPistonRingGroove1MajorDiaTol")
    private Double dPistonRingGroove1MajorDiaTol;

    @JsonView(View.Summary.class)
    @JsonProperty("ePistonRingGroove1MinorDia")
    @Column(name = "ePistonRingGroove1MinorDia")
    private Double ePistonRingGroove1MinorDia;

    @JsonView(View.Summary.class)
    @JsonProperty("ePistonRingGroove1MinorDiaTol")
    @Column(name = "ePistonRingGroove1MinorDiaTol")
    private Double ePistonRingGroove1MinorDiaTol;

    @JsonView(View.Summary.class)
    @JsonProperty("fPistonRingGroove1Width")
    @Column(name = "fPistonRingGroove1Width")
    private Double fPistonRingGroove1Width;

    @JsonView(View.Summary.class)
    @JsonProperty("fPistonRingGroove1WidthTol")
    @Column(name = "fPistonRingGroove1WidthTol")
    private Double fPistonRingGroove1WidthTol;

    @JsonView(View.Summary.class)
    @JsonProperty("gPistonRingGroove2MajorDia")
    @Column(name = "gPistonRingGroove2MajorDia")
    private Double gPistonRingGroove2MajorDia;

    @JsonView(View.Summary.class)
    @JsonProperty("gPistonRingGroove2MajorDiaTol")
    @Column(name = "gPistonRingGroove2MajorDiaTol")
    private Double gPistonRingGroove2MajorDiaTol;

    @JsonView(View.Summary.class)
    @JsonProperty("hPistonRingGroove2MinorDia")
    @Column(name = "hPistonRingGroove2MinorDia")
    private Double hPistonRingGroove2MinorDia;

    @JsonView(View.Summary.class)
    @JsonProperty("hPistonRingGroove2MinorDiaTol")
    @Column(name = "hPistonRingGroove2MinorDiaTol")
    private Double hPistonRingGroove2MinorDiaTol;

    @JsonView(View.Summary.class)
    @JsonProperty("iPistonRingGroove2Width")
    @Column(name = "iPistonRingGroove2Width")
    private Double iPistonRingGroove2Width;

    @JsonView(View.Summary.class)
    @JsonProperty("iPistonRingGroove2WidthTol")
    @Column(name = "iPistonRingGroove2WidthTol")
    private Double iPistonRingGroove2WidthTol;

    @JsonView(View.Summary.class)
    @JsonProperty("jceFlingerDia")
    @Column(name = "jceFlingerDia")
    private Double jceFlingerDia;

    @JsonView(View.Summary.class)
    @JsonProperty("kteFlingerDia")
    @Column(name = "kteFlingerDia")
    private Double kteFlingerDia;

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

    public Double getAOal() {
        return aOal;
    }

    public void setAOal(Double aOal) {
        this.aOal = aOal;
    }

    public Double getAOalTol() {
        return aOalTol;
    }

    public void setAOalTol(Double aOalTol) {
        this.aOalTol = aOalTol;
    }

    public Double getBThrustDia() {
        return bThrustDia;
    }

    public void setBThrustDia(Double bThrustDia) {
        this.bThrustDia = bThrustDia;
    }

    public Double getBThrustDiaTol() {
        return bThrustDiaTol;
    }

    public void setBThrustDiaTol(Double bThrustDiaTol) {
        this.bThrustDiaTol = bThrustDiaTol;
    }

    public Double getCBoreDia() {
        return cBoreDia;
    }

    public void setCBoreDia(Double cBoreDia) {
        this.cBoreDia = cBoreDia;
    }

    public Double getCBoreDiaTol() {
        return cBoreDiaTol;
    }

    public void setCBoreDiaTol(Double cBoreDiaTol) {
        this.cBoreDiaTol = cBoreDiaTol;
    }

    public Double getDPistonRingGroove1MajorDia() {
        return dPistonRingGroove1MajorDia;
    }

    public void setDPistonRingGroove1MajorDia(Double dPistonRingGroove1MajorDia) {
        this.dPistonRingGroove1MajorDia = dPistonRingGroove1MajorDia;
    }

    public Double getDPistonRingGroove1MajorDiaTol() {
        return dPistonRingGroove1MajorDiaTol;
    }

    public void setDPistonRingGroove1MajorDiaTol(Double dPistonRingGroove1MajorDiaTol) {
        this.dPistonRingGroove1MajorDiaTol = dPistonRingGroove1MajorDiaTol;
    }

    public Double getEPistonRingGroove1MinorDia() {
        return ePistonRingGroove1MinorDia;
    }

    public void setEPistonRingGroove1MinorDia(Double ePistonRingGroove1MinorDia) {
        this.ePistonRingGroove1MinorDia = ePistonRingGroove1MinorDia;
    }

    public Double getEPistonRingGroove1MinorDiaTol() {
        return ePistonRingGroove1MinorDiaTol;
    }

    public void setEPistonRingGroove1MinorDiaTol(Double ePistonRingGroove1MinorDiaTol) {
        this.ePistonRingGroove1MinorDiaTol = ePistonRingGroove1MinorDiaTol;
    }

    public Double getFPistonRingGroove1Width() {
        return fPistonRingGroove1Width;
    }

    public void setFPistonRingGroove1Width(Double fPistonRingGroove1Width) {
        this.fPistonRingGroove1Width = fPistonRingGroove1Width;
    }

    public Double getFPistonRingGroove1WidthTol() {
        return fPistonRingGroove1WidthTol;
    }

    public void setFPistonRingGroove1WidthTol(Double fPistonRingGroove1WidthTol) {
        this.fPistonRingGroove1WidthTol = fPistonRingGroove1WidthTol;
    }

    public Double getGPistonRingGroove2MajorDia() {
        return gPistonRingGroove2MajorDia;
    }

    public void setGPistonRingGroove2MajorDia(Double gPistonRingGroove2MajorDia) {
        this.gPistonRingGroove2MajorDia = gPistonRingGroove2MajorDia;
    }

    public Double getGPistonRingGroove2MajorDiaTol() {
        return gPistonRingGroove2MajorDiaTol;
    }

    public void setGPistonRingGroove2MajorDiaTol(Double gPistonRingGroove2MajorDiaTol) {
        this.gPistonRingGroove2MajorDiaTol = gPistonRingGroove2MajorDiaTol;
    }

    public Double getHPistonRingGroove2MinorDia() {
        return hPistonRingGroove2MinorDia;
    }

    public void setHPistonRingGroove2MinorDia(Double hPistonRingGroove2MinorDia) {
        this.hPistonRingGroove2MinorDia = hPistonRingGroove2MinorDia;
    }

    public Double getHPistonRingGroove2MinorDiaTol() {
        return hPistonRingGroove2MinorDiaTol;
    }

    public void setHPistonRingGroove2MinorDiaTol(Double hPistonRingGroove2MinorDiaTol) {
        this.hPistonRingGroove2MinorDiaTol = hPistonRingGroove2MinorDiaTol;
    }

    public Double getIPistonRingGroove2Width() {
        return iPistonRingGroove2Width;
    }

    public void setIPistonRingGroove2Width(Double iPistonRingGroove2Width) {
        this.iPistonRingGroove2Width = iPistonRingGroove2Width;
    }

    public Double getIPistonRingGroove2WidthTol() {
        return iPistonRingGroove2WidthTol;
    }

    public void setIPistonRingGroove2WidthTol(Double iPistonRingGroove2WidthTol) {
        this.iPistonRingGroove2WidthTol = iPistonRingGroove2WidthTol;
    }

    public Double getJceFlingerDia() {
        return jceFlingerDia;
    }

    public void setJceFlingerDia(Double jceFlingerDia) {
        this.jceFlingerDia = jceFlingerDia;
    }

    public Double getKteFlingerDia() {
        return kteFlingerDia;
    }

    public void setKteFlingerDia(Double kteFlingerDia) {
        this.kteFlingerDia = kteFlingerDia;
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
