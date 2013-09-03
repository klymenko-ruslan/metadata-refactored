package com.turbointernational.metadata.domain.part;
import javax.persistence.Column;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.SecondaryTable;
import net.sf.jsog.JSOG;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;

@RooJavaBean
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
        partObject.put("inducer_oa", inducerOa);
        partObject.put("tip_height_b", tipHeightB);
        partObject.put("exducer_oc", exducerOc);
        partObject.put("hub_length_d", hubLengthD);
        partObject.put("bore_oe", boreOe);
        partObject.put("number_of_blades", numberOfBlades);
        partObject.put("application", application);
    }
}
