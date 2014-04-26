package com.turbointernational.metadata.domain.part.types;
import com.turbointernational.metadata.domain.car.CarModelEngineYear;
import com.turbointernational.metadata.domain.type.CoolType;
import com.turbointernational.metadata.domain.other.TurboModel;
import com.turbointernational.metadata.domain.part.Part;
import flexjson.JSONSerializer;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import net.sf.jsog.JSOG;
import org.apache.commons.lang.ObjectUtils;
import org.springframework.beans.factory.annotation.Configurable;

@Cacheable
@Configurable
@Entity
@Table(name="turbo")
@PrimaryKeyJoinColumn(name = "part_id")
public class Turbo extends Part {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="turbo_model_id")
    private TurboModel turboModel;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="cool_type_id")
    private CoolType coolType;
    
    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable(name="turbo_car_model_engine_year",
            joinColumns=@JoinColumn(name="part_id"),
            inverseJoinColumns=@JoinColumn(name="car_model_engine_year_id"))
    private Set<CarModelEngineYear> cars = new HashSet<CarModelEngineYear>();
    
    /**
     * View takes care of joining by common turbo type and handling the kit_part_common_component aspect.
     */
    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable(name="vpart_turbotype_kits",
        joinColumns = @JoinColumn(name="part_id"),
        inverseJoinColumns = @JoinColumn(name="kit_id"))
    private final Set<Kit> serviceKits = new TreeSet();


    public TurboModel getTurboModel() {
        return turboModel;
    }

    public void setTurboModel(TurboModel turboModel) {
        this.turboModel = turboModel;
    }

    public CoolType getCoolType() {
        return coolType;
    }

    public void setCoolType(CoolType coolType) {
        this.coolType = coolType;
    }

    public Set<CarModelEngineYear> getCars() {
        return cars;
    }

    public void setCars(Set<CarModelEngineYear> cars) {
        this.cars = cars;
    }
    
    @Override
    protected JSONSerializer buildJSONSerializer() {
        return super.buildJSONSerializer()
            .include("coolType.id")
            .include("coolType.name")
            .include("coolType.version")
            .include("turboModel.id")
            .include("turboModel.name")
            .include("turboModel.version")
            .include("turboModel.turboType.id")
            .include("turboModel.turboType.name")
            .include("turboModel.turboType.manufacturer.id")
            .include("turboModel.turboType.manufacturer.name");
    }
    
    @Override
    protected JSONSerializer getSearchSerializer() {
        return super.getSearchSerializer()
                    .include("coolType.id")
                    .include("coolType.name")
                    .include("turboModel.id")
                    .include("turboModel.name")
                    .include("turboModel.turboType.id")
                    .include("turboModel.turboType.name");
    }

    @Override
    public JSOG toJsog() {
        JSOG partObject = super.toJsog();
        
        if (turboModel != null) {
            partObject.put("turbo_model_name", turboModel.getName());
        }

        if (getCoolType() != null) {
            partObject.put("cool_type_name", getCoolType().getName());
        }
        
        return partObject;
    }
    
    @Override
    public void csvColumns(Map<String, String> columns) {
        super.csvColumns(columns);
        
        // turbo model is handled specially.

        if (getCoolType() != null) {
            columns.put("cool_type", ObjectUtils.toString(getCoolType().getName()));
        }
        
        // Service kits
        String kits = new JSONSerializer()
            .include("id")
            .include("kitType.name")
            .include("manufacturerPartNumber")
            .include("manufacturer.name")
            .include("tiParts.id")
            .include("tiParts.manufacturerPartNumber")
            .include("tiParts.")
            .exclude("*")
            .serialize(serviceKits);
        
        columns.put("service_kits", kits);
    }
}
