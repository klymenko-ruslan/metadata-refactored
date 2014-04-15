package com.turbointernational.metadata.domain.part.types;
import com.turbointernational.metadata.domain.part.Part;
import com.turbointernational.metadata.domain.part.types.kit.KitComponent;
import com.turbointernational.metadata.domain.type.KitType;
import flexjson.JSONSerializer;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.SecondaryTable;
import net.sf.jsog.JSOG;
import org.springframework.beans.factory.annotation.Configurable;

@Configurable
@Entity
@SecondaryTable(name="kit", pkJoinColumns=@PrimaryKeyJoinColumn(name = "part_id"))
public class Kit extends Part {
    
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="kit_type_id", table = "kit")
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
            .include("kitType.*")
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
    public JSOG toJsog() {
        JSOG partObject = super.toJsog();
        
        if (kitType != null) {
            partObject.put("kit_type_name", kitType.getName());
        }
        
        return partObject;
    }
    
    @Override
    public void csvColumns(Map<String, String> columns) {
        super.csvColumns(columns);
        
        if (kitType != null) {
            columns.put("kit_type", kitType.getName());
        }
    }
}
