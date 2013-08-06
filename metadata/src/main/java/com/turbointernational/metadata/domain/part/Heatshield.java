package com.turbointernational.metadata.domain.part;
import javax.persistence.Column;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.SecondaryTable;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
@SecondaryTable(name="HEATSHIELD", pkJoinColumns=@PrimaryKeyJoinColumn(name = "part_id"))
public class Heatshield extends Part {

    @Column(name="overall_diameter", table = "HEATSHIELD")
    private Float overallDiameter;

    @Column(name="inside_diameter", table = "HEATSHIELD")
    private Float insideDimater;

    @Column(name="inducer_diameter", table = "HEATSHIELD")
    private Float inducerDiameter;

}
