package com.turbointernational.metadata.domain.type;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PersistenceContext;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

@Cacheable
@Configurable
@Entity
@Table(name="MANFR_TYPE",
       uniqueConstraints=@UniqueConstraint(columnNames={"name"}))
public class ManufacturerType {

    //<editor-fold defaultstate="collapsed" desc="properties">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable=false)
    private String name;
    
    @Version
    @Column(name = "version")
    private Integer version;
    
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
    
    public Integer getVersion() {
        return this.version;
    }
    
    public void setVersion(Integer version) {
        this.version = version;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="activerecords">
    @PersistenceContext
    transient EntityManager entityManager;
    
    public static final EntityManager entityManager() {
        EntityManager em = new ManufacturerType().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
    
    public static long countManufacturerTypes() {
        return entityManager().createQuery("SELECT COUNT(o) FROM ManufacturerType o", Long.class).getSingleResult();
    }
    
    public static List<ManufacturerType> findAllManufacturerTypes() {
        return entityManager().createQuery("SELECT o FROM ManufacturerType o", ManufacturerType.class).getResultList();
    }
    
    public static ManufacturerType findManufacturerType(Long id) {
        if (id == null) return null;
        return entityManager().find(ManufacturerType.class, id);
    }
    
    public static List<ManufacturerType> findManufacturerTypeEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM ManufacturerType o", ManufacturerType.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    @Transactional
    public void persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }
    
    @Transactional
    public void remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            ManufacturerType attached = findManufacturerType(this.id);
            this.entityManager.remove(attached);
        }
    }
    
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
    
    @Transactional
    public ManufacturerType merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        ManufacturerType merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="json">
    public String toJson() {
        return new JSONSerializer().exclude("*.class").serialize(this);
    }
    
    public String toJson(String[] fields) {
        return new JSONSerializer().include(fields).exclude("*.class").serialize(this);
    }
    
    public static ManufacturerType fromJsonToManufacturerType(String json) {
        return new JSONDeserializer<ManufacturerType>().use(null, ManufacturerType.class).deserialize(json);
    }
    
    public static String toJsonArray(Collection<ManufacturerType> collection) {
        return new JSONSerializer().exclude("*.class").serialize(collection);
    }
    
    public static String toJsonArray(Collection<ManufacturerType> collection, String[] fields) {
        return new JSONSerializer().include(fields).exclude("*.class").serialize(collection);
    }
    
    public static Collection<ManufacturerType> fromJsonArrayToManufacturerTypes(String json) {
        return new JSONDeserializer<List<ManufacturerType>>().use(null, ArrayList.class).use("values", ManufacturerType.class).deserialize(json);
    }
    //</editor-fold>
}
