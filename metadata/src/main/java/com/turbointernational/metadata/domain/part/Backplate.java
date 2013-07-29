package com.turbointernational.metadata.domain.part;
import com.turbointernational.metadata.domain.type.SealType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(table="BACKPLATE", inheritanceType = "JOINED")
@DiscriminatorValue(value = "14")
@PrimaryKeyJoinColumn(name = "part_id")
public class Backplate extends Part {

    @ManyToOne
    @JoinColumn(name="seal_type_id")
    private SealType sealType;

    @Column(name="style_compressor_wheel")
    private String styleCompressorWheel;

    // ???: How is this different from the seal type's name?
    @Column(name="seal_type")
    private String sealTypeString;

    @Column(name="overall_diameter")
    private Float overallDiameter;

    @Column(name="compressor_wheel_diameter")
    private Float compressorWheelDiameter;

    @Column(name="piston_ring_diameter")
    private Float pistonRingDiameter;

    @Column(name="compressor_housing_diameter")
    private Float compressorHousingDiameter;

    @Column(name="notes")
    private String notes;

    @Column(name="secondary_diameter")
    private Float secondaryDiameter;

    @Column(name="overall_height")
    private Float overallHeight;

}
