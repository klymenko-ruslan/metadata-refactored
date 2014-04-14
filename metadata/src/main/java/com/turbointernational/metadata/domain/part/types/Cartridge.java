package com.turbointernational.metadata.domain.part.types;
import com.turbointernational.metadata.domain.part.Part;
import flexjson.JSONSerializer;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.SecondaryTable;
import org.springframework.beans.factory.annotation.Configurable;

@Configurable
@Entity
@SecondaryTable(name="cartridge", pkJoinColumns=@PrimaryKeyJoinColumn(name = "part_id"))
public class Cartridge extends Part {
    
    /**
     * View takes care of joining by common turbo type and handling the kit_part_common_component aspect.
     */
    @OneToMany
    @JoinTable(name="vcartridge_kits",
        joinColumns = @JoinColumn(name="cartridge_id"),
        inverseJoinColumns = @JoinColumn(name="kit_id"))
    private final Set<Kit> serviceKits = new TreeSet();

    @Override
    public void csvColumns(Map<String, String> columns) {
        super.csvColumns(columns);
        
        // Service kits
        String kits = new JSONSerializer()
            .include("id")
            .include("kitType.name")
            .include("manufacturerPartNumber")
            .exclude("*")
            .serialize(serviceKits);
        
        columns.put("service_kits", kits);
    }

}
