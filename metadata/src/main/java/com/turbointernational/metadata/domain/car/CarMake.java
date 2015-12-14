package com.turbointernational.metadata.domain.car;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.*;

@Cacheable
@Entity
@Table(name = "car_make", uniqueConstraints = @UniqueConstraint(columnNames = {"name"}))
@NamedQueries(
        @NamedQuery(name = "findCarMakeByName", query = "FROM CarMake WHERE name=:name")
)
public class CarMake implements Serializable {

    //<editor-fold defaultstate="collapsed" desc="Properties">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
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

    protected JSONSerializer getSearchSerializer() {
        return new JSONSerializer()
                .include("id")
                .include("name")
                .exclude("*.class");
    }

    public String toSearchJson() {
        return getSearchSerializer().exclude("*").serialize(this);
    }

    public String toJson() {
        return new JSONSerializer().exclude("*.class").serialize(this);
    }

    public String toJson(String[] fields) {
        return new JSONSerializer().include(fields).exclude("*.class").serialize(this);
    }

    public static CarMake fromJsonToCarMake(String json) {
        return new JSONDeserializer<CarMake>().use(null, CarMake.class).deserialize(json);
    }

    public static String toJsonArray(Collection<CarMake> collection) {
        return new JSONSerializer().exclude("*.class").serialize(collection);
    }

    public static String toJsonArray(Collection<CarMake> collection, String[] fields) {
        return new JSONSerializer().include(fields).exclude("*.class").serialize(collection);
    }

    public static Collection<CarMake> fromJsonArrayToCarMakes(String json) {
        return new JSONDeserializer<List<CarMake>>().use(null, ArrayList.class).use("values", CarMake.class).deserialize(json);
    }
    //</editor-fold>

}
