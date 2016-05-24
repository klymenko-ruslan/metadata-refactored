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

    public Double getMaxod() {
        return maxOd;
    }

    public void setMaxod(Double maxOd) {
        this.maxOd = maxOd;
    }

    public Double getMinod() {
        return minOd;
    }

    public void setMinod(Double minOd) {
        this.minOd = minOd;
    }

    public Double getMaxid() {
        return maxId;
    }

    public void setMaxid(Double maxId) {
        this.maxId = maxId;
    }

    public Double getMinid() {
        return minId;
    }

    public void setMinid(Double minId) {
        this.minId = minId;
    }

    public Double getFreeheight() {
        return freeHeight;
    }

    public void setFreeheight(Double freeHeight) {
        this.freeHeight = freeHeight;
    }

    public Double getCompressedheight() {
        return compressedHeight;
    }

    public void setCompressedheight(Double compressedHeight) {
        this.compressedHeight = compressedHeight;
    }

    public Double getMaxoperheight() {
        return maxOperHeight;
    }

    public void setMaxoperheight(Double maxOperHeight) {
        this.maxOperHeight = maxOperHeight;
    }

    public Double getMinoperheight() {
        return minOperHeight;
    }

    public void setMinoperheight(Double minOperHeight) {
        this.minOperHeight = minOperHeight;
    }

    public Double getDiametera() {
        return diameterA;
    }

    public void setDiametera(Double diameterA) {
        this.diameterA = diameterA;
    }

    public Double getDiameterb() {
        return diameterB;
    }

    public void setDiameterb(Double diameterB) {
        this.diameterB = diameterB;
    }

    public Double getDiameterc() {
        return diameterC;
    }

    public void setDiameterc(Double diameterC) {
        this.diameterC = diameterC;
    }

    public Double getDiameterd() {
        return diameterD;
    }

    public void setDiameterd(Double diameterD) {
        this.diameterD = diameterD;
    }


    //</editor-fold>
}
