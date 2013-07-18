package com.turbointernational.metadata.domain;
import com.turbointernational.metadata.domain.type.ManufacturerType;
import javax.persistence.Column;
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
@Table(name = "MANFR",
       uniqueConstraints=@UniqueConstraint(columnNames={"name"}))
public class Manufacturer {

    @Column(nullable=false)
    private String name;

    @ManyToOne
    @JoinColumn(name="manfr_type_id", nullable=false)
    private ManufacturerType type;

    @ManyToOne
    @JoinColumn(name="parent_manfr_id")
    private Manufacturer parent;

    @Column(name="import_pk")
    private Long importPk;


}
