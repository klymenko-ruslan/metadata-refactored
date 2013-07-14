package com.turbointernational.metadata.domain.part;
import com.turbointernational.metadata.domain.type.KitType;
import javax.persistence.Column;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
@Inheritance(strategy= InheritanceType.JOINED)
public class Kit extends Part {
    private String name;

    @Column(name="kit_type_id")
    private KitType type;
}
