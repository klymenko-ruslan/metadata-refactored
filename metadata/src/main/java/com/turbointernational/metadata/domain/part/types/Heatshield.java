package com.turbointernational.metadata.domain.part.types;
import com.turbointernational.metadata.domain.part.Part;
import java.util.Map;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import net.sf.jsog.JSOG;
import org.apache.commons.lang.ObjectUtils;
import org.springframework.beans.factory.annotation.Configurable;

@Configurable
@Entity
@Table(name="heatshield")
@PrimaryKeyJoinColumn(name = "part_id")
public class Heatshield extends Part {

    @Column(name="overall_diameter")
    private Float overallDiameter;

    @Column(name="inside_diameter")
    private Float insideDiameter;

    @Column(name="inducer_diameter")
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

    @Override
    public void csvColumns(Map<String, String> columns) {
        super.csvColumns(columns);
        
        columns.put("overall_diameter", ObjectUtils.toString(getOverallDiameter()));
        columns.put("inside_diameter", ObjectUtils.toString(getInsideDiameter()));
        columns.put("inducer_diameter", ObjectUtils.toString(getInducerDiameter()));
    }
}
