package com.turbointernational.metadata.domain.part;
import com.turbointernational.metadata.domain.type.CoolType;
import com.turbointernational.metadata.domain.other.TurboModel;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.SecondaryTable;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;

@RooJavaBean
@RooJpaActiveRecord
@SecondaryTable(name="turbo", pkJoinColumns=@PrimaryKeyJoinColumn(name = "part_id"))
public class Turbo extends Part {

    @OneToOne
    @JoinColumn(name="turbo_model_id", table = "turbo")
    private TurboModel model;

    @OneToOne
    @JoinColumn(name="cool_type_id", table = "turbo")
    private CoolType coolType;
    
}
