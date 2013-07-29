// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package ti.metadata.domain.part;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import ti.metadata.domain.part.Part;
import ti.metadata.domain.part.TurbineWheel;

privileged aspect TurbineWheel_Roo_DbManaged {
    
    @OneToOne
    @JoinColumn(name = "part_id", nullable = false, insertable = false, updatable = false)
    private Part TurbineWheel.part;
    
    @Column(name = "exduce_oa", precision = 18, scale = 6)
    private BigDecimal TurbineWheel.exduceOa;
    
    @Column(name = "tip_height_b", precision = 18, scale = 6)
    private BigDecimal TurbineWheel.tipHeightB;
    
    @Column(name = "inducer_oc", precision = 18, scale = 6)
    private BigDecimal TurbineWheel.inducerOc;
    
    @Column(name = "journal_od", precision = 18, scale = 6)
    private BigDecimal TurbineWheel.journalOd;
    
    @Column(name = "stem_oe", precision = 18, scale = 6)
    private BigDecimal TurbineWheel.stemOe;
    
    @Column(name = "trim_no_blades", length = 100)
    private String TurbineWheel.trimNoBlades;
    
    @Column(name = "shaft_thread_f", length = 100)
    private String TurbineWheel.shaftThreadF;
    
    public Part TurbineWheel.getPart() {
        return part;
    }
    
    public void TurbineWheel.setPart(Part part) {
        this.part = part;
    }
    
    public BigDecimal TurbineWheel.getExduceOa() {
        return exduceOa;
    }
    
    public void TurbineWheel.setExduceOa(BigDecimal exduceOa) {
        this.exduceOa = exduceOa;
    }
    
    public BigDecimal TurbineWheel.getTipHeightB() {
        return tipHeightB;
    }
    
    public void TurbineWheel.setTipHeightB(BigDecimal tipHeightB) {
        this.tipHeightB = tipHeightB;
    }
    
    public BigDecimal TurbineWheel.getInducerOc() {
        return inducerOc;
    }
    
    public void TurbineWheel.setInducerOc(BigDecimal inducerOc) {
        this.inducerOc = inducerOc;
    }
    
    public BigDecimal TurbineWheel.getJournalOd() {
        return journalOd;
    }
    
    public void TurbineWheel.setJournalOd(BigDecimal journalOd) {
        this.journalOd = journalOd;
    }
    
    public BigDecimal TurbineWheel.getStemOe() {
        return stemOe;
    }
    
    public void TurbineWheel.setStemOe(BigDecimal stemOe) {
        this.stemOe = stemOe;
    }
    
    public String TurbineWheel.getTrimNoBlades() {
        return trimNoBlades;
    }
    
    public void TurbineWheel.setTrimNoBlades(String trimNoBlades) {
        this.trimNoBlades = trimNoBlades;
    }
    
    public String TurbineWheel.getShaftThreadF() {
        return shaftThreadF;
    }
    
    public void TurbineWheel.setShaftThreadF(String shaftThreadF) {
        this.shaftThreadF = shaftThreadF;
    }
    
}
