package com.turbointernational.metadata.domain.part;
import com.turbointernational.metadata.domain.type.CoolType;
import com.turbointernational.metadata.domain.other.TurboModel;
import javax.persistence.DiscriminatorValue;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(table="TURBO", inheritanceType = "JOINED")
@DiscriminatorValue(value = "1")
@PrimaryKeyJoinColumn(name = "part_id")
public class Turbo extends Part {

    @ManyToOne
    @JoinColumn(name="turbo_model_id")
    private TurboModel model;

    @ManyToOne
    @JoinColumn(name="cool_type_id")
    private CoolType coolType;
    
}
