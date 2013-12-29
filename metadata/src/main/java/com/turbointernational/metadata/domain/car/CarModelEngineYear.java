package com.turbointernational.metadata.domain.car;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import org.springframework.beans.factory.annotation.Configurable;

@Cacheable
@Configurable
@Entity
@Table(name="car_model_engine_year")
public class CarModelEngineYear {
    
    //<editor-fold defaultstate="collapsed" desc="Properties">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne
    @JoinColumn(name="car_model_id")
    private CarModel model;
    
    @OneToOne
    @JoinColumn(name="car_engine_id")
    private CarEngine engine;
    
    @OneToOne
    @JoinColumn(name="car_year_id")
    private CarYear year;
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }

    public CarModel getModel() {
        return model;
    }

    public void setModel(CarModel model) {
        this.model = model;
    }
    
    public CarEngine getEngine() {
        return engine;
    }

    public void setEngine(CarEngine engine) {
        this.engine = engine;
    }

    public CarYear getYear() {
        return year;
    }

    public void setYear(CarYear year) {
        this.year = year;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Serialization">
    public String toJson() {
        return new JSONSerializer().exclude("*.class").serialize(this);
    }
    
    public String toJson(String[] fields) {
        return new JSONSerializer().include(fields).exclude("*.class").serialize(this);
    }
    
    public static CarModelEngineYear fromJsonToCarFuelType(String json) {
        return new JSONDeserializer<CarModelEngineYear>().use(null, CarModelEngineYear.class).deserialize(json);
    }
    
    public static String toJsonArray(Collection<CarModelEngineYear> collection) {
        return new JSONSerializer().exclude("*.class").serialize(collection);
    }
    
    public static String toJsonArray(Collection<CarModelEngineYear> collection, String[] fields) {
        return new JSONSerializer().include(fields).exclude("*.class").serialize(collection);
    }
    
    public static Collection<CarModelEngineYear> fromJsonArrayToCarFuelTypes(String json) {
        return new JSONDeserializer<List<CarModelEngineYear>>().use(null, ArrayList.class).use("values", CarModelEngineYear.class).deserialize(json);
    }
    //</editor-fold>
    
}
