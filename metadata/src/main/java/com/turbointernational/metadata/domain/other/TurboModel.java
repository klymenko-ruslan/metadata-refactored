package com.turbointernational.metadata.domain.other;
import com.turbointernational.metadata.domain.type.TurboType;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;

@Cacheable
@RooJavaBean
@RooJpaActiveRecord(table="TURBO_MODEL")
public class TurboModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false)
    private String name;

    @OneToOne
    @JoinColumn(name="turbo_type_id")
    private TurboType type;
}
