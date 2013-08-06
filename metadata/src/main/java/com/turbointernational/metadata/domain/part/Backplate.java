package com.turbointernational.metadata.domain.part;
import com.turbointernational.metadata.domain.type.SealType;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.SecondaryTable;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
@SecondaryTable(name="BACKPLATE", pkJoinColumns=@PrimaryKeyJoinColumn(name = "part_id"))
public class Backplate extends Part {

    @ManyToOne
    @JoinColumn(name="seal_type_id", table = "BACKPLATE")
    private SealType sealType;

    @Column(name="style_compressor_wheel", table = "BACKPLATE")
    private String styleCompressorWheel;

    // ???: How is this different from the seal type's name?
    @Column(name="seal_type", table = "BACKPLATE")
    private String sealTypeString;

    @Column(name="overall_diameter", table = "BACKPLATE")
    private Float overallDiameter;

    @Column(name="compressor_wheel_diameter", table = "BACKPLATE")
    private Float compressorWheelDiameter;

    @Column(name="piston_ring_diameter", table = "BACKPLATE")
    private Float pistonRingDiameter;

    @Column(name="compressor_housing_diameter", table = "BACKPLATE")
    private Float compressorHousingDiameter;

    @Column(name="notes", table = "BACKPLATE")
    private String notes;

    @Column(name="secondary_diameter", table = "BACKPLATE")
    private Float secondaryDiameter;

    @Column(name="overall_height", table = "BACKPLATE")
    private Float overallHeight;

}
