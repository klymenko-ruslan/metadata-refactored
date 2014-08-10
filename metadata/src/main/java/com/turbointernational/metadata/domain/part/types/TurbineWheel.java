package com.turbointernational.metadata.domain.part.types;
import com.turbointernational.metadata.domain.part.Part;
import java.util.Map;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import net.sf.jsog.JSOG;
import org.apache.commons.lang.ObjectUtils;
import org.springframework.beans.factory.annotation.Configurable;


/**
 * http://www.turbointernational.com/products/majorCmptDetails.asp?mcid=454&turbotype=35
 * @author Jeff
 */
@Configurable
@Entity
@Table(name="turbine_wheel")
@PrimaryKeyJoinColumn(name = "part_id")
public class TurbineWheel extends Part {

    @Column(name="exduce_oa")
    private Float exducerDiameterA;

    @Column(name="tip_height_b")
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
    private String numberOfBlades;

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
    
    @Override
    public void csvColumns(Map<String, String> columns) {
        super.csvColumns(columns);
        
        columns.put("exducer_oa", ObjectUtils.toString(getExducerDiameterA()));
        columns.put("tip_height_b", ObjectUtils.toString(getTipHeightB()));
        columns.put("inducer_oc", ObjectUtils.toString(getInducerDiameterC()));
        columns.put("journal_od", ObjectUtils.toString(getJournalDiameterD()));
        columns.put("stem_oe", ObjectUtils.toString(getStemDiameterE()));
        columns.put("shaft_thread_f", ObjectUtils.toString(getShaftThreadF()));
        columns.put("number_of_blades", ObjectUtils.toString(getNumberOfBlades()));
    }
}
