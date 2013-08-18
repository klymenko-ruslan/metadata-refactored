package com.turbointernational.metadata.domain.part;
import com.turbointernational.metadata.domain.type.CoolType;
import com.turbointernational.metadata.domain.other.TurboModel;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.SecondaryTable;
import net.sf.jsog.JSOG;
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

    @Override
    public void addIndexFields(JSOG partObject) {
        if (model != null) {
            partObject.put("turbo_model_name", model.getName());
        }

        if (coolType != null) {
            partObject.put("cool_type_name", coolType.getName());
        }
    }
    
}
