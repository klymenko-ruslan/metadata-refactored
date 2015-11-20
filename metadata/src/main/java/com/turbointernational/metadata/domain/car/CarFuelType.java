package com.turbointernational.metadata.domain.car;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import org.springframework.beans.factory.annotation.Configurable;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Cacheable
@Configurable
@Entity
@Table(name="car_fuel_type", uniqueConstraints=@UniqueConstraint(columnNames={"name"}))
public class CarFuelType {
    
    //<editor-fold defaultstate="collapsed" desc="Properties">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable=false)
    private String name;
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Serialization">
    public String toJson() {
        return new JSONSerializer().exclude("*.class").serialize(this);
    }
    
    public String toJson(String[] fields) {
        return new JSONSerializer().include(fields).exclude("*.class").serialize(this);
    }
    
    public static CarFuelType fromJsonToCarFuelType(String json) {
        return new JSONDeserializer<CarFuelType>().use(null, CarFuelType.class).deserialize(json);
    }
    
    public static String toJsonArray(Collection<CarFuelType> collection) {
        return new JSONSerializer().exclude("*.class").serialize(collection);
    }
    
    public static String toJsonArray(Collection<CarFuelType> collection, String[] fields) {
        return new JSONSerializer().include(fields).exclude("*.class").serialize(collection);
    }
    
    public static Collection<CarFuelType> fromJsonArrayToCarFuelTypes(String json) {
        return new JSONDeserializer<List<CarFuelType>>().use(null, ArrayList.class).use("values", CarFuelType.class).deserialize(json);
    }
    //</editor-fold>
    
}
