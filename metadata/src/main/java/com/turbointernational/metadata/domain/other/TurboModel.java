package com.turbointernational.metadata.domain.other;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.PersistenceContext;
import javax.persistence.Table;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

@Cacheable
@Configurable
@Entity
@Table(name = "TURBO_MODEL")
public class TurboModel {
    
    //<editor-fold defaultstate="collapsed" desc="Properties">
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable=false)
    private String name;
    
    @OneToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name="turbo_type_id")
    private TurboType turboType;
    
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
    
    public TurboType getTurboType() {
        return turboType;
    }
    
    public void setTurboType(TurboType turboType) {
        this.turboType = turboType;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Serialization">
    
    public String toJson() {
        return new JSONSerializer().exclude("*.class").serialize(this);
    }
    
    public String toJson(String[] fields) {
        return new JSONSerializer().include(fields).exclude("*.class").serialize(this);
    }
    
    public static TurboModel fromJsonToTurboModel(String json) {
        return new JSONDeserializer<TurboModel>().use(null, TurboModel.class).deserialize(json);
    }
    
    public static String toJsonArray(Collection<TurboModel> collection) {
        return new JSONSerializer()
                .include("id")
                .include("name")
                .include("version")
                .include("turboType.id")
                .include("turboType.version")
                .exclude("*")
                .serialize(collection);
    }
    
    public static String toJsonArray(Collection<TurboModel> collection, String[] fields) {
        return new JSONSerializer().include(fields).exclude("*.class").serialize(collection);
    }
    
    public static Collection<TurboModel> fromJsonArrayToTurboModels(String json) {
        return new JSONDeserializer<List<TurboModel>>().use(null, ArrayList.class).use("values", TurboModel.class).deserialize(json);
    }
    
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="ActiveRecord">
    
    @PersistenceContext
    transient EntityManager entityManager;
    
    public static final EntityManager entityManager() {
        EntityManager em = new TurboModel().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
    
    public static List<TurboModel> findTurboModelsByTurboTypeId(Long turboTypeId) {
        return entityManager().createQuery(
              "SELECT o\n"
            + "FROM\n"
            + "  TurboModel o\n"
            + "  JOIN o.turboType\n"
            + "WHERE o.turboType.id = :turboTypeId",
            TurboModel.class)
            .setParameter("turboTypeId", turboTypeId)
            .getResultList();
    }
    
    public static TurboModel findTurboModel(Long id) {
        if (id == null) return null;
        return entityManager().find(TurboModel.class, id);
    }
    //</editor-fold>
    
}
