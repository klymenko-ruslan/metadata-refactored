package com.turbointernational.metadata.domain.type;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
public class PartType {

    @Column(nullable=false)
    private String name;

    // TODO: Is this a duplicate of parentTypes?
    @ManyToOne
    @Column(name="parent_part_type_id")
    private PartType parent;

    @Column(name="import_pk")
    private Long importPk;

    // TODO: Is this a duplicate of parent?
    @ManyToMany
    @JoinTable(name="BOM_HIERARCHY",
               joinColumns=@JoinColumn(name="child_part_type_id"),
               inverseJoinColumns=@JoinColumn(name="parent_part_type_id"))
    private List<PartType> parentTypes;

    @ManyToMany
    @JoinTable(name="BOM_HIERARCHY",
               joinColumns=@JoinColumn(name="parent_part_type_id"),
               inverseJoinColumns=@JoinColumn(name="child_part_type_id"))
    private List<PartType> childTypes;
}
