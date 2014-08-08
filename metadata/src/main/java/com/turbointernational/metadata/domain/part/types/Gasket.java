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
import javax.persistence.Table;
import net.sf.jsog.JSOG;
import org.apache.commons.lang.ObjectUtils;
import org.springframework.beans.factory.annotation.Configurable;

@Configurable
@Entity
@Table(name="gasket")
@PrimaryKeyJoinColumn(name = "part_id")
public class Gasket extends Part {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="gasket_type_id")
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
    protected JSONSerializer getSearchSerializer() {
        return super.getSearchSerializer()
                    .include("gasketType.id")
                    .include("gasketType.name");
    }
    
    @Override
    public void csvColumns(Map<String, String> columns) {
        super.csvColumns(columns);
        
        if (getGasketType() != null) {
            columns.put("gasket_type", ObjectUtils.toString(getGasketType().getName()));
        }
    }
}
