package com.turbointernational.metadata.domain.part.types;
import com.turbointernational.metadata.domain.part.Part;
import java.util.Set;
import java.util.TreeSet;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import org.springframework.beans.factory.annotation.Configurable;

@Configurable
@Entity
@Table(name="cartridge")
@PrimaryKeyJoinColumn(name = "part_id")
public class Cartridge extends Part {
    
    /**
     * View takes care of joining by common turbo type and handling the kit_part_common_component aspect.
     */
    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable(name="vpart_turbotype_kits",
        joinColumns = @JoinColumn(name="part_id"),
        inverseJoinColumns = @JoinColumn(name="kit_id"))
    private final Set<Kit> serviceKits = new TreeSet();

}
