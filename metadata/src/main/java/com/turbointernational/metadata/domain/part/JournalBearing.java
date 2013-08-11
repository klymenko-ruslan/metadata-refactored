package com.turbointernational.metadata.domain.part;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.SecondaryTable;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;

@RooJavaBean
@RooJpaActiveRecord
@RooJson
@SecondaryTable(name="JOURNAL_BEARING", pkJoinColumns=@PrimaryKeyJoinColumn(name = "part_id"))
public class JournalBearing extends Part {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinTable(name="STANDARD_JOURNAL_BEARING",
               joinColumns=@JoinColumn(name="oversized_part_id"),
               inverseJoinColumns=@JoinColumn(name="standard_part_id"))
    private JournalBearing standardSize;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinTable(name="STANDARD_JOURNAL_BEARING",
               joinColumns=@JoinColumn(name="standard_part_id"),
               inverseJoinColumns=@JoinColumn(name="oversized_part_id"))
    private JournalBearing oversize;

    @Column(name="outside_dim_min", table = "JOURNAL_BEARING")
    private Float outsideDiameterMin;

    @Column(name="outside_dim_max", table = "JOURNAL_BEARING")
    private Float outsideDiameterMax;

    @Column(name="inside_dim_min", table = "JOURNAL_BEARING")
    private Float insideDiameterMin;

    @Column(name="inside_dim_max", table = "JOURNAL_BEARING")
    private Float insideDiameterMax;
}
