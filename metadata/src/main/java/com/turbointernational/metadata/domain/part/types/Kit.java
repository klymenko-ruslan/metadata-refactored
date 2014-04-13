package com.turbointernational.metadata.domain.part.types;
import com.turbointernational.metadata.domain.part.Part;
import com.turbointernational.metadata.domain.part.types.kit.KitServices;
import com.turbointernational.metadata.domain.type.KitType;
import flexjson.JSONSerializer;
import java.util.Map;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
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
    
    @OneToMany(mappedBy="kit", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<KitServices> services;
    
    public KitType getKitType() {
        return kitType;
    }

    public void setKitType(KitType kitType) {
        this.kitType = kitType;
    }

    public Set<KitServices> getServices() {
        return services;
    }

    public void setServices(Set<KitServices> services) {
        this.services = services;
    }
    
    @Override
    protected JSONSerializer buildJSONSerializer() {
        return super.buildJSONSerializer()
            .include("kitType.id")
            .include("kitType.name")
            .include("kitType.version")
            .include("services.exclude")
            .include("services.part.id")
            .include("services.part.name")
            .include("services.part.manufacturerPartNumber")
            .include("services.part.partType.id")
            .include("services.part.partType.name")
            .include("services.part.manufacturer.name");
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
