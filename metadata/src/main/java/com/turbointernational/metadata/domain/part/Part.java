package com.turbointernational.metadata.domain.part;
import com.turbointernational.metadata.domain.Interchange;
import com.turbointernational.metadata.domain.Manufacturer;
import com.turbointernational.metadata.domain.type.PartType;
import java.util.List;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
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

    @Column(nullable = false)
    private Boolean inactive;

    private Long importPk;

    @ManyToMany
    @JoinTable(name="INTERCHANGE_ITEM",
               joinColumns=@JoinColumn(name="part_id"),
               inverseJoinColumns=@JoinColumn(name="turbo_type_id"))
    private List<Interchange> interchanges;

}
