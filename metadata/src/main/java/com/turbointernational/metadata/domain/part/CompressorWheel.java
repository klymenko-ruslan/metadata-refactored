package com.turbointernational.metadata.domain.part;
import javax.persistence.Column;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.SecondaryTable;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
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
}
