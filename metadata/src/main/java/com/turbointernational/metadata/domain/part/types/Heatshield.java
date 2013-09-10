package com.turbointernational.metadata.domain.part.types;
import com.turbointernational.metadata.domain.part.Part;
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
@SecondaryTable(name="heatshield", pkJoinColumns=@PrimaryKeyJoinColumn(name = "part_id"))
public class Heatshield extends Part {

    @Column(name="overall_diameter", table = "heatshield")
    private Float overallDiameter;

    @Column(name="inside_diameter", table = "heatshield")
    private Float insideDiameter;

    @Column(name="inducer_diameter", table = "heatshield")
    private Float inducerDiameter;

    public Float getOverallDiameter() {
        return overallDiameter;
    }

    public void setOverallDiameter(Float overallDiameter) {
        this.overallDiameter = overallDiameter;
    }

    public Float getInsideDiameter() {
        return insideDiameter;
    }

    public void setInsideDiameter(Float insideDiameter) {
        this.insideDiameter = insideDiameter;
    }

    public Float getInducerDiameter() {
        return inducerDiameter;
    }

    public void setInducerDiameter(Float inducerDiameter) {
        this.inducerDiameter = inducerDiameter;
    }

    @Override
    public JSOG toJsog() {
        JSOG partObject = super.toJsog();
        
        partObject.put("overall_diameter", getOverallDiameter());
        partObject.put("inside_diameter", getInsideDiameter());
        partObject.put("inducer_diameter", getInducerDiameter());
        
        return partObject;
    }

}
