package com.turbointernational.metadata.domain.part;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.SecondaryTable;
import net.sf.jsog.JSOG;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;

@Configurable
@Entity
@RooJpaActiveRecord
@SecondaryTable(name="COMPRESSOR_WHEEL", pkJoinColumns=@PrimaryKeyJoinColumn(name = "part_id"))
public class CompressorWheel extends Part {
    @Column(name="inducer_oa", table = "COMPRESSOR_WHEEL")
    private Float inducerOa;

    @Column(name="tip_height_b", table = "COMPRESSOR_WHEEL")
    private Float tipHeightB;

    @Column(name="exducer_oc", table = "COMPRESSOR_WHEEL")
    private Float exducerOc;

    @Column(name="hub_length_d", table = "COMPRESSOR_WHEEL")
    private Float hubLengthD;

    @Column(name="bore_oe", table = "COMPRESSOR_WHEEL")
    private Float boreOe;

    @Column(name="trim_no_blades", table = "COMPRESSOR_WHEEL")
    private String numberOfBlades;

    @Column(name="application", table = "COMPRESSOR_WHEEL")
    private String application;

    @Override
    public void addIndexFields(JSOG partObject) {
        partObject.put("inducer_oa", getInducerOa());
        partObject.put("tip_height_b", getTipHeightB());
        partObject.put("exducer_oc", getExducerOc());
        partObject.put("hub_length_d", getHubLengthD());
        partObject.put("bore_oe", getBoreOe());
        partObject.put("number_of_blades", getNumberOfBlades());
        partObject.put("application", getApplication());
    }

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
}
