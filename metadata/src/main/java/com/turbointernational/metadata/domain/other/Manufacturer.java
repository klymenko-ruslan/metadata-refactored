package com.turbointernational.metadata.domain.other;

import com.googlecode.ehcache.annotations.TriggersRemove;
import com.turbointernational.metadata.domain.type.ManufacturerType;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Cacheable
@Configurable
@Entity
@Table(name="MANFR", uniqueConstraints=@UniqueConstraint(columnNames={"name"}))
public class Manufacturer implements Serializable {
    
    public static final Long TI_ID = 11L;
    
    //<editor-fold defaultstate="collapsed" desc="Properties">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable=false)
    private String name;
    
    @OneToOne
    @JoinColumn(name="manfr_type_id", nullable=false)
    private ManufacturerType type;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="parent_manfr_id")
    private Manufacturer parent;
    
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
    
    public ManufacturerType getType() {
        return type;
    }
    
    public void setType(ManufacturerType type) {
        this.type = type;
    }
    
    public Manufacturer getParent() {
        return parent;
    }
    
    public void setParent(Manufacturer parent) {
        this.parent = parent;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="ActiveRecord">
    @PersistenceContext
    transient EntityManager entityManager;
    
    public static final EntityManager entityManager() {
        EntityManager em = new Manufacturer().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
    
    @com.googlecode.ehcache.annotations.Cacheable(cacheName = "manufacturers")
    public static List<Manufacturer> findAllManufacturers() {
        return entityManager().createQuery("SELECT o FROM Manufacturer o", Manufacturer.class).getResultList();
    }
    
    @com.googlecode.ehcache.annotations.Cacheable(cacheName = "manufacturers")
    public static Manufacturer findManufacturer(Long id) {
        if (id == null) return null;
        return entityManager().find(Manufacturer.class, id);
    }
    
    @com.googlecode.ehcache.annotations.Cacheable(cacheName = "manufacturers")
    public static Manufacturer TI() {
        return findManufacturer(TI_ID);
    }
    
    @TriggersRemove(cacheName = "manufacturers")
    @Transactional
    public void flush() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.flush();
    }
    
    @Transactional
    public void clear() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.clear();
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Serialization">
    public String toJson() {
        return new JSONSerializer().exclude("*.class").serialize(this);
    }
    
    public String toJson(String[] fields) {
        return new JSONSerializer().include(fields).exclude("*.class").serialize(this);
    }
    
    public static Manufacturer fromJsonToManufacturer(String json) {
        return new JSONDeserializer<Manufacturer>().use(null, Manufacturer.class).deserialize(json);
    }
    
    public static String toJsonArray(Collection<Manufacturer> collection) {
        return new JSONSerializer().exclude("*.class").serialize(collection);
    }
    
    public static String toJsonArray(Collection<Manufacturer> collection, String[] fields) {
        return new JSONSerializer().include(fields).exclude("*.class").serialize(collection);
    }
    
    public static Collection<Manufacturer> fromJsonArrayToManufacturers(String json) {
        return new JSONDeserializer<List<Manufacturer>>().use(null, ArrayList.class).use("values", Manufacturer.class).deserialize(json);
    }
    //</editor-fold>
    
}
