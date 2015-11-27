package com.turbointernational.metadata.domain.part.types;

import com.turbointernational.metadata.domain.car.CarModelEngineYear;
import com.turbointernational.metadata.domain.other.TurboModel;
import com.turbointernational.metadata.domain.part.Part;
import com.turbointernational.metadata.domain.type.CoolType;
import flexjson.JSONSerializer;
import org.apache.commons.lang.ObjectUtils;
import org.springframework.beans.factory.annotation.Configurable;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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
    
//    @OneToMany(fetch = FetchType.LAZY)
//    @JoinTable(name="turbo_car_model_engine_year",
//            joinColumns=@JoinColumn(name="part_id"),
//            inverseJoinColumns=@JoinColumn(name="car_model_engine_year_id"))
//    private Set<TurboCarModelEngineYear> cars = new HashSet<TurboCarModelEngineYear>();
    
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

//    public Set<TurboCarModelEngineYear> getCars() {
//        return cars;
//    }
//
//    public void setCars(Set<TurboCarModelEngineYear> cars) {
//        this.cars = cars;
//    }
    
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
    public void csvColumns(Map<String, String> columns) {
        super.csvColumns(columns);

        if (getTurboModel() != null) {
            columns.put("turbo_type", ObjectUtils.toString(getTurboModel().getTurboType().getName()));
            columns.put("turbo_model", ObjectUtils.toString(getTurboModel().getName()));
        }

        if (getCoolType() != null) {
            columns.put("cool_type", ObjectUtils.toString(getCoolType().getName()));
        }
    }
}
