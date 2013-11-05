package com.turbointernational.metadata.domain.part;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.PrimaryKeyJoinColumn;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(table="PISTON_RING", inheritanceType = "JOINED")
@DiscriminatorValue(value = "4")
@PrimaryKeyJoinColumn(name = "part_id")
public class PistonRing extends Part {
    
    @Column(name="outside_dim_min")
    private Float outsideDiameterMin;

    @Column(name="outside_dim_max")
    private Float outsideDiameterMax;

    @Column(name="width_min")
    private Float widthMin;

    @Column(name="width_max")
    private Float widthMax;

    @Column(name="i_gap_min")
    private Float installedGapMin;

    @Column(name="i_gap_max")
    private Float installedGapMax;
}
