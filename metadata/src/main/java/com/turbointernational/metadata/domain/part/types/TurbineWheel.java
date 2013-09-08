package com.turbointernational.metadata.domain.part.types;
import com.turbointernational.metadata.domain.part.Part;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.SecondaryTable;
import net.sf.jsog.JSOG;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;


/**
 * http://www.turbointernational.com/products/majorCmptDetails.asp?mcid=454&turbotype=35
 * @author Jeff
 */
@Configurable
@Entity
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
    public void addIndexFields(JSOG partObject) {
        partObject.put("exduce_oa", getExducerDiameterA());
        partObject.put("tip_height_b", getTipHeightB());
        partObject.put("inducer_oc", getInducerDiameterC());
        partObject.put("journal_od", getJournalDiameterD());
        partObject.put("stem_oe", getStemDiameterE());
        partObject.put("shaft_thread_f", getShaftThreadF());
        partObject.put("number_of_blades", getNumberOfBlades());
    }

    public Float getExducerDiameterA() {
        return exducerDiameterA;
    }

    public void setExducerDiameterA(Float exducerDiameterA) {
        this.exducerDiameterA = exducerDiameterA;
    }

    public Float getTipHeightB() {
        return tipHeightB;
    }

    public void setTipHeightB(Float tipHeightB) {
        this.tipHeightB = tipHeightB;
    }

    public Float getInducerDiameterC() {
        return inducerDiameterC;
    }

    public void setInducerDiameterC(Float inducerDiameterC) {
        this.inducerDiameterC = inducerDiameterC;
    }

    public Float getJournalDiameterD() {
        return journalDiameterD;
    }

    public void setJournalDiameterD(Float journalDiameterD) {
        this.journalDiameterD = journalDiameterD;
    }

    public Float getStemDiameterE() {
        return stemDiameterE;
    }

    public void setStemDiameterE(Float stemDiameterE) {
        this.stemDiameterE = stemDiameterE;
    }

    public String getShaftThreadF() {
        return shaftThreadF;
    }

    public void setShaftThreadF(String shaftThreadF) {
        this.shaftThreadF = shaftThreadF;
    }

    public String getNumberOfBlades() {
        return numberOfBlades;
    }

    public void setNumberOfBlades(String numberOfBlades) {
        this.numberOfBlades = numberOfBlades;
    }
}