package com.turbointernational.metadata.domain.car;

import com.turbointernational.metadata.domain.SearchableEntity;
import com.turbointernational.metadata.util.CarModelElasticSearch;
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
@Table(name="car_model", uniqueConstraints=@UniqueConstraint(columnNames={"name", "car_make_id"}))
public class CarModel implements Serializable, SearchableEntity {

    private final static Logger log = LoggerFactory.getLogger(CarModel.class);
    
    //<editor-fold defaultstate="collapsed" desc="Properties">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable=false)
    private String name;
    
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="car_make_id", nullable = true)
    private CarMake make;
    
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

    public String toSearchJson() {
        return getSearchSerializer().exclude("*").serialize(this);
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
    @PreRemove
    @Override
    public void removeSearchIndex() throws Exception {
        log.info("Removing from search index.");
        CarModelElasticSearch.instance().delete(this);
    }

    @PostUpdate
    @PostPersist
    @Override
    public void updateSearchIndex() throws Exception {
        log.info("Updating search index.");
        CarModelElasticSearch.instance().index(this);
    }
    //</editor-fold>
}
