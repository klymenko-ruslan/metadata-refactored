package com.turbointernational.metadata.domain.part.types;
import com.turbointernational.metadata.domain.type.CoolType;
import com.turbointernational.metadata.domain.other.TurboModel;
import com.turbointernational.metadata.domain.part.Part;
import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.SecondaryTable;
import net.sf.jsog.JSOG;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;

@Cacheable
@Configurable
@Entity
@RooJpaActiveRecord
@SecondaryTable(name="turbo", pkJoinColumns=@PrimaryKeyJoinColumn(name = "part_id"))
public class Turbo extends Part {

    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.REFRESH})
    @JoinColumn(name="turbo_model_id", table = "turbo")
    private TurboModel model;

    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.REFRESH})
    @JoinColumn(name="cool_type_id", table = "turbo")
    private CoolType coolType;

    public TurboModel getModel() {
        return model;
    }

    public void setModel(TurboModel model) {
        this.model = model;
    }

    public CoolType getCoolType() {
        return coolType;
    }

    public void setCoolType(CoolType coolType) {
        this.coolType = coolType;
    }

    @Override
    public JSOG toJsog() {
        JSOG partObject = super.toJsog();
        
        if (model != null) {
            partObject.put("turbo_model_name", model.getName());
        }

        if (getCoolType() != null) {
            partObject.put("cool_type_name", getCoolType().getName());
        }
        
        return partObject;
    }
    
}
