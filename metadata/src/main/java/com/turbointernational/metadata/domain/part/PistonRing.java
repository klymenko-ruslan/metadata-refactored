package com.turbointernational.metadata.domain.part;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.SecondaryTable;
import net.sf.jsog.JSOG;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;

@Configurable
@Entity
@RooJpaActiveRecord
@SecondaryTable(name="PISTON_RING", pkJoinColumns=@PrimaryKeyJoinColumn(name = "part_id"))
public class PistonRing extends Part {
    
    @Column(name="outside_dim_min", table = "PISTON_RING")
    private Float outsideDiameterMin;

    @Column(name="outside_dim_max", table = "PISTON_RING")
    private Float outsideDiameterMax;

    @Column(name="width_min", table = "PISTON_RING")
    private Float widthMin;

    @Column(name="width_max", table = "PISTON_RING")
    private Float widthMax;

    @Column(name="i_gap_min", table = "PISTON_RING")
    private Float installedGapMin;

    @Column(name="i_gap_max", table = "PISTON_RING")
    private Float installedGapMax;

    @Override
    public void addIndexFields(JSOG partObject) {
        partObject.put("outside_dim_min", getOutsideDiameterMin());
        partObject.put("outside_dim_max", getOutsideDiameterMax());
        partObject.put("width_min", getWidthMin());
        partObject.put("width_max", getWidthMax());
        partObject.put("i_gap_min", getInstalledGapMin());
        partObject.put("i_gap_max", getInstalledGapMax());
    }

    public Float getOutsideDiameterMin() {
        return outsideDiameterMin;
    }

    public void setOutsideDiameterMin(Float outsideDiameterMin) {
        this.outsideDiameterMin = outsideDiameterMin;
    }

    public Float getOutsideDiameterMax() {
        return outsideDiameterMax;
    }

    public void setOutsideDiameterMax(Float outsideDiameterMax) {
        this.outsideDiameterMax = outsideDiameterMax;
    }

    public Float getWidthMin() {
        return widthMin;
    }

    public void setWidthMin(Float widthMin) {
        this.widthMin = widthMin;
    }

    public Float getWidthMax() {
        return widthMax;
    }

    public void setWidthMax(Float widthMax) {
        this.widthMax = widthMax;
    }

    public Float getInstalledGapMin() {
        return installedGapMin;
    }

    public void setInstalledGapMin(Float installedGapMin) {
        this.installedGapMin = installedGapMin;
    }

    public Float getInstalledGapMax() {
        return installedGapMax;
    }

    public void setInstalledGapMax(Float installedGapMax) {
        this.installedGapMax = installedGapMax;
    }
}
