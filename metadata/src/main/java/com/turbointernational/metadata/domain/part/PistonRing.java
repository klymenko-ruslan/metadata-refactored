package com.turbointernational.metadata.domain.part;
import javax.persistence.Column;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.SecondaryTable;
import net.sf.jsog.JSOG;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;

@RooJavaBean
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
    public void toJson(JSOG partObject) {
        partObject.put("outside_dim_min", outsideDiameterMin);
        partObject.put("outside_dim_max", outsideDiameterMax);
        partObject.put("width_min", widthMin);
        partObject.put("width_max", widthMax);
        partObject.put("i_gap_min", installedGapMin);
        partObject.put("i_gap_max", installedGapMax);
    }
}
