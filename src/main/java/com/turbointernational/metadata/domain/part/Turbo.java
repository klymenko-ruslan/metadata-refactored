package com.turbointernational.metadata.domain.part;
import com.turbointernational.metadata.domain.type.CoolType;
import com.turbointernational.metadata.domain.TurboModel;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
@Inheritance(strategy=InheritanceType.JOINED)
public class Turbo extends Part {
    private TurboModel model;
    private CoolType coolType;
    
}
