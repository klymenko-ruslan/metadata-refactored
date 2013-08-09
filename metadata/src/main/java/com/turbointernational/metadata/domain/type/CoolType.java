package com.turbointernational.metadata.domain.type;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;

@Cacheable
@RooJavaBean
@RooJpaActiveRecord(table = "COOL_TYPE")
public class CoolType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false)
    private String name;
}
