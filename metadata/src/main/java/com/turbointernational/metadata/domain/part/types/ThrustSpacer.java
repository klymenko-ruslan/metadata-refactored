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
    @ManyToOne(fetch = EAGER)
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
    public Double getAoal() {
        return aOal;
    }

    public void setAoal(Double aOal) {
        this.aOal = aOal;
    }

    public Double getAoaltol() {
        return aOalTol;
    }

    public void setAoaltol(Double aOalTol) {
        this.aOalTol = aOalTol;
    }

    public Double getBthrustdia() {
        return bThrustDia;
    }

    public void setBthrustdia(Double bThrustDia) {
        this.bThrustDia = bThrustDia;
    }

    public Double getBthrustdiatol() {
        return bThrustDiaTol;
    }

    public void setBthrustdiatol(Double bThrustDiaTol) {
        this.bThrustDiaTol = bThrustDiaTol;
    }

    public Double getCboredia() {
        return cBoreDia;
    }

    public void setCboredia(Double cBoreDia) {
        this.cBoreDia = cBoreDia;
    }

    public Double getCborediatol() {
        return cBoreDiaTol;
    }

    public void setCborediatol(Double cBoreDiaTol) {
        this.cBoreDiaTol = cBoreDiaTol;
    }

    public Double getDpistonringgroove1majordia() {
        return dPistonRingGroove1MajorDia;
    }

    public void setDpistonringgroove1majordia(Double dPistonRingGroove1MajorDia) {
        this.dPistonRingGroove1MajorDia = dPistonRingGroove1MajorDia;
    }

    public Double getDpistonringgroove1majordiatol() {
        return dPistonRingGroove1MajorDiaTol;
    }

    public void setDpistonringgroove1majordiatol(Double dPistonRingGroove1MajorDiaTol) {
        this.dPistonRingGroove1MajorDiaTol = dPistonRingGroove1MajorDiaTol;
    }

    public Double getEpistonringgroove1minordia() {
        return ePistonRingGroove1MinorDia;
    }

    public void setEpistonringgroove1minordia(Double ePistonRingGroove1MinorDia) {
        this.ePistonRingGroove1MinorDia = ePistonRingGroove1MinorDia;
    }

    public Double getEpistonringgroove1minordiatol() {
        return ePistonRingGroove1MinorDiaTol;
    }

    public void setEpistonringgroove1minordiatol(Double ePistonRingGroove1MinorDiaTol) {
        this.ePistonRingGroove1MinorDiaTol = ePistonRingGroove1MinorDiaTol;
    }

    public Double getFpistonringgroove1width() {
        return fPistonRingGroove1Width;
    }

    public void setFpistonringgroove1width(Double fPistonRingGroove1Width) {
        this.fPistonRingGroove1Width = fPistonRingGroove1Width;
    }

    public Double getFpistonringgroove1widthtol() {
        return fPistonRingGroove1WidthTol;
    }

    public void setFpistonringgroove1widthtol(Double fPistonRingGroove1WidthTol) {
        this.fPistonRingGroove1WidthTol = fPistonRingGroove1WidthTol;
    }

    public Double getGpistonringgroove2majordia() {
        return gPistonRingGroove2MajorDia;
    }

    public void setGpistonringgroove2majordia(Double gPistonRingGroove2MajorDia) {
        this.gPistonRingGroove2MajorDia = gPistonRingGroove2MajorDia;
    }

    public Double getGpistonringgroove2majordiatol() {
        return gPistonRingGroove2MajorDiaTol;
    }

    public void setGpistonringgroove2majordiatol(Double gPistonRingGroove2MajorDiaTol) {
        this.gPistonRingGroove2MajorDiaTol = gPistonRingGroove2MajorDiaTol;
    }

    public Double getHpistonringgroove2minordia() {
        return hPistonRingGroove2MinorDia;
    }

    public void setHpistonringgroove2minordia(Double hPistonRingGroove2MinorDia) {
        this.hPistonRingGroove2MinorDia = hPistonRingGroove2MinorDia;
    }

    public Double getHpistonringgroove2minordiatol() {
        return hPistonRingGroove2MinorDiaTol;
    }

    public void setHpistonringgroove2minordiatol(Double hPistonRingGroove2MinorDiaTol) {
        this.hPistonRingGroove2MinorDiaTol = hPistonRingGroove2MinorDiaTol;
    }

    public Double getIpistonringgroove2width() {
        return iPistonRingGroove2Width;
    }

    public void setIpistonringgroove2width(Double iPistonRingGroove2Width) {
        this.iPistonRingGroove2Width = iPistonRingGroove2Width;
    }

    public Double getIpistonringgroove2widthtol() {
        return iPistonRingGroove2WidthTol;
    }

    public void setIpistonringgroove2widthtol(Double iPistonRingGroove2WidthTol) {
        this.iPistonRingGroove2WidthTol = iPistonRingGroove2WidthTol;
    }

    public Double getJceflingerdia() {
        return jceFlingerDia;
    }

    public void setJceflingerdia(Double jceFlingerDia) {
        this.jceFlingerDia = jceFlingerDia;
    }

    public Double getKteflingerdia() {
        return kteFlingerDia;
    }

    public void setKteflingerdia(Double kteFlingerDia) {
        this.kteFlingerDia = kteFlingerDia;
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
