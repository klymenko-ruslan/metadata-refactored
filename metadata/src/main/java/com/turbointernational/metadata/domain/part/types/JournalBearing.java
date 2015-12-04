package com.turbointernational.metadata.domain.part.types;

import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.domain.part.Part;
import com.turbointernational.metadata.web.View;
import flexjson.JSONSerializer;
import org.apache.commons.lang.ObjectUtils;
import org.springframework.beans.factory.annotation.Configurable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import org.apache.commons.lang.ObjectUtils;

import java.util.Map;

@Entity
@Table(name="journal_bearing")
@PrimaryKeyJoinColumn(name = "part_id")
public class JournalBearing extends Part {

//    @OneToOne(fetch = FetchType.LAZY)
//    @JoinTable(name="standard_journal_bearing",
//               joinColumns=@JoinColumn(name="oversized_part_id"),
//               inverseJoinColumns=@JoinColumn(name="standard_part_id"))
//    private JournalBearing standardSize;
//
//    @OneToOne(fetch = FetchType.LAZY)
//    @JoinTable(name="standard_journal_bearing",
//               joinColumns=@JoinColumn(name="standard_part_id"),
//               inverseJoinColumns=@JoinColumn(name="oversized_part_id"))
//    private JournalBearing oversize;

    @JsonView(View.Detail.class)
    @Column(name="outside_dim_min")
    private Float outsideDiameterMin;

    @JsonView(View.Detail.class)
    @Column(name="outside_dim_max")
    private Float outsideDiameterMax;

    @JsonView(View.Detail.class)
    @Column(name="inside_dim_min")
    private Float insideDiameterMin;

    @JsonView(View.Detail.class)
    @Column(name="inside_dim_max")
    private Float insideDiameterMax;

    @JsonView(View.Detail.class)
    @Column(name="width")
    private Float width;

//    public JournalBearing getStandardSize() {
//        return standardSize;
//    }
//
//    public void setStandardSize(JournalBearing standardSize) {
//        this.standardSize = standardSize;
//    }
//
//    public JournalBearing getOversize() {
//        return oversize;
//    }
//
//    public void setOversize(JournalBearing oversize) {
//        this.oversize = oversize;
//    }

    public Float getOutsideDiameterMin() {
        return outsideDiameterMin;
    }

    public void setOutsideDiameterMin(Float outsideDiameterMin) {
        this.outsideDiameterMin = outsideDiameterMin;
    }

    public Float getOutsideDiameterMax() {
        return outsideDiameterMax;
    }

    public void setOutsideDiameterMax(Float outsideDiameterMax) {
        this.outsideDiameterMax = outsideDiameterMax;
    }

    public Float getInsideDiameterMin() {
        return insideDiameterMin;
    }

    public void setInsideDiameterMin(Float insideDiameterMin) {
        this.insideDiameterMin = insideDiameterMin;
    }

    public Float getInsideDiameterMax() {
        return insideDiameterMax;
    }

    public void setInsideDiameterMax(Float insideDiameterMax) {
        this.insideDiameterMax = insideDiameterMax;
    }

    public Float getWidth() {
        return width;
    }

    public void setWidth(Float width) {
        this.width = width;
    }
    
    @Override
    protected JSONSerializer buildJSONSerializer() {
        return super.buildJSONSerializer()
            .include("standardSize.id")
            .include("standardSize.manufacturer.id")
            .include("standardSize.manufacturer.name")
            .include("standardSize.manufacturerPartNumber")
            .include("standardSize.version")
            .include("oversize.id")
            .include("oversize.manufacturer.id")
            .include("oversize.manufacturer.name")
            .include("oversize.manufacturerPartNumber")
            .include("oversize.version");
    }
    
    @Override
    public void csvColumns(Map<String, String> columns) {
        super.csvColumns(columns);
        
        columns.put("width_jb", ObjectUtils.toString(getWidth()));
        columns.put("outside_diameter_min", ObjectUtils.toString(getOutsideDiameterMin()));
        columns.put("outside_diameter_max", ObjectUtils.toString(getOutsideDiameterMax()));
        columns.put("inside_diameter_min", ObjectUtils.toString(getInsideDiameterMin()));
        columns.put("inside_diameter_max", ObjectUtils.toString(getInsideDiameterMax()));
    }
}
