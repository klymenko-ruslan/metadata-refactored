package com.turbointernational.metadata.domain.part.types;
import com.turbointernational.metadata.domain.part.Part;
import java.util.Map;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.SecondaryTable;
import net.sf.jsog.JSOG;
import org.apache.commons.lang.ObjectUtils;
import org.springframework.beans.factory.annotation.Configurable;

@Configurable
@Entity
@SecondaryTable(name="compressor_wheel", pkJoinColumns=@PrimaryKeyJoinColumn(name = "part_id"))
public class CompressorWheel extends Part {
    @Column(name="inducer_oa", table = "compressor_wheel")
    private Float inducerOa;

    @Column(name="tip_height_b", table = "compressor_wheel")
    private Float tipHeightB;

    @Column(name="exducer_oc", table = "compressor_wheel")
    private Float exducerOc;

    @Column(name="hub_length_d", table = "compressor_wheel")
    private Float hubLengthD;

    @Column(name="bore_oe", table = "compressor_wheel")
    private Float boreOe;

    @Column(name="trim_no_blades", table = "compressor_wheel")
    private String numberOfBlades;

    @Column(name="application", table = "compressor_wheel")
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
    public JSOG toJsog() {
        JSOG partObject = super.toJsog();
        
        partObject.put("inducer_oa", getInducerOa());
        partObject.put("tip_height_b", getTipHeightB());
        partObject.put("exducer_oc", getExducerOc());
        partObject.put("hub_length_d", getHubLengthD());
        partObject.put("bore_oe", getBoreOe());
        partObject.put("number_of_blades", getNumberOfBlades());
        partObject.put("application", getApplication());
        
        return partObject;
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
