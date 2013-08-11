package com.turbointernational.metadata.domain.type;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import org.springframework.roo.addon.json.RooJson;

@Cacheable
@RooJavaBean
@RooJpaActiveRecord
@RooJson
@Table(name="MANFR_TYPE",
       uniqueConstraints=@UniqueConstraint(columnNames={"name"}))
public class ManufacturerType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false)
    private String name;
}
