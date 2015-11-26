package com.turbointernational.metadata.domain.car;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Cacheable
@Configurable
@Entity
@Table(name="car_model_engine_year")
public class CarModelEngineYear {

    //<editor-fold defaultstate="collapsed" desc="Properties">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="car_model_id", nullable = true)
    private CarModel model;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="car_engine_id", nullable = true)
    private CarEngine engine;

    @PersistenceContext
    @Transient
    private EntityManager entityManager;

    public static final EntityManager entityManager() {
        EntityManager em = new CarModelEngineYear().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected " +
                "(is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="car_year_id", nullable = true)
    private CarYear year;
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }

    public CarModel getModel() {
        return model;
    }

    public void setModel(CarModel model) {
        this.model = model;
    }
    
    public CarEngine getEngine() {
        return engine;
    }

    public void setEngine(CarEngine engine) {
        this.engine = engine;
    }

    public CarYear getYear() {
        return year;
    }

    public void setYear(CarYear year) {
        this.year = year;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Serialization">
    public String toJson() {
        return new JSONSerializer().exclude("*.class").serialize(this);
    }
    
    public String toJson(String[] fields) {
        return new JSONSerializer().include(fields).exclude("*.class").serialize(this);
    }

    public static CarModelEngineYear findById(Long id) {
        return entityManager().find(CarModelEngineYear.class, id);
    }

    public static List<CarModelEngineYear> findApplications(List<Long> ids) {
        List<CarModelEngineYear> retVal = entityManager().createQuery(
                "SELECT cmey FROM CarModelEngineYear cmey WHERE id IN(:ids)",
                CarModelEngineYear.class).setParameter("ids", ids)
                .getResultList();
        return retVal;
    }
    
    public static CarModelEngineYear fromJsonToCarFuelType(String json) {
        return new JSONDeserializer<CarModelEngineYear>().use(null, CarModelEngineYear.class).deserialize(json);
    }
    
    public static String toJsonArray(Collection<CarModelEngineYear> collection) {
        return new JSONSerializer().exclude("*.class").serialize(collection);
    }
    
    public static String toJsonArray(Collection<CarModelEngineYear> collection, String[] fields) {
        return new JSONSerializer().include(fields).exclude("*.class").serialize(collection);
    }
    
    public static Collection<CarModelEngineYear> fromJsonArrayToCarFuelTypes(String json) {
        return new JSONDeserializer<List<CarModelEngineYear>>().use(null, ArrayList.class).use("values", CarModelEngineYear.class).deserialize(json);
    }
    //</editor-fold>
    
}
