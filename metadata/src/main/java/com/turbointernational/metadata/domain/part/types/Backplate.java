package com.turbointernational.metadata.domain.part.types;
import com.turbointernational.metadata.domain.part.Part;
import com.turbointernational.metadata.domain.type.SealType;
import java.util.Map;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.SecondaryTable;
import net.sf.jsog.JSOG;
import org.apache.commons.lang.ObjectUtils;
import org.springframework.beans.factory.annotation.Configurable;

@Configurable
@Entity
@SecondaryTable(name="backplate", pkJoinColumns=@PrimaryKeyJoinColumn(name = "part_id"))
public class Backplate extends Part {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="seal_type_id", table = "backplate")
    private SealType sealType;

    @Column(name="style_compressor_wheel", table = "backplate")
    private String styleCompressorWheel;

    // ???: How is this different from the seal type's name?
    @Column(name="seal_type", table = "backplate")
    private String sealTypeString;

    @Column(name="overall_diameter", table = "backplate")
    private Float overallDiameter;

    @Column(name="compressor_wheel_diameter", table = "backplate")
    private Float compressorWheelDiameter;

    @Column(name="piston_ring_diameter", table = "backplate")
    private Float pistonRingDiameter;

    @Column(name="compressor_housing_diameter", table = "backplate")
    private Float compressorHousingDiameter;

    @Column(name="notes", table = "backplate")
    private String notes;

    @Column(name="secondary_diameter", table = "backplate")
    private Float secondaryDiameter;

    @Column(name="overall_height", table = "backplate")
    private Float overallHeight;

    public SealType getSealType() {
        return sealType;
    }

    public void setSealType(SealType sealType) {
        this.sealType = sealType;
    }

    public String getStyleCompressorWheel() {
        return styleCompressorWheel;
    }

    public void setStyleCompressorWheel(String styleCompressorWheel) {
        this.styleCompressorWheel = styleCompressorWheel;
    }

    public String getSealTypeString() {
        return sealTypeString;
    }

    public void setSealTypeString(String sealTypeString) {
        this.sealTypeString = sealTypeString;
    }

    public Float getOverallDiameter() {
        return overallDiameter;
    }

    public void setOverallDiameter(Float overallDiameter) {
        this.overallDiameter = overallDiameter;
    }

    public Float getCompressorWheelDiameter() {
        return compressorWheelDiameter;
    }

    public void setCompressorWheelDiameter(Float compressorWheelDiameter) {
        this.compressorWheelDiameter = compressorWheelDiameter;
    }

    public Float getPistonRingDiameter() {
        return pistonRingDiameter;
    }

    public void setPistonRingDiameter(Float pistonRingDiameter) {
        this.pistonRingDiameter = pistonRingDiameter;
    }

    public Float getCompressorHousingDiameter() {
        return compressorHousingDiameter;
    }

    public void setCompressorHousingDiameter(Float compressorHousingDiameter) {
        this.compressorHousingDiameter = compressorHousingDiameter;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Float getSecondaryDiameter() {
        return secondaryDiameter;
    }

    public void setSecondaryDiameter(Float secondaryDiameter) {
        this.secondaryDiameter = secondaryDiameter;
    }

    public Float getOverallHeight() {
        return overallHeight;
    }

    public void setOverallHeight(Float overallHeight) {
        this.overallHeight = overallHeight;
    }

    @Override
    public JSOG toJsog() {
        JSOG partObject = super.toJsog();
        
        if (getSealType() != null) {
            partObject.put("seal_type_name", getSealType().getName());
        }

        partObject.put("overall_diameter", getOverallDiameter());
        partObject.put("compressor_wheel_diameter", getCompressorWheelDiameter());
        partObject.put("piston_ring_diameter", getPistonRingDiameter());
        partObject.put("compressor_housing_diameter", getCompressorHousingDiameter());
        partObject.put("secondary_diameter", getSecondaryDiameter());
        partObject.put("overall_height", getOverallHeight());
        partObject.put("style_compressor_wheel", getStyleCompressorWheel());
        
        return partObject;
    }
    
    @Override
    public void csvColumns(Map<String, String> columns) {
        super.csvColumns(columns);
        
        if (getSealType() != null) {
            columns.put("seal_type", ObjectUtils.toString(getSealType().getName()));
        }

        columns.put("overall_diameter", ObjectUtils.toString(getOverallDiameter()));
        columns.put("compressor_wheel_diameter", ObjectUtils.toString(getCompressorWheelDiameter()));
        columns.put("piston_ring_diameter", ObjectUtils.toString(getPistonRingDiameter()));
        columns.put("compressor_housing_diameter", ObjectUtils.toString(getCompressorHousingDiameter()));
        columns.put("secondary_diameter", ObjectUtils.toString(getSecondaryDiameter()));
        columns.put("overall_height", ObjectUtils.toString(getOverallHeight()));
        columns.put("style_compressor_wheel", ObjectUtils.toString(getStyleCompressorWheel()));
    }

}
