package com.turbointernational.metadata.domain;
import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
@Table(uniqueConstraints=@UniqueConstraint(columnNames={"part_id, attribute_type_id"}))
public class PartAttribute {

    @Column(nullable=false)
    private Part part;

    @Column(nullable=false)
    private AttributeType attributeType;

    private String value;
}
