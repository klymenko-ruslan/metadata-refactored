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
@Table(name="compressor_wheel")
@PrimaryKeyJoinColumn(name = "part_id")
public class CompressorWheel extends Part {
    @Column(name="inducer_oa")
    private Float inducerOa;

    @Column(name="tip_height_b")
    private Float tipHeightB;

    @Column(name="exducer_oc")
    private Float exducerOc;

    @Column(name="hub_length_d")
    private Float hubLengthD;

    @Column(name="bore_oe")
    private Float boreOe;

    @Column(name="trim_no_blades")
    private String numberOfBlades;

    @Column(name="application")
    private String application;

    public Float getInducerOa() {
        return inducerOa;
    }

    public void setInducerOa(Float inducerOa) {
        this.inducerOa = inducerOa;
    }

    public Float getTipHeightB() {
        return tipHeightB;
    }

    public void setTipHeightB(Float tipHeightB) {
        this.tipHeightB = tipHeightB;
    }

    public Float getExducerOc() {
        return exducerOc;
    }

    public void setExducerOc(Float exducerOc) {
        this.exducerOc = exducerOc;
    }

    public Float getHubLengthD() {
        return hubLengthD;
    }

    public void setHubLengthD(Float hubLengthD) {
        this.hubLengthD = hubLengthD;
    }

    public Float getBoreOe() {
        return boreOe;
    }

    public void setBoreOe(Float boreOe) {
        this.boreOe = boreOe;
    }

    public String getNumberOfBlades() {
        return numberOfBlades;
    }

    public void setNumberOfBlades(String numberOfBlades) {
        this.numberOfBlades = numberOfBlades;
    }

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }
    
    @Override
    public void csvColumns(Map<String, String> columns) {
        super.csvColumns(columns);
        
        columns.put("inducer_oa", ObjectUtils.toString(getInducerOa()));
        columns.put("tip_height_b", ObjectUtils.toString(getTipHeightB()));
        columns.put("exducer_oc", ObjectUtils.toString(getExducerOc()));
        columns.put("hub_length_d", ObjectUtils.toString(getHubLengthD()));
        columns.put("bore_oe", ObjectUtils.toString(getBoreOe()));
        columns.put("number_of_blades", ObjectUtils.toString(getNumberOfBlades()));
        columns.put("application", ObjectUtils.toString(getApplication()));
    }
}
