package com.turbointernational.metadata.domain.part.types;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.domain.criticaldimension.CriticalDimensionEnumVal;
import com.turbointernational.metadata.domain.part.Part;
import com.turbointernational.metadata.web.View;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;


/**
 * Created by dmytro.trunykov@zorallabs.com.
 */
@Entity
@Table(name = "thrust_collar")
@PrimaryKeyJoinColumn(name = "part_id")
public class ThrustCollar extends Part {

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
    public Double getAlengthacrossthrustbearing() {
        return aLengthAcrossThrustBearing;
    }

    public void setAlengthacrossthrustbearing(Double aLengthAcrossThrustBearing) {
        this.aLengthAcrossThrustBearing = aLengthAcrossThrustBearing;
    }

    public Double getAlengthacrossthrustbearingtol() {
        return aLengthAcrossThrustBearingTol;
    }

    public void setAlengthacrossthrustbearingtol(Double aLengthAcrossThrustBearingTol) {
        this.aLengthAcrossThrustBearingTol = aLengthAcrossThrustBearingTol;
    }

    public Double getBoal() {
        return bOal;
    }

    public void setBoal(Double bOal) {
        this.bOal = bOal;
    }

    public Double getBoaltol() {
        return bOalTol;
    }

    public void setBoaltol(Double bOalTol) {
        this.bOalTol = bOalTol;
    }

    public Double getCtethrustdia() {
        return cteThrustDia;
    }

    public void setCtethrustdia(Double cteThrustDia) {
        this.cteThrustDia = cteThrustDia;
    }

    public Double getCtethrustdiatol() {
        return cteThrustDiaTol;
    }

    public void setCtethrustdiatol(Double cteThrustDiaTol) {
        this.cteThrustDiaTol = cteThrustDiaTol;
    }

    public Double getDcethrustdia() {
        return dceThrustDia;
    }

    public void setDcethrustdia(Double dceThrustDia) {
        this.dceThrustDia = dceThrustDia;
    }

    public Double getDcethrustdiatol() {
        return dceThrustDiaTol;
    }

    public void setDcethrustdiatol(Double dceThrustDiaTol) {
        this.dceThrustDiaTol = dceThrustDiaTol;
    }

    public Double getEboredia() {
        return eBoreDia;
    }

    public void setEboredia(Double eBoreDia) {
        this.eBoreDia = eBoreDia;
    }

    public Double getEborediatol() {
        return eBoreDiaTol;
    }

    public void setEborediatol(Double eBoreDiaTol) {
        this.eBoreDiaTol = eBoreDiaTol;
    }

    public Double getFpistonringgroove1majordia() {
        return fPistonRingGroove1MajorDia;
    }

    public void setFpistonringgroove1majordia(Double fPistonRingGroove1MajorDia) {
        this.fPistonRingGroove1MajorDia = fPistonRingGroove1MajorDia;
    }

    public Double getFpistonringgroove1majordiatol() {
        return fPistonRingGroove1MajorDiaTol;
    }

    public void setFpistonringgroove1majordiatol(Double fPistonRingGroove1MajorDiaTol) {
        this.fPistonRingGroove1MajorDiaTol = fPistonRingGroove1MajorDiaTol;
    }

    public Double getGpistonringgroove1minordia() {
        return gPistonRingGroove1MinorDia;
    }

    public void setGpistonringgroove1minordia(Double gPistonRingGroove1MinorDia) {
        this.gPistonRingGroove1MinorDia = gPistonRingGroove1MinorDia;
    }

    public Double getGpistonringgroove1minordiatol() {
        return gPistonRingGroove1MinorDiaTol;
    }

    public void setGpistonringgroove1minordiatol(Double gPistonRingGroove1MinorDiaTol) {
        this.gPistonRingGroove1MinorDiaTol = gPistonRingGroove1MinorDiaTol;
    }

    public Double getHpistonringgroove1width() {
        return hPistonRingGroove1Width;
    }

    public void setHpistonringgroove1width(Double hPistonRingGroove1Width) {
        this.hPistonRingGroove1Width = hPistonRingGroove1Width;
    }

    public Double getHpistonringgroove1widthtol() {
        return hPistonRingGroove1WidthTol;
    }

    public void setHpistonringgroove1widthtol(Double hPistonRingGroove1WidthTol) {
        this.hPistonRingGroove1WidthTol = hPistonRingGroove1WidthTol;
    }

    public Double getIceflingerdia() {
        return iceFlingerDia;
    }

    public void setIceflingerdia(Double iceFlingerDia) {
        this.iceFlingerDia = iceFlingerDia;
    }

    public Double getJteflingerdia() {
        return jteFlingerDia;
    }

    public void setJteflingerdia(Double jteFlingerDia) {
        this.jteFlingerDia = jteFlingerDia;
    }

    public Double getKpistonringgroove2majordia() {
        return kPistonRingGroove2MajorDia;
    }

    public void setKpistonringgroove2majordia(Double kPistonRingGroove2MajorDia) {
        this.kPistonRingGroove2MajorDia = kPistonRingGroove2MajorDia;
    }

    public Double getKpistonringgroove2majordiatol() {
        return kPistonRingGroove2MajorDiaTol;
    }

    public void setKpistonringgroove2majordiatol(Double kPistonRingGroove2MajorDiaTol) {
        this.kPistonRingGroove2MajorDiaTol = kPistonRingGroove2MajorDiaTol;
    }

    public Double getLpistonringgroove2minordia() {
        return lPistonRingGroove2MinorDia;
    }

    public void setLpistonringgroove2minordia(Double lPistonRingGroove2MinorDia) {
        this.lPistonRingGroove2MinorDia = lPistonRingGroove2MinorDia;
    }

    public Double getLpistonringgroove2minordiatol() {
        return lPistonRingGroove2MinorDiaTol;
    }

    public void setLpistonringgroove2minordiatol(Double lPistonRingGroove2MinorDiaTol) {
        this.lPistonRingGroove2MinorDiaTol = lPistonRingGroove2MinorDiaTol;
    }

    public Double getMpistonringgroove2width() {
        return mPistonRingGroove2Width;
    }

    public void setMpistonringgroove2width(Double mPistonRingGroove2Width) {
        this.mPistonRingGroove2Width = mPistonRingGroove2Width;
    }

    public Double getMpistonringgroove2widthtol() {
        return mPistonRingGroove2WidthTol;
    }

    public void setMpistonringgroove2widthtol(Double mPistonRingGroove2WidthTol) {
        this.mPistonRingGroove2WidthTol = mPistonRingGroove2WidthTol;
    }

    public Integer getNumflingerholescerow() {
        return numFlingerHolesCeRow;
    }

    public void setNumflingerholescerow(Integer numFlingerHolesCeRow) {
        this.numFlingerHolesCeRow = numFlingerHolesCeRow;
    }

    public Double getDiaflingerholescerow() {
        return diaFlingerHolesCeRow;
    }

    public void setDiaflingerholescerow(Double diaFlingerHolesCeRow) {
        this.diaFlingerHolesCeRow = diaFlingerHolesCeRow;
    }

    public Integer getNumflingerholesterow() {
        return numFlingerHolesTeRow;
    }

    public void setNumflingerholesterow(Integer numFlingerHolesTeRow) {
        this.numFlingerHolesTeRow = numFlingerHolesTeRow;
    }

    public Double getDiaflingerholesterow() {
        return diaFlingerHolesTeRow;
    }

    public void setDiaflingerholesterow(Double diaFlingerHolesTeRow) {
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
