package com.turbointernational.metadata.domain.part;
import com.turbointernational.metadata.domain.type.SealType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
@Inheritance(strategy=InheritanceType.JOINED)
public class Backplate extends Part {

    @ManyToOne
    @JoinColumn(name="seal_type_id")
    private SealType sealType;
    
    private String styleCompressorWheel;

    // ???: How is this different from the seal type's name?
    private String sealTypeString;

    private Float overallDiameter;

    private Float compressorWheelDiameter;

    private Float pistonRingDiameter;

    private Float compressorHousingDIameter;

    private String notes;

    private Float secondaryDiameter;

    private Float overallHeight;

}
