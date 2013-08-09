package com.turbointernational.metadata.domain.other;
import com.turbointernational.metadata.domain.type.ManufacturerType;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;

@Cacheable
@RooJavaBean
@RooJpaActiveRecord
@Table(name="MANFR", uniqueConstraints=@UniqueConstraint(columnNames={"name"}))
public class Manufacturer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false)
    private String name;

    @OneToOne
    @JoinColumn(name="manfr_type_id", nullable=false)
    private ManufacturerType type;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="parent_manfr_id")
    private Manufacturer parent;


}
