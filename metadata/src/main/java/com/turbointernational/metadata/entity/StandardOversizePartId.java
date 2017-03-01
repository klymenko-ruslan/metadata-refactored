package com.turbointernational.metadata.entity;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.entity.part.Part;
import com.turbointernational.metadata.util.View;

/**
 * Created by dmytro.trunykov@zorallabs.com on 2017-02-13.
 */
@Embeddable
public class StandardOversizePartId implements Serializable {

    //<editor-fold defaultstate="collapsed" desc="Properties">

    private static final long serialVersionUID = 3905359509087475623L;

    @ManyToOne
    @JoinColumn(name = "standard_part_id", nullable = false)
    @JsonView({View.Summary.class})
    private Part standard;

    @ManyToOne
    @JoinColumn(name = "oversize_part_id", nullable = false)
    @JsonView({View.Summary.class})
    private Part oversize;

    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Constructors">

    public StandardOversizePartId() {

    }

    public StandardOversizePartId(Part standard, Part oversize) {
        this.standard = standard;
        this.oversize = oversize;
    }

    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Getters and Setters">

    public Part getStandard() {
        return standard;
    }

    public void setStandard(Part standard) {
        this.standard = standard;
    }

    public Part getOversize() {
        return oversize;
    }

    public void setOversize(Part oversize) {
        this.oversize = oversize;
    }

    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="equals() and hashCode()">

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StandardOversizePartId that = (StandardOversizePartId) o;

        if (!standard.equals(that.standard)) return false;
        return oversize.equals(that.oversize);
    }

    @Override
    public int hashCode() {
        int result = standard.hashCode();
        result = 31 * result + oversize.hashCode();
        return result;
    }

    //</editor-fold>

}
