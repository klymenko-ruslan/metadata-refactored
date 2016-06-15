package com.turbointernational.metadata.domain.part.types;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.domain.criticaldimension.CriticalDimensionEnumVal;
import com.turbointernational.metadata.domain.part.Part;
import com.turbointernational.metadata.web.View;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;


/**
 * Created by dmytro.trunykov@zorallabs.com on 2016-06-15 17:07:35.991519.
 */
@Entity
@Table(name = "carbon_seal")
@PrimaryKeyJoinColumn(name = "part_id")
public class CarbonSeal extends Part {

    //<editor-fold defaultstate="collapsed" desc="Properties: critical dimensions">

    @JsonView(View.Summary.class)
    @JsonProperty("encapsulated")
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "encapsulated")
    private CriticalDimensionEnumVal encapsulated;

    @JsonView(View.Summary.class)
    @JsonProperty("maxOd")
    @Column(name = "maxOd")
    private Double maxOd;

    @JsonView(View.Summary.class)
    @JsonProperty("minOd")
    @Column(name = "minOd")
    private Double minOd;

    @JsonView(View.Summary.class)
    @JsonProperty("maxId")
    @Column(name = "maxId")
    private Double maxId;

    @JsonView(View.Summary.class)
    @JsonProperty("minId")
    @Column(name = "minId")
    private Double minId;

    @JsonView(View.Summary.class)
    @JsonProperty("freeHeight")
    @Column(name = "freeHeight")
    private Double freeHeight;

    @JsonView(View.Summary.class)
    @JsonProperty("compressedHeight")
    @Column(name = "compressedHeight")
    private Double compressedHeight;

    @JsonView(View.Summary.class)
    @JsonProperty("maxOperHeight")
    @Column(name = "maxOperHeight")
    private Double maxOperHeight;

    @JsonView(View.Summary.class)
    @JsonProperty("minOperHeight")
    @Column(name = "minOperHeight")
    private Double minOperHeight;

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
    @JsonProperty("diameterD")
    @Column(name = "diameterD")
    private Double diameterD;

    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Getters and setters: critical dimensions">

    public CriticalDimensionEnumVal getEncapsulated() {
        return encapsulated;
    }

    public void setEncapsulated(CriticalDimensionEnumVal encapsulated) {
        this.encapsulated = encapsulated;
    }

    public Double getMaxOd() {
        return maxOd;
    }

    public void setMaxOd(Double maxOd) {
        this.maxOd = maxOd;
    }

    public Double getMinOd() {
        return minOd;
    }

    public void setMinOd(Double minOd) {
        this.minOd = minOd;
    }

    public Double getMaxId() {
        return maxId;
    }

    public void setMaxId(Double maxId) {
        this.maxId = maxId;
    }

    public Double getMinId() {
        return minId;
    }

    public void setMinId(Double minId) {
        this.minId = minId;
    }

    public Double getFreeHeight() {
        return freeHeight;
    }

    public void setFreeHeight(Double freeHeight) {
        this.freeHeight = freeHeight;
    }

    public Double getCompressedHeight() {
        return compressedHeight;
    }

    public void setCompressedHeight(Double compressedHeight) {
        this.compressedHeight = compressedHeight;
    }

    public Double getMaxOperHeight() {
        return maxOperHeight;
    }

    public void setMaxOperHeight(Double maxOperHeight) {
        this.maxOperHeight = maxOperHeight;
    }

    public Double getMinOperHeight() {
        return minOperHeight;
    }

    public void setMinOperHeight(Double minOperHeight) {
        this.minOperHeight = minOperHeight;
    }

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

    public Double getDiameterD() {
        return diameterD;
    }

    public void setDiameterD(Double diameterD) {
        this.diameterD = diameterD;
    }

    //</editor-fold>

}
