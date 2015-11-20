package com.turbointernational.metadata.domain.type;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import org.springframework.beans.factory.annotation.Configurable;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Cacheable
@Configurable
@Entity
@Table(name = "cool_type")
public class CoolType {

    //<editor-fold defaultstate="collapsed" desc="properties">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable=false)
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
    
    //<editor-fold defaultstate="collapsed" desc="activerecord">
    @PersistenceContext
    transient EntityManager entityManager;
    
    public static final EntityManager entityManager() {
        EntityManager em = new CoolType().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
    
    public static List<CoolType> findAllCoolTypes() {
        return entityManager().createQuery("SELECT o FROM CoolType o", CoolType.class).getResultList();
    }
    
    public static CoolType findCoolType(Long id) {
        if (id == null) return null;
        return entityManager().find(CoolType.class, id);
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="json">
    public String toJson() {
        return new JSONSerializer().exclude("*.class").serialize(this);
    }
    
    public String toJson(String[] fields) {
        return new JSONSerializer().include(fields).exclude("*.class").serialize(this);
    }
    
    public static CoolType fromJsonToCoolType(String json) {
        return new JSONDeserializer<CoolType>().use(null, CoolType.class).deserialize(json);
    }
    
    public static String toJsonArray(Collection<CoolType> collection) {
        return new JSONSerializer().exclude("*.class").serialize(collection);
    }
    
    public static String toJsonArray(Collection<CoolType> collection, String[] fields) {
        return new JSONSerializer().include(fields).exclude("*.class").serialize(collection);
    }
    
    public static Collection<CoolType> fromJsonArrayToCoolTypes(String json) {
        return new JSONDeserializer<List<CoolType>>().use(null, ArrayList.class).use("values", CoolType.class).deserialize(json);
    }
    //</editor-fold>
}
