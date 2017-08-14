package com.turbointernational.metadata.entity;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.service.SearchService;
import com.turbointernational.metadata.service.SearchableEntity;
import com.turbointernational.metadata.util.View;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

@Cacheable
@Entity
@Table(name = "car_make", uniqueConstraints = @UniqueConstraint(columnNames = {"name"}))
@NamedQueries({
        @NamedQuery(name = "findCarMakeByName", query = "SELECT o FROM CarMake o WHERE o.name=:name"),
        @NamedQuery(name = "findAllCarMakeOrderedByName", query = "SELECT o FROM CarMake o ORDER BY o.name")
})
public class CarMake implements Serializable, SearchableEntity {

    private static final long serialVersionUID = -9135175861170947324L;

    private final static Logger log = LoggerFactory.getLogger(CarMake.class);

    //<editor-fold defaultstate="collapsed" desc="Properties">
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @JsonView(View.Summary.class)
    private Long id;

    @Column(nullable = false)
    @JsonView(View.Summary.class)
    private String name;

    @OneToMany(fetch = LAZY, mappedBy = "make", cascade = ALL)
    @JsonIgnore
    private List<CarModel> carModels;

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

    public List<CarModel> getCarModels() {
        return carModels;
    }

    public void setCarModels(List<CarModel> carModels) {
        this.carModels = carModels;
    }

    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Serialization">

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

    //<editor-fold defaultstate="collapsed" desc="Lifecycle">
    @PostRemove
    @Override
    public void removeSearchIndex() throws Exception {
        log.info("Removing from search index.");
        SearchService.instance().deleteCarMake(this);
    }

    @PostUpdate
    @PostPersist
    @Override
    public void updateSearchIndex() throws Exception {
        log.info("Updating search index.");
        SearchService.instance().indexCarMake(this);
    }

    @Override
    public void beforeIndexing() {
        // Nothing.
    }

    //</editor-fold>

}
