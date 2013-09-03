package com.turbointernational.metadata.domain.part;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.SecondaryTable;
import net.sf.jsog.JSOG;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;

@RooJavaBean
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
        if (standardSize != null) {
            partObject.put("standard_size_id", standardSize.getId());
        }
        if (oversize != null) {
            partObject.put("oversize_id", oversize.getId());
        }

        partObject.put("outside_diameter_min", outsideDiameterMin);
        partObject.put("outside_diameter_max", outsideDiameterMax);
        partObject.put("inside_diameter_min", insideDiameterMin);
        partObject.put("inside_diameter_max", insideDiameterMax);
    }

}
