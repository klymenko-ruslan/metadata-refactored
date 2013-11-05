package com.turbointernational.metadata.domain.part;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.PrimaryKeyJoinColumn;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(table="HEATSHIELD", inheritanceType = "JOINED")
@DiscriminatorValue(value = "15")
@PrimaryKeyJoinColumn(name = "part_id")
public class Heatshield extends Part {

    @Column(name="overall_diameter")
    private Float overallDiameter;

    @Column(name="inside_diameter")
    private Float insideDimater;

    @Column(name="inducer_diameter")
    private Float inducerDiameter;

}
