package com.turbointernational.metadata.domain.part;
import javax.persistence.Column;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;


/**
 * http://www.turbointernational.com/products/majorCmptDetails.asp?mcid=454&turbotype=35
 * @author Jeff
 */
@RooJavaBean
@RooToString
@RooJpaActiveRecord(table="TURBINE_WHEEL", inheritanceType = "JOINED")
public class TurbineWheel extends Part {

    @Column(name="exduce_oa")
    private Float exducerDiameterA;

    private Float tipHeightB;

    @Column(name="inducer_oc")
    private Float inducerDiameterC;

    @Column(name="journal_od")
    private Float journalDiameterD;

    @Column(name="stem_oe")
    private Float stemDiameterE;

    @Column(name="shaft_thread_f")
    private String shaftThreadF;

    @Column(name="trim_no_blades")
    private String trimNoBlades;
}
