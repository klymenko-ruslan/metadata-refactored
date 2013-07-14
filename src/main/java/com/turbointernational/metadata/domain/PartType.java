package com.turbointernational.metadata.domain;
import javax.persistence.Column;
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

    @ManyToOne
    @Column(name="parent_part_type_id")
    private PartType parent;

    @Column(name="import_pk")
    private Long importPk;
}
