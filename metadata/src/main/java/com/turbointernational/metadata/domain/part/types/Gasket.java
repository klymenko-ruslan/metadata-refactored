package com.turbointernational.metadata.domain.part.types;
import com.turbointernational.metadata.domain.part.Part;
import com.turbointernational.metadata.domain.type.GasketType;
import flexjson.JSONSerializer;
import java.util.Map;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.SecondaryTable;
import net.sf.jsog.JSOG;
import org.apache.commons.lang.ObjectUtils;
import org.springframework.beans.factory.annotation.Configurable;

@Configurable
@Entity
@SecondaryTable(name="gasket", pkJoinColumns=@PrimaryKeyJoinColumn(name = "part_id"))
public class Gasket extends Part {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="gasket_type_id", table = "gasket")
    private GasketType gasketType;

    public GasketType getGasketType() {
        return gasketType;
    }

    public void setGasketType(GasketType gasketType) {
        this.gasketType = gasketType;
    }

    @Override
    protected JSONSerializer buildJSONSerializer() {
        return super.buildJSONSerializer()
            .include("gasketType.id")
            .include("gasketType.name")
            .include("gasketType.version");
    }

    @Override
    public JSOG toJsog() {
        JSOG partObject = super.toJsog();
        
        if (gasketType != null) {
            partObject.put("gasket_type", gasketType.getName());
        }
        
        return partObject;
    }
}
