package com.turbointernational.metadata.domain.part;
import javax.persistence.Column;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.SecondaryTable;
import net.sf.jsog.JSOG;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;


/**
 * http://www.turbointernational.com/products/majorCmptDetails.asp?mcid=454&turbotype=35
 * @author Jeff
 */
@RooJavaBean
@RooJpaActiveRecord
@SecondaryTable(name="TURBINE_WHEEL", pkJoinColumns=@PrimaryKeyJoinColumn(name = "part_id"))
public class TurbineWheel extends Part {

    @Column(name="exduce_oa", table = "TURBINE_WHEEL")
    private Float exducerDiameterA;

    @Column(name="tip_height_b", table = "TURBINE_WHEEL")
    private Float tipHeightB;

    @Column(name="inducer_oc", table = "TURBINE_WHEEL")
    private Float inducerDiameterC;

    @Column(name="journal_od", table = "TURBINE_WHEEL")
    private Float journalDiameterD;

    @Column(name="stem_oe", table = "TURBINE_WHEEL")
    private Float stemDiameterE;

    @Column(name="shaft_thread_f", table = "TURBINE_WHEEL")
    private String shaftThreadF;

    @Column(name="trim_no_blades", table = "TURBINE_WHEEL")
    private String numberOfBlades;

    @Override
    public JSOG toJsog() {
        JSOG partObject = super.toJsog();
        
        partObject.put("exducer_oa", exducerDiameterA);
        partObject.put("tip_height_b", tipHeightB);
        partObject.put("inducer_oc", inducerDiameterC);
        partObject.put("journal_od", journalDiameterD);
        partObject.put("stem_oe", stemDiameterE);
        partObject.put("shaft_thread_f", shaftThreadF);
        partObject.put("number_of_blades", numberOfBlades);
        
        return partObject;
    }
}
