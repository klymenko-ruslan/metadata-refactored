package com.turbointernational.metadata.entity;

import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.service.SearchableEntity;
import com.turbointernational.metadata.service.SearchService;
import com.turbointernational.metadata.util.View;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Cacheable
@Entity
@Table(name = "car_model", uniqueConstraints = @UniqueConstraint(columnNames = {"name", "car_make_id"}))
@NamedQueries({
        @NamedQuery(name = "findCarModelsOfMake",
                query = "SELECT cm FROM CarModel AS cm WHERE cm.make.id=:makeId ORDER BY cm.name"),
        @NamedQuery(name = "findCarModelsByFilter",
                query = "SELECT cm FROM CarModel AS cm WHERE cm.name=:name AND cm.make.id=:makeId"),
        @NamedQuery(name = "findCarModelsByFilter2",
                query = "SELECT cm FROM CarModel AS cm WHERE cm.id!=:id AND cm.name=:name AND cm.make.id=:makeId")
})
public class CarModel implements Serializable, SearchableEntity {

    private final static Logger log = LoggerFactory.getLogger(CarModel.class);

    //<editor-fold defaultstate="collapsed" desc="Properties">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(View.Summary.class)
    private Long id;

    @Column(nullable = false)
    @JsonView(View.Summary.class)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "car_make_id", nullable = true)
    @JsonView(View.CarModelDetailed.class)
    private CarMake make;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "model", cascade = CascadeType.ALL)
    private List<CarModelEngineYear> carModelEngineYears;


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

    public CarMake getMake() {
        return make;
    }

    public void setMake(CarMake make) {
        this.make = make;
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
                .include("name")
                .include("make.name")
                .exclude("make.*")
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

    public static CarModel fromJsonToCarModel(String json) {
        return new JSONDeserializer<CarModel>().use(null, CarModel.class).deserialize(json);
    }

    public static String toJsonArray(Collection<CarModel> collection) {
        return new JSONSerializer().exclude("*.class").serialize(collection);
    }

    public static String toJsonArray(Collection<CarModel> collection, String[] fields) {
        return new JSONSerializer().include(fields).exclude("*.class").serialize(collection);
    }

    public static Collection<CarModel> fromJsonArrayToCarModels(String json) {
        return new JSONDeserializer<List<CarModel>>().use(null, ArrayList.class).use("values", CarModel.class).deserialize(json);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Lifecycle">
    @PostRemove
    @Override
    public void removeSearchIndex() throws Exception {
        log.info("Removing from search index.");
        SearchService.instance().deleteCarModel(this);
    }

    @PostUpdate
    @PostPersist
    @Override
    public void updateSearchIndex() throws Exception {
        log.info("Updating search index.");
        SearchService.instance().indexCarModel(this);
    }

    @Override
    public void beforeIndexing() {
        // Nothing.
    }
    //</editor-fold>
}
