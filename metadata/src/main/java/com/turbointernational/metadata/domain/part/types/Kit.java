package com.turbointernational.metadata.domain.part.types;
import com.turbointernational.metadata.domain.part.Part;
import com.turbointernational.metadata.domain.type.KitType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.SecondaryTable;
import net.sf.jsog.JSOG;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;

@Configurable
@Entity
@RooJpaActiveRecord
@SecondaryTable(name="kit", pkJoinColumns=@PrimaryKeyJoinColumn(name = "part_id"))
public class Kit extends Part {
    @OneToOne
    @JoinColumn(name="kit_type_id", table = "kit")
    private KitType type;

    @Override
    public JSOG toJsog() {
        JSOG partObject = super.toJsog();
        
        if (type != null) {
            partObject.put("kit_type_name", type.getName());
        }
        
        return partObject;
    }

    public KitType getType() {
        return type;
    }

    public void setType(KitType type) {
        this.type = type;
    }
}
