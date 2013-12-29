package com.turbointernational.metadata.domain.part.types;
import com.turbointernational.metadata.domain.car.CarModelEngineYear;
import com.turbointernational.metadata.domain.type.CoolType;
import com.turbointernational.metadata.domain.other.TurboModel;
import com.turbointernational.metadata.domain.part.Part;
import flexjson.JSONSerializer;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.SecondaryTable;
import net.sf.jsog.JSOG;
import org.apache.commons.lang.ObjectUtils;
import org.springframework.beans.factory.annotation.Configurable;

@Cacheable
@Configurable
@Entity
@SecondaryTable(name="turbo", pkJoinColumns=@PrimaryKeyJoinColumn(name = "part_id"))
public class Turbo extends Part {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="turbo_model_id", table = "turbo")
    private TurboModel turboModel;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="cool_type_id", table = "turbo")
    private CoolType coolType;
    
    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable(name="turbo_car_model_engine_year",
            joinColumns=@JoinColumn(name="part_id"),
            inverseJoinColumns=@JoinColumn(name="car_model_engine_year_id"))
    private Set<CarModelEngineYear> cars = new HashSet<CarModelEngineYear>();

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
        
        if (getTurboModel() != null) {
            columns.put("turbo_model_name", ObjectUtils.toString(getTurboModel().getName()));
        }

        if (getCoolType() != null) {
            columns.put("cool_type", ObjectUtils.toString(getCoolType().getName()));
        }
    }
}
