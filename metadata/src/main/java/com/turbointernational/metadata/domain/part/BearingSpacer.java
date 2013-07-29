package com.turbointernational.metadata.domain.part;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(table="BEARING_SPACER", inheritanceType = "JOINED")
@DiscriminatorValue(value = "7")
@PrimaryKeyJoinColumn(name = "part_id")
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
    
    @Column(name="outside_dim_min")
    private Float outsideDiameterMin;

    @Column(name="outside_dim_max")
    private Float outsideDiameterMax;
    
    @Column(name="inside_dim_min")
    private Float insideDiameterMin;
    
    @Column(name="inside_dim_max")
    private Float insideDiameterMax;

}
