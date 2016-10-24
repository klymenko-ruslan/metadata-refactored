package com.turbointernational.metadata.domain.car;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.domain.SearchableEntity;
import com.turbointernational.metadata.domain.criticaldimension.CriticalDimension;
import com.turbointernational.metadata.domain.part.bom.BOMItemDao;
import com.turbointernational.metadata.domain.part.types.TurboCarModelEngineYearDao;
import com.turbointernational.metadata.services.SearchService;
import com.turbointernational.metadata.web.View;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.*;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Cacheable
@Entity
@Table(name = "car_fuel_type", uniqueConstraints = @UniqueConstraint(columnNames = {"name"}))
@NamedQueries({
        @NamedQuery(name = "findCarFuelTypeByName", query = "FROM CarFuelType WHERE name=:name"),
        @NamedQuery(name = "findAllCarFuelTypeOrderedByName", query = "FROM CarFuelType ORDER BY name")
})
public class CarFuelType implements Serializable, SearchableEntity {

    private final static Logger log = LoggerFactory.getLogger(CarFuelType.class);

    //<editor-fold defaultstate="collapsed" desc="Properties">
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @JsonView({View.Summary.class})
    private Long id;

    @Column(nullable = false)
    @JsonView({View.Summary.class})
    private String name;

    @JsonIgnore
    @OneToMany(fetch = LAZY, mappedBy = "fuelType", cascade = ALL)
    private List<CarEngine> carEngines;

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

    public List<CarEngine> getCarEngines() {
        return carEngines;
    }

    public void setCarEngines(List<CarEngine> carEngines) {
        this.carEngines = carEngines;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Serialization">

    //<editor-fold defaultstate="collapsed" desc="Serialization">
    protected JSONSerializer getSearchSerializer() {
        return new JSONSerializer()
                .include("id")
                .include("name")
                .exclude("*.class");
    }

    @Override
    public String toSearchJson(List<CriticalDimension> criticalDimensions, TurboCarModelEngineYearDao tcmeyDao,
                               BOMItemDao bomItemDao) {
        return getSearchSerializer().exclude("*").serialize(this);
    }

    @Override
    public String getSearchId() {
        return getId().toString();
    }

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

    //<editor-fold defaultstate="collapsed" desc="Lifecycle">
    @PostRemove
    @Override
    public void removeSearchIndex() throws Exception {
        log.info("Removing from search index.");
        SearchService.instance().deleteCarFuelType(this);
    }

    @PostUpdate
    @PostPersist
    @Override
    public void updateSearchIndex() throws Exception {
        log.info("Updating search index.");
        SearchService.instance().indexCarFuelType(this);
    }

    //</editor-fold>

}
