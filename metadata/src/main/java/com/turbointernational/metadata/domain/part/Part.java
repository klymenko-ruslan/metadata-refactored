package com.turbointernational.metadata.domain.part;
import com.turbointernational.metadata.domain.bom.BOMItem;
import com.turbointernational.metadata.domain.interchange.Interchange;
import com.turbointernational.metadata.domain.other.Manufacturer;
import java.util.Set;
import javax.persistence.CascadeType;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@RooJavaBean
@RooJpaActiveRecord(table="PART", inheritanceType = "SINGLE_TABLE")
public class Part {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "manfr_id", nullable = false)
    private Manufacturer manufacturer;

    @Column(name = "manfr_part_num")
    private String manufacturerPartNumber;

    @Column(name = "name")
    private String name;

    @Column(name="description")
    private String description;

    @Column(nullable = false, columnDefinition = "BIT", length = 1)
    private Boolean inactive;

    @ManyToMany(mappedBy = "parts", fetch = FetchType.LAZY)
    private Set<Interchange> interchanges;

    @OneToMany(mappedBy="parent", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<BOMItem> bom;

}
