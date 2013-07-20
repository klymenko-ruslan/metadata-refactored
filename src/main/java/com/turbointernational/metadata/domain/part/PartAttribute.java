package com.turbointernational.metadata.domain.part;

import com.turbointernational.metadata.domain.type.AttributeType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
@Table(uniqueConstraints=@UniqueConstraint(columnNames={"part_id", "attribute_type_id"}))
public class PartAttribute {

    @ManyToOne
    @JoinColumn(name="part_id", nullable=false)
    private Part part;

    @ManyToOne
    @JoinColumn(name="attribute_type_id", nullable=false)
    private AttributeType attributeType;

    private String value;
}
