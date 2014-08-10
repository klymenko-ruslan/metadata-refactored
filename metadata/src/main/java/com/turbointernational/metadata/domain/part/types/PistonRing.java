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
@Table(name="piston_ring")
@PrimaryKeyJoinColumn(name = "part_id")
public class PistonRing extends Part {
    
    @Column(name="outside_dim_min")
    private Float outsideDiameterMin;

    @Column(name="outside_dim_max")
    private Float outsideDiameterMax;

    @Column(name="width_min")
    private Float widthMin;

    @Column(name="width_max")
    private Float widthMax;

    @Column(name="i_gap_min")
    private Float installedGapMin;

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
