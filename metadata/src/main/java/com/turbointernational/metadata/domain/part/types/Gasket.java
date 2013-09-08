package com.turbointernational.metadata.domain.part.types;
import com.turbointernational.metadata.domain.part.Part;
import com.turbointernational.metadata.domain.type.GasketType;
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
@SecondaryTable(name="gasket", pkJoinColumns=@PrimaryKeyJoinColumn(name = "part_id"))
public class Gasket extends Part {

    @OneToOne
    @JoinColumn(name="gasket_type_id", table = "gasket")
    private GasketType type;

    public GasketType getType() {
        return type;
    }

    public void setType(GasketType type) {
        this.type = type;
    }

    @Override
    public JSOG toJsog() {
        JSOG partObject = super.toJsog();
        
        if (type != null) {
            partObject.put("gasket_type_name", type.getName());
        }
        
        return partObject;
    }
}
