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
@Table(uniqueConstraints=@UniqueConstraint(columnNames={"part_types_id", "name"}))
public class AttributeType {

    // TODO: Check on response from paul on this field
    // https://github.com/zero-one/TurboInternational/pull/28/files#L0R72
    
    @Column(name="part_types_id", nullable=false)
    private PartType partType;

    @Column(nullable=false)
    private String name;
}
