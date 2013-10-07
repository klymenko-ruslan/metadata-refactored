package com.turbointernational.metadata.domain.part.types;
import com.turbointernational.metadata.domain.part.Part;
import com.turbointernational.metadata.domain.type.KitType;
import javax.persistence.CascadeType;
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
    
    @OneToOne(cascade = {CascadeType.REFRESH, CascadeType.MERGE})
    @JoinColumn(name="kit_type_id", table = "kit")
    private KitType kitType;

    public KitType getKitType() {
        return kitType;
    }

    public void setKitType(KitType kitType) {
        this.kitType = kitType;
    }

    @Override
    public JSOG toJsog() {
        JSOG partObject = super.toJsog();
        
        if (kitType != null) {
            partObject.put("kit_type_name", kitType.getName());
        }
        
        return partObject;
    }
}
