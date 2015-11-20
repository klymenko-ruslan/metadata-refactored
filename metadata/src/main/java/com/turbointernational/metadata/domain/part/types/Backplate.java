package com.turbointernational.metadata.domain.part.types;

import com.turbointernational.metadata.domain.part.Part;
import com.turbointernational.metadata.domain.type.SealType;
import flexjson.JSONSerializer;
import org.apache.commons.lang.ObjectUtils;
import org.springframework.beans.factory.annotation.Configurable;

import javax.persistence.*;
import java.util.Map;

@Configurable
@Entity
@Table(name="backplate")
@PrimaryKeyJoinColumn(name = "part_id")
public class Backplate extends Part {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="seal_type_id")
    private SealType sealType;

    @Column(name="style_compressor_wheel")
    private String styleCompressorWheel;

    // ???: How is this different from the seal type's name?
    @Column(name="seal_type")
    private String sealTypeString;

    @Column(name="overall_diameter")
    private Float overallDiameter;

    @Column(name="compressor_wheel_diameter")
    private Float compressorWheelDiameter;

    @Column(name="piston_ring_diameter")
    private Float pistonRingDiameter;

    @Column(name="compressor_housing_diameter")
    private Float compressorHousingDiameter;

    @Column(name="notes")
    private String notes;

    @Column(name="secondary_diameter")
    private Float secondaryDiameter;

    @Column(name="overall_height")
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
    protected JSONSerializer getSearchSerializer() {
        return super.getSearchSerializer()
                    .include("sealType.id")
                    .include("sealType.name");
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
