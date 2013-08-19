package com.turbointernational.metadata.domain.part;
import javax.persistence.Column;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.SecondaryTable;
import net.sf.jsog.JSOG;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;

@RooJavaBean
@RooJpaActiveRecord
@RooJson
@SecondaryTable(name="HEATSHIELD", pkJoinColumns=@PrimaryKeyJoinColumn(name = "part_id"))
public class Heatshield extends Part {

    @Column(name="overall_diameter", table = "HEATSHIELD")
    private Float overallDiameter;

    @Column(name="inside_diameter", table = "HEATSHIELD")
    private Float insideDimater;

    @Column(name="inducer_diameter", table = "HEATSHIELD")
    private Float inducerDiameter;

    @Override
    public void addIndexFields(JSOG partObject) {
        partObject.put("overall_diameter", overallDiameter);
        partObject.put("inside_diameter", insideDimater);
        partObject.put("inducer_diameter", inducerDiameter);
    }

}
