package com.turbointernational.metadata.domain.part.types;
import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.domain.part.Part;
import com.turbointernational.metadata.web.View;
import java.util.Map;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import org.apache.commons.lang.ObjectUtils;

@Entity
@Table(name="heatshield")
@PrimaryKeyJoinColumn(name = "part_id")
public class Heatshield extends Part {
    
    @JsonView(View.Detail.class)
    @Column(name="overall_diameter")
    private Float overallDiameter;

    @JsonView(View.Detail.class)
    @Column(name="inside_diameter")
    private Float insideDiameter;

    @JsonView(View.Detail.class)
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
    public void csvColumns(Map<String, String> columns) {
        super.csvColumns(columns);
        
        columns.put("overall_diameter", ObjectUtils.toString(getOverallDiameter()));
        columns.put("inside_diameter", ObjectUtils.toString(getInsideDiameter()));
        columns.put("inducer_diameter", ObjectUtils.toString(getInducerDiameter()));
    }
}
