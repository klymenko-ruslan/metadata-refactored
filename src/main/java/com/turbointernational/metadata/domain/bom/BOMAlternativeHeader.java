package com.turbointernational.metadata.domain.bom;
import javax.persistence.Column;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(table="BOM_ALT_HEADER")
public class BOMAlternativeHeader {

    @Column(nullable=false)
    private String name;

    private String description;
}
