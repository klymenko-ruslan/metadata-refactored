package com.turbointernational.metadata.domain.part;
import com.turbointernational.metadata.domain.interchange.Interchange;
import com.turbointernational.metadata.domain.other.Manufacturer;
import com.turbointernational.metadata.domain.type.PartType;
import java.util.List;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(table="PART_ORM_VIEW")
public abstract class Part {

    @Column(name = "manfr_part_num")
    private String manufacturerPartNumber;

    @ManyToOne
    @JoinColumn(name = "manfr_id", nullable = false)
    private Manufacturer manufacturer;

    @ManyToOne
    @JoinColumn(name = "part_type_id", nullable = false)
    private PartType partType;

    @Column(name = "Name")
    private String name;

    private String description;

    @Column(nullable = false, columnDefinition = "BIT", length = 1)
    private Boolean inactive;

    @OneToMany
    @JoinTable(name="INTERCHANGE_ITEM",
               joinColumns=@JoinColumn(name="part_id"),
               inverseJoinColumns=@JoinColumn(name="interchange_header_id"))
    private List<Interchange> interchanges;

}
