package com.turbointernational.metadata.domain.part;
import javax.persistence.Column;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(table="COMPRESSOR_WHEEL", inheritanceType = "JOINED")
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
}
