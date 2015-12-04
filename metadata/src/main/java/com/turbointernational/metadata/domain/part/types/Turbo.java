package com.turbointernational.metadata.domain.part.types;

import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.domain.car.CarModelEngineYear;
import com.turbointernational.metadata.domain.other.TurboModel;
import com.turbointernational.metadata.domain.part.Part;
import com.turbointernational.metadata.domain.type.CoolType;
import com.turbointernational.metadata.web.View;
import flexjson.JSONSerializer;
import org.apache.commons.lang.ObjectUtils;
import org.springframework.beans.factory.annotation.Configurable;

import javax.persistence.*;
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
import javax.persistence.Table;
import org.apache.commons.lang.ObjectUtils;

@Cacheable
@Entity
@Table(name="turbo")
@PrimaryKeyJoinColumn(name = "part_id")
public class Turbo extends Part {

    @JsonView(View.Detail.class)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="turbo_model_id")
    private TurboModel turboModel;

    @JsonView(View.Detail.class)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="cool_type_id")
    private CoolType coolType;
    
    @JsonView(View.Detail.class)
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
