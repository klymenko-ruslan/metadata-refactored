package com.turbointernational.metadata.domain.part.types;
import com.turbointernational.metadata.domain.part.Part;
import flexjson.JSONSerializer;
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

    @Column(name="outside_dim_min")
    private Float outsideDiameterMin;

    @Column(name="outside_dim_max")
    private Float outsideDiameterMax;

    @Column(name="inside_dim_min")
    private Float insideDiameterMin;

    @Column(name="inside_dim_max")
    private Float insideDiameterMax;

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
    public JSOG toJsog() {
        JSOG partObject = super.toJsog();

        partObject.put("outside_diameter_min", getOutsideDiameterMin());
        partObject.put("outside_diameter_max", getOutsideDiameterMax());
        partObject.put("inside_diameter_min", getInsideDiameterMin());
        partObject.put("inside_diameter_max", getInsideDiameterMax());
        
//        if (getStandardSize() != null) {
//            partObject.put("standard_size_id", getStandardSize().getId());
//        }
        
//        if (getOversize() != null) {
//            partObject.put("oversize_id", getOversize().getId());
//        }
        
        return partObject;
    }
    
    @Override
    public void csvColumns(Map<String, String> columns) {
        super.csvColumns(columns);
        
        columns.put("outside_diameter_min", ObjectUtils.toString(getOutsideDiameterMin()));
        columns.put("outside_diameter_max", ObjectUtils.toString(getOutsideDiameterMax()));
        columns.put("inside_diameter_min", ObjectUtils.toString(getInsideDiameterMin()));
        columns.put("inside_diameter_max", ObjectUtils.toString(getInsideDiameterMax()));

//        if (getStandardSize() != null) {
//            columns.put("standard_size_id", ObjectUtils.toString(getStandardSize().getId()));
//        }
//
//        if (getOversize() != null) {
//            columns.put("oversize_id", ObjectUtils.toString(getOversize().getId()));
//        }
    }
}
