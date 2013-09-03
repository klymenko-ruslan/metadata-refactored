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
        partObject.put("overall_diameter", getOverallDiameter());
        partObject.put("inside_diameter", getInsideDimater());
        partObject.put("inducer_diameter", getInducerDiameter());
    }

    public Float getOverallDiameter() {
        return overallDiameter;
    }

    public void setOverallDiameter(Float overallDiameter) {
        this.overallDiameter = overallDiameter;
    }

    public Float getInsideDimater() {
        return insideDimater;
    }

    public void setInsideDimater(Float insideDimater) {
        this.insideDimater = insideDimater;
    }

    public Float getInducerDiameter() {
        return inducerDiameter;
    }

    public void setInducerDiameter(Float inducerDiameter) {
        this.inducerDiameter = inducerDiameter;
    }

}
