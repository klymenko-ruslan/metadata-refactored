package com.turbointernational.metadata.domain.car;

import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.domain.SearchableEntity;
import com.turbointernational.metadata.util.CarMakeElasticSearch;
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

@Cacheable
@Entity
@Table(name = "car_make", uniqueConstraints = @UniqueConstraint(columnNames = {"name"}))
@NamedQueries({
        @NamedQuery(name = "findCarMakeByName", query = "FROM CarMake WHERE name=:name"),
        @NamedQuery(name = "findAllCarMakeOrderedByName", query = "FROM CarMake ORDER BY name")
})
public class CarMake implements Serializable, SearchableEntity {

    private final static Logger log = LoggerFactory.getLogger(CarMake.class);

    //<editor-fold defaultstate="collapsed" desc="Properties">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(View.Summary.class)
    private Long id;

    @Column(nullable = false)
    @JsonView(View.Summary.class)
    private String name;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "make", cascade = CascadeType.ALL)
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

    //<editor-fold defaultstate="collapsed" desc="Lifecycle">
    @PostRemove
    @Override
    public void removeSearchIndex() throws Exception {
        log.info("Removing from search index.");
        CarMakeElasticSearch.instance().delete(this);
    }

    @PostUpdate
    @PostPersist
    @Override
    public void updateSearchIndex() throws Exception {
        log.info("Updating search index.");
        CarMakeElasticSearch.instance().index(this);
    }

    //</editor-fold>

}
