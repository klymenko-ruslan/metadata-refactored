package com.turbointernational.metadata.domain.other;
import com.turbointernational.metadata.domain.type.TurboType;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(table="TURBO_MODEL")
public class TurboModel {

    @Column(nullable=false)
    private String name;

    @ManyToOne
    @JoinColumn(name="turbo_type_id")
    private TurboType type;
}
