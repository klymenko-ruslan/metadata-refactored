// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package ti.metadata.domain.part;

import java.math.BigDecimal;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import ti.metadata.domain.part.JournalBearing;
import ti.metadata.domain.part.Part;
import ti.metadata.domain.part.StandardJournalBearing;

privileged aspect JournalBearing_Roo_DbManaged {
    
    @OneToOne
    @JoinColumn(name = "part_id", nullable = false, insertable = false, updatable = false)
    private Part JournalBearing.part;
    
    @OneToMany(mappedBy = "standardPartId")
    private Set<StandardJournalBearing> JournalBearing.standardJournalBearings;
    
    @OneToMany(mappedBy = "oversizedPartId")
    private Set<StandardJournalBearing> JournalBearing.standardJournalBearings1;
    
    @Column(name = "outside_dim_min", precision = 18, scale = 6)
    @NotNull
    private BigDecimal JournalBearing.outsideDimMin;
    
    @Column(name = "outside_dim_max", precision = 18, scale = 6)
    @NotNull
    private BigDecimal JournalBearing.outsideDimMax;
    
    @Column(name = "inside_dim_min", precision = 18, scale = 6)
    @NotNull
    private BigDecimal JournalBearing.insideDimMin;
    
    @Column(name = "inside_dim_max", precision = 18, scale = 6)
    @NotNull
    private BigDecimal JournalBearing.insideDimMax;
    
    @Column(name = "width", precision = 18, scale = 6)
    @NotNull
    private BigDecimal JournalBearing.width;
    
    public Part JournalBearing.getPart() {
        return part;
    }
    
    public void JournalBearing.setPart(Part part) {
        this.part = part;
    }
    
    public Set<StandardJournalBearing> JournalBearing.getStandardJournalBearings() {
        return standardJournalBearings;
    }
    
    public void JournalBearing.setStandardJournalBearings(Set<StandardJournalBearing> standardJournalBearings) {
        this.standardJournalBearings = standardJournalBearings;
    }
    
    public Set<StandardJournalBearing> JournalBearing.getStandardJournalBearings1() {
        return standardJournalBearings1;
    }
    
    public void JournalBearing.setStandardJournalBearings1(Set<StandardJournalBearing> standardJournalBearings1) {
        this.standardJournalBearings1 = standardJournalBearings1;
    }
    
    public BigDecimal JournalBearing.getOutsideDimMin() {
        return outsideDimMin;
    }
    
    public void JournalBearing.setOutsideDimMin(BigDecimal outsideDimMin) {
        this.outsideDimMin = outsideDimMin;
    }
    
    public BigDecimal JournalBearing.getOutsideDimMax() {
        return outsideDimMax;
    }
    
    public void JournalBearing.setOutsideDimMax(BigDecimal outsideDimMax) {
        this.outsideDimMax = outsideDimMax;
    }
    
    public BigDecimal JournalBearing.getInsideDimMin() {
        return insideDimMin;
    }
    
    public void JournalBearing.setInsideDimMin(BigDecimal insideDimMin) {
        this.insideDimMin = insideDimMin;
    }
    
    public BigDecimal JournalBearing.getInsideDimMax() {
        return insideDimMax;
    }
    
    public void JournalBearing.setInsideDimMax(BigDecimal insideDimMax) {
        this.insideDimMax = insideDimMax;
    }
    
    public BigDecimal JournalBearing.getWidth() {
        return width;
    }
    
    public void JournalBearing.setWidth(BigDecimal width) {
        this.width = width;
    }
    
}
