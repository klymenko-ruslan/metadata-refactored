package com.turbointernational.metadata.domain.type;
import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
@Table(name="GASKET_TYPE", uniqueConstraints=@UniqueConstraint(columnNames={"name"}))
public class GasketType {

    @Column(nullable=false)
    private String name;
}
