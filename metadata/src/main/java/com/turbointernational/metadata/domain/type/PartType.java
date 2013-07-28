package com.turbointernational.metadata.domain.type;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(table = "PART_TYPE")
public class PartType {

    @Column(nullable=false)
    private String name;

    // TODO: Is this a duplicate of parentTypes?
    @ManyToOne
    @JoinColumn(name="parent_part_type_id")
    private PartType parent;
}
