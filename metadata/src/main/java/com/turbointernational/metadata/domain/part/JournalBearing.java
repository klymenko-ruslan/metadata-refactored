package com.turbointernational.metadata.domain.part;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToOne;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(table="JOURNAL_BEARING", inheritanceType = "JOINED")
public class JournalBearing extends Part {

    @OneToOne
    @JoinTable(name="STANDARD_JOURNAL_BEARING",
               joinColumns=@JoinColumn(name="oversized_part_id"),
               inverseJoinColumns=@JoinColumn(name="standard_part_id"))
    private JournalBearing standardSize;

    @OneToOne
    @JoinTable(name="STANDARD_JOURNAL_BEARING",
               joinColumns=@JoinColumn(name="standard_part_id"),
               inverseJoinColumns=@JoinColumn(name="oversized_part_id"))
    private JournalBearing oversize;

    @Column(name="outside_dim_min")
    private Float outsideDiameterMin;

    @Column(name="outside_dim_max")
    private Float outsideDiameterMax;

    @Column(name="inside_dim_min")
    private Float insideDiameterMin;

    @Column(name="inside_dim_max")
    private Float insideDiameterMax;
}
