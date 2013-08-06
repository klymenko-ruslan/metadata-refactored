package com.turbointernational.metadata.domain.part;
import com.turbointernational.metadata.domain.bom.BOMItem;
import com.turbointernational.metadata.domain.interchange.Interchange;
import com.turbointernational.metadata.domain.other.Manufacturer;
import java.util.List;
import javax.persistence.CascadeType;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(table="PART_ORM_VIEW", inheritanceType = "SINGLE_TABLE")
public class Part {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "manfr_part_num")
    private String manufacturerPartNumber;

    @ManyToOne
    @JoinColumn(name = "manfr_id", nullable = false)
    private Manufacturer manufacturer;

    @Column(name = "name")
    private String name;

    @Column(name="description")
    private String description;

    @Column(nullable = false, columnDefinition = "BIT", length = 1)
    private Boolean inactive;

    @OneToMany
    @JoinTable(name="INTERCHANGE_ITEM",
               joinColumns=@JoinColumn(name="part_id"),
               inverseJoinColumns=@JoinColumn(name="interchange_header_id"))
    private List<Interchange> interchanges;

//    @OneToMany(mappedBy="parent", cascade = CascadeType.ALL)
//    private List<BOMItem> bom;

}
