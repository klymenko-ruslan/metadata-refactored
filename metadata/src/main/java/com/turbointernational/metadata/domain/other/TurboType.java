package com.turbointernational.metadata.domain.other;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Configurable
@Entity
@Table(name="TURBO_TYPE", uniqueConstraints=@UniqueConstraint(columnNames={"name"}))
public class TurboType implements Comparable<TurboType>{
    
    //<editor-fold defaultstate="collapsed" desc="Properties">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne
    @JoinColumn(name="manfr_id", nullable=false)
    private Manufacturer manufacturer;
    
    @Column(nullable=false)
    private String name;
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Manufacturer getManufacturer() {
        return manufacturer;
    }
    
    public void setManufacturer(Manufacturer manufacturer) {
        this.manufacturer = manufacturer;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Serialization">
    
    public String toJson() {
        return new JSONSerializer()
                .exclude("*.class").serialize(this);
    }
    
    public String toJson(String[] fields) {
        return new JSONSerializer()
                .include(fields)
                .exclude("*.class").serialize(this);
    }
    
    public static TurboType fromJsonToTurboType(String json) {
        return new JSONDeserializer<TurboType>().use(null, TurboType.class).deserialize(json);
    }
    
    public static String toJsonArray(Collection<TurboType> collection) {
        return new JSONSerializer()
                .include("manufacturer.id")
                .exclude("manufacturer.*")
                .exclude("*.class")
                .serialize(collection);
    }
    
    public static String toJsonArray(Collection<TurboType> collection, String[] fields) {
        return new JSONSerializer()
                .include(fields)
                .exclude("*.class")
                .serialize(collection);
    }
    
    public static Collection<TurboType> fromJsonArrayToTurboTypes(String json) {
        return new JSONDeserializer<List<TurboType>>().use(null, ArrayList.class).use("values", TurboType.class).deserialize(json);
    }
    
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="ActiveRecord">
    
    @PersistenceContext
    transient EntityManager entityManager;
    
    public static final EntityManager entityManager() {
        EntityManager em = new TurboType().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
    
    public static TurboType findTurboType(Long id) {
        if (id == null) return null;
        return entityManager().find(TurboType.class, id);
    }
    
    public static List<TurboType> findTurboTypesByManufacturerId(Long manufacturerId) {
        return entityManager().createQuery(
              "SELECT o\n"
            + "FROM\n"
            + "  TurboType o\n"
            + "  JOIN o.manufacturer\n"
            + "WHERE o.manufacturer.id = :manufacturerId\n"
            + "ORDER BY o.name",
            TurboType.class)
            .setParameter("manufacturerId", manufacturerId)
            .getResultList();
    }
    
    @Transactional
    public void persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }
    
    @Transactional
    public TurboType merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        TurboType merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
    @Transactional
    public void remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            TurboType attached = TurboType.findTurboType(this.id);
            this.entityManager.remove(attached);
        }
    }
    //</editor-fold>
    
    @Override
    public int compareTo(TurboType o) {
        return this.name.compareTo(o.getName());
    }
}
