package com.turbointernational.metadata.domain.car;

import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.domain.SearchableEntity;
import com.turbointernational.metadata.domain.criticaldimension.CriticalDimension;
import com.turbointernational.metadata.services.SearchService;
import com.turbointernational.metadata.web.View;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Cacheable
@Entity
@Table(name="car_engine", uniqueConstraints=@UniqueConstraint(columnNames={"engine_size", "car_fuel_type_id"}))
@NamedQueries({
        @NamedQuery(name="findAllCarEngineOrderedByName", query = "FROM CarEngine ORDER BY engineSize")
})
public class CarEngine implements Serializable, SearchableEntity {

    private final static Logger log = LoggerFactory.getLogger(CarEngine.class);
    
    //<editor-fold defaultstate="collapsed" desc="Properties">
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @JsonView(View.Summary.class)
    private Long id;
    
    @Column(name="engine_size", nullable=false)
    @JsonView(View.Summary.class)
    private String engineSize;
    
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name="car_fuel_type_id")
    @JsonView(View.CarEngineDetailed.class)
    private CarFuelType fuelType;

    @OneToMany(fetch = LAZY, mappedBy = "engine", cascade = CascadeType.ALL)
    private List<CarModelEngineYear> carModelEngineYears;

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


    public List<CarModelEngineYear> getCarModelEngineYears() {
        return carModelEngineYears;
    }

    public void setCarModelEngineYears(List<CarModelEngineYear> carModelEngineYears) {
        this.carModelEngineYears = carModelEngineYears;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Serialization">
    protected JSONSerializer getSearchSerializer() {
        return new JSONSerializer()
                .include("id")
                .include("engineSize")
                .include("fuelType.name")
                .exclude("fuelType.*")
                .exclude("*.class");
    }

    @Override
    public String toSearchJson(List<CriticalDimension> criticalDimensions) {
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

    //<editor-fold defaultstate="collapsed" desc="Lifecycle">
    @PostRemove
    @Override
    public void removeSearchIndex() throws Exception {
        log.info("Removing from search index.");
        SearchService.instance().deleteCarEngine(this);
    }

    @PostUpdate
    @PostPersist
    @Override
    public void updateSearchIndex() throws Exception {
        log.info("Updating search index.");
        SearchService.instance().indexCarEngine(this);
    }

    @Override
    public void beforeIndexing() {
        // Nothing.
    }
    //</editor-fold>

}
