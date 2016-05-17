package com.turbointernational.metadata.domain.part.types;

import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.domain.part.Part;
import com.turbointernational.metadata.web.View;
import java.util.Map;
import javax.persistence.*;

import org.apache.commons.lang.ObjectUtils;

/**
 * http://www.turbointernational.com/products/majorCmptDetails.asp?mcid=454&turbotype=35
 * @author Jeff
 */
@Entity
@Table(name="turbine_wheel")
@PrimaryKeyJoinColumn(name = "part_id")
public class TurbineWheel extends Part {

    @JsonView(View.Detail.class)
    @Column(name="exduce_oa")
    private Float exducerDiameterA;

    @JsonView(View.Detail.class)
    @Column(name="tip_height_b")
    private Float tipHeightB;

    @JsonView(View.Detail.class)
    @Column(name="inducer_oc")
    private Float inducerDiameterC;

    @JsonView(View.Detail.class)
    @Column(name="journal_od")
    private Float journalDiameterD;

    @JsonView(View.Detail.class)
    @Column(name="stem_oe")
    private Float stemDiameterE;

    @JsonView(View.Detail.class)
    @Column(name="shaft_thread_f")
    private String shaftThreadF;

    @JsonView(View.Detail.class)
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

    /*
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
    */

}
