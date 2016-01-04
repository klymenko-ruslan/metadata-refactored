package com.turbointernational.metadata.domain.part.types;

import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.domain.part.Part;
import com.turbointernational.metadata.domain.type.GasketType;
import com.turbointernational.metadata.web.View;
import flexjson.JSONSerializer;
import java.util.Map;
import javax.persistence.*;

import org.apache.commons.lang.ObjectUtils;

@Entity
@Table(name="gasket")
@PrimaryKeyJoinColumn(name = "part_id")
public class Gasket extends Part {

    @JsonView(View.Summary.class)
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
