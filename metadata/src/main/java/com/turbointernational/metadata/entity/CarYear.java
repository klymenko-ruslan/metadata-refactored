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
@Table(name = "car_year", uniqueConstraints = @UniqueConstraint(columnNames = {"name"}))
@NamedQueries({
        @NamedQuery(name = "findCarYearByName", query = "FROM CarYear WHERE name=:name")
})
public class CarYear implements Serializable, SearchableEntity {

    private final static Logger log = LoggerFactory.getLogger(CarYear.class);

    //<editor-fold defaultstate="collapsed" desc="Properties">
    @Id
    @JsonView(View.Summary.class)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    @JsonView(View.Summary.class)
    private String name;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "year", cascade = CascadeType.ALL)
    private List<CarModelEngineYear> carModelEngineYears;

    public CarYear() {
    }

    public CarYear(String year) {
        setName(year);
    }

    protected JSONSerializer getSearchSerializer() {
        return new JSONSerializer()
                .include("id")
                .include("name")
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

    //<editor-fold defaultstate="collapsed" desc="Lifecycle">
    @PostRemove
    @Override
    public void removeSearchIndex() throws Exception {
        log.info("Removing from search index.");
        SearchService.instance().deleteCarYear(this);
    }

    @PostUpdate
    @PostPersist
    @Override
    public void updateSearchIndex() throws Exception {
        log.info("Updating search index.");
        SearchService.instance().indexCarYear(this);
    }

    @Override
    public void beforeIndexing() {
        // Nothing.
    }

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

    public List<CarModelEngineYear> getCarModelEngineYears() {
        return carModelEngineYears;
    }

    public void setCarModelEngineYears(List<CarModelEngineYear> carModelEngineYears) {
        this.carModelEngineYears = carModelEngineYears;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Serialization">
    public String toJson() {
        return new JSONSerializer().exclude("*.class").serialize(this);
    }

    public String toJson(String[] fields) {
        return new JSONSerializer().include(fields).exclude("*.class").serialize(this);
    }

    public static CarYear fromJsonToCarYear(String json) {
        return new JSONDeserializer<CarYear>().use(null, CarYear.class).deserialize(json);
    }

    public static String toJsonArray(Collection<CarYear> collection) {
        return new JSONSerializer().exclude("*.class").serialize(collection);
    }

    public static String toJsonArray(Collection<CarYear> collection, String[] fields) {
        return new JSONSerializer().include(fields).exclude("*.class").serialize(collection);
    }

    public static Collection<CarYear> fromJsonArrayToCarYears(String json) {
        return new JSONDeserializer<List<CarYear>>().use(null, ArrayList.class).use("values", CarYear.class).deserialize(json);
    }
    //</editor-fold>


}
