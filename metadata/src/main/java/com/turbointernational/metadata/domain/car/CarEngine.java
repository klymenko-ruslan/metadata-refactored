package com.turbointernational.metadata.domain.car;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.io.Serializable;
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

@Cacheable
@Entity
@Table(name="car_engine", uniqueConstraints=@UniqueConstraint(columnNames={"engineSize", "car_fuel_type_id"}))
public class CarEngine implements Serializable {
    
    //<editor-fold defaultstate="collapsed" desc="Properties">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable=false)
    private String engineSize;
    
    @OneToOne
    @JoinColumn(name="car_fuel_type_id")
    private CarFuelType fuelType;
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }

    public String getEngineSize() {
        return engineSize;
    }

    public void setEngineSize(String engineSize) {
        this.engineSize = engineSize;
    }

    public CarFuelType getFuelType() {
        return fuelType;
    }

    public void setFuelType(CarFuelType fuelType) {
        this.fuelType = fuelType;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Serialization">
    public String toJson() {
        return new JSONSerializer().exclude("*.class").serialize(this);
    }
    
    public String toJson(String[] fields) {
        return new JSONSerializer().include(fields).exclude("*.class").serialize(this);
    }
    
    public static CarEngine fromJsonToCarFuelType(String json) {
        return new JSONDeserializer<CarEngine>().use(null, CarEngine.class).deserialize(json);
    }
    
    public static String toJsonArray(Collection<CarEngine> collection) {
        return new JSONSerializer().exclude("*.class").serialize(collection);
    }
    
    public static String toJsonArray(Collection<CarEngine> collection, String[] fields) {
        return new JSONSerializer().include(fields).exclude("*.class").serialize(collection);
    }
    
    public static Collection<CarEngine> fromJsonArrayToCarFuelTypes(String json) {
        return new JSONDeserializer<List<CarEngine>>().use(null, ArrayList.class).use("values", CarEngine.class).deserialize(json);
    }
    //</editor-fold>
    
}
