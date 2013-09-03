package com.turbointernational.metadata.domain.part;
import com.turbointernational.metadata.domain.type.SealType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.SecondaryTable;
import net.sf.jsog.JSOG;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;

@RooJavaBean
@RooJpaActiveRecord
@SecondaryTable(name="backplate", pkJoinColumns=@PrimaryKeyJoinColumn(name = "part_id"))
public class Backplate extends Part {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="seal_type_id", table = "backplate")
    private SealType sealType;

    @Column(name="style_compressor_wheel", table = "backplate")
    private String styleCompressorWheel;

    // ???: How is this different from the seal type's name?
    @Column(name="seal_type", table = "backplate")
    private String sealTypeString;

    @Column(name="overall_diameter", table = "backplate")
    private Float overallDiameter;

    @Column(name="compressor_wheel_diameter", table = "backplate")
    private Float compressorWheelDiameter;

    @Column(name="piston_ring_diameter", table = "backplate")
    private Float pistonRingDiameter;

    @Column(name="compressor_housing_diameter", table = "backplate")
    private Float compressorHousingDiameter;

    @Column(name="notes", table = "backplate")
    private String notes;

    @Column(name="secondary_diameter", table = "backplate")
    private Float secondaryDiameter;

    @Column(name="overall_height", table = "backplate")
    private Float overallHeight;

    @Override
    public void addIndexFields(JSOG partObject) {
        if (sealType != null) {
            partObject.put("seal_type_name", sealType.getName());
        }

        partObject.put("overall_diameter", overallDiameter);
        partObject.put("compressor_wheel_diameter", compressorWheelDiameter);
        partObject.put("piston_ring_diameter", pistonRingDiameter);
        partObject.put("compressor_housing_diameter", compressorHousingDiameter);
        partObject.put("secondary_diameter", secondaryDiameter);
        partObject.put("overall_height", overallHeight);
        partObject.put("style_compressor_wheel", styleCompressorWheel);
    }

}
