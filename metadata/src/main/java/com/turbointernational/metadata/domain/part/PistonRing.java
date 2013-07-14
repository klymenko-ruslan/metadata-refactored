package com.turbointernational.metadata.domain.part;
import javax.persistence.Column;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
@Inheritance(strategy=InheritanceType.JOINED)
public class PistonRing extends Part {
    @Column(name="outside_dim_min")
    private Float outsideDiameterMin;

    @Column(name="outside_dim_max")
    private Float outsideDiameterMax;

    private Float widthMin;

    private Float widthMax;

    // ???: Better names for these?
    @Column(name="i_gap_min")
    private Float iGapMin;

    @Column(name="i_gap_max")
    private Float iGapMax;
}
