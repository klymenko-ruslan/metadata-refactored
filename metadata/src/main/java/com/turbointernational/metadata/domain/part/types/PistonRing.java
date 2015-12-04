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
@Table(name="piston_ring")
@PrimaryKeyJoinColumn(name = "part_id")
public class PistonRing extends Part {
    
    @JsonView(View.Detail.class)
    @Column(name="outside_dim_min")
    private Float outsideDiameterMin;

    @JsonView(View.Detail.class)
    @Column(name="outside_dim_max")
    private Float outsideDiameterMax;

    @JsonView(View.Detail.class)
    @Column(name="width_min")
    private Float widthMin;

    @JsonView(View.Detail.class)
    @Column(name="width_max")
    private Float widthMax;

    @JsonView(View.Detail.class)
    @Column(name="i_gap_min")
    private Float installedGapMin;

    @JsonView(View.Detail.class)
    @Column(name="i_gap_max")
    private Float installedGapMax;

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

    public Float getWidthMin() {
        return widthMin;
    }

    public void setWidthMin(Float widthMin) {
        this.widthMin = widthMin;
    }

    public Float getWidthMax() {
        return widthMax;
    }

    public void setWidthMax(Float widthMax) {
        this.widthMax = widthMax;
    }

    public Float getInstalledGapMin() {
        return installedGapMin;
    }

    public void setInstalledGapMin(Float installedGapMin) {
        this.installedGapMin = installedGapMin;
    }

    public Float getInstalledGapMax() {
        return installedGapMax;
    }

    public void setInstalledGapMax(Float installedGapMax) {
        this.installedGapMax = installedGapMax;
    }
    
    @Override
    public void csvColumns(Map<String, String> columns) {
        super.csvColumns(columns);
        
        columns.put("outside_dim_min", ObjectUtils.toString(getOutsideDiameterMin()));
        columns.put("outside_dim_max", ObjectUtils.toString(getOutsideDiameterMax()));
        columns.put("width_min", ObjectUtils.toString(getWidthMin()));
        columns.put("width_max", ObjectUtils.toString(getWidthMax()));
        columns.put("i_gap_min", ObjectUtils.toString(getInstalledGapMin()));
        columns.put("i_gap_max", ObjectUtils.toString(getInstalledGapMax()));
    }
}
