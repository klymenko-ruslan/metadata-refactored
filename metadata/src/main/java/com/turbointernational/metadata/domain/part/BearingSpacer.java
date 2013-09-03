package com.turbointernational.metadata.domain.part;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.SecondaryTable;
import net.sf.jsog.JSOG;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;

@Configurable
@Entity
@RooJpaActiveRecord
@SecondaryTable(name="BEARING_SPACER", pkJoinColumns=@PrimaryKeyJoinColumn(name = "part_id"))
public class BearingSpacer extends Part {

    @OneToOne
    @JoinTable(name="STANDARD_BEARING_SPACER",
               joinColumns=@JoinColumn(name="oversized_part_id"),
               inverseJoinColumns=@JoinColumn(name="standard_part_id"))
    private BearingSpacer standardSize;

    @OneToOne
    @JoinTable(name="STANDARD_BEARING_SPACER",
               joinColumns=@JoinColumn(name="standard_part_id"),
               inverseJoinColumns=@JoinColumn(name="oversized_part_id"))
    private BearingSpacer oversize;
    
    @Column(name="outside_dim_min", table = "BEARING_SPACER")
    private Float outsideDiameterMin;

    @Column(name="outside_dim_max", table = "BEARING_SPACER")
    private Float outsideDiameterMax;
    
    @Column(name="inside_dim_min", table = "BEARING_SPACER")
    private Float insideDiameterMin;
    
    @Column(name="inside_dim_max", table = "BEARING_SPACER")
    private Float insideDiameterMax;

    @Override
    public void addIndexFields(JSOG partObject) {
        if (getStandardSize() != null) {
            partObject.put("standard_size_id", getStandardSize().getId());
        }
        if (getOversize() != null) {
            partObject.put("oversize_id", getOversize().getId());
        }

        partObject.put("outside_diameter_min", getOutsideDiameterMin());
        partObject.put("outside_diameter_max", getOutsideDiameterMax());
        partObject.put("inside_diameter_min", getInsideDiameterMin());
        partObject.put("inside_diameter_max", getInsideDiameterMax());
    }

    public BearingSpacer getStandardSize() {
        return standardSize;
    }

    public void setStandardSize(BearingSpacer standardSize) {
        this.standardSize = standardSize;
    }

    public BearingSpacer getOversize() {
        return oversize;
    }

    public void setOversize(BearingSpacer oversize) {
        this.oversize = oversize;
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

    public Float getInsideDiameterMin() {
        return insideDiameterMin;
    }

    public void setInsideDiameterMin(Float insideDiameterMin) {
        this.insideDiameterMin = insideDiameterMin;
    }

    public Float getInsideDiameterMax() {
        return insideDiameterMax;
    }

    public void setInsideDiameterMax(Float insideDiameterMax) {
        this.insideDiameterMax = insideDiameterMax;
    }

}
