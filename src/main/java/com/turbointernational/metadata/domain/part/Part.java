package com.turbointernational.metadata.domain.part;
import com.turbointernational.metadata.domain.Interchange;
import com.turbointernational.metadata.domain.Manufacturer;
import com.turbointernational.metadata.domain.type.PartType;
import com.turbointernational.metadata.domain.type.TurboType;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
public class Part {

    @Column(name = "manfr_part_num")
    private String manufacturerPartNumber;

    @ManyToOne
    @JoinColumn(name = "manfr_id", nullable = false)
    private Manufacturer manufacturer;

    @ManyToOne
    @JoinColumn(name = "part_type_id", nullable = false)
    private PartType type;

    private String name;

    private String description;

    @Column(nullable = false)
    private Boolean inactive;

    private Long importPk;

    @OneToMany
    @JoinTable(name="PART_TURBO_TYPE",
               joinColumns=@JoinColumn(name="part_id"),
               inverseJoinColumns=@JoinColumn(name="turbo_type_id"))
    private List<TurboType> turboTypes;

    @ManyToMany
    @JoinTable(name="INTERCHANGE_ITEM",
               joinColumns=@JoinColumn(name="part_id"),
               inverseJoinColumns=@JoinColumn(name="turbo_type_id"))
    private List<Interchange> interchanges;


    Probably deleting this relationship.
    // ???: ManyToMany?
    // https://github.com/zero-one/TurboInternational/issues/29
    @ManyToMany
    @JoinTable(name="PART_TURBO_TYPE",
               joinColumns=@JoinColumn(name="part_id"),
               inverseJoinColumns=@JoinColumn(name="turbo_type_id"))
    private List<Part> parentParts;


    // ???: ManyToMany?
    // https://github.com/zero-one/TurboInternational/issues/29
    @ManyToMany
    @JoinTable(name="PART_TURBO_TYPE",
               joinColumns=@JoinColumn(name="part_id"),
               inverseJoinColumns=@JoinColumn(name="turbo_type_id"))
    private List<Part> childParts;

    // ???: What are these?
    private String temp1Char;

    private String temp2Char;

    private String temp3Char;

    private String temp4Char;

    private String temp5Char;

    private String temp6Char;

    private String temp7Char;

    private String temp8Char;

    private Integer temp2Int;

    private Integer temp3Int;

    private Double temp1Dec;

    private Double temp2Dec;

    private Double temp3Dec;

    private Double temp4Dec;

    private Double temp5Dec;

    private Double temp6Dec;
    
}
