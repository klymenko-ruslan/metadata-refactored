package com.turbointernational.metadata.domain;
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
    @Column(name = "manfr_id", nullable = false)
    private Manufacturer manufacturer;

    @ManyToOne
    @Column(name = "part_type_id", nullable = false)
    private PartType type;

    @Column(name = "Name")
    private String name;

    private String description;

    @Column(nullable = false)
    private Boolean inactive;

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

    private Long importPk;

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
