package com.turbointernational.metadata.domain.part.types;

import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.domain.part.Part;
import com.turbointernational.metadata.domain.part.types.kit.KitComponent;
import com.turbointernational.metadata.domain.type.KitType;
import com.turbointernational.metadata.web.View;
import flexjson.JSONSerializer;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

@Entity
@Table(name="kit")
@PrimaryKeyJoinColumn(name = "part_id")
public class Kit extends Part {
    
    @JsonView(View.Detail.class)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="kit_type_id")
    private KitType kitType;
    
    @OrderBy("id")
    @OneToMany(mappedBy="kit", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<KitComponent> components = new LinkedHashSet();
    
    public KitType getKitType() {
        return kitType;
    }

    public void setKitType(KitType kitType) {
        this.kitType = kitType;
    }

    public Set<KitComponent> getComponents() {
        return components;
    }

    public void setComponents(Set<KitComponent> components) {
        this.components = components;
    }
    
    @Override
    protected JSONSerializer buildJSONSerializer() {
        return super.buildJSONSerializer()
            .include("kitType.id")
            .include("kitType.name")
            .exclude("kitType.*")
            .include("components.id")
            .include("components.exclude")
            .include("components.part.id")
            .include("components.part.name")
            .include("components.part.manufacturerPartNumber")
            .include("components.part.partType.id")
            .include("components.part.partType.name")
            .include("components.part.manufacturer.name")
            .exclude("components.*");
    }
    
    @Override
    protected JSONSerializer getSearchSerializer() {
        return super.getSearchSerializer()
                    .include("kitType.id")
                    .include("kitType.name");
    }
    
    @Override
    public void csvColumns(Map<String, String> columns) {
        super.csvColumns(columns);
        
        if (kitType != null) {
            columns.put("kit_type", kitType.getName());
        }
    }
}
