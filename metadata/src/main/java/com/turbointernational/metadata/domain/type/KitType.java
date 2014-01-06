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
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

@Cacheable
@Configurable
@Entity
@Table(name="KIT_TYPE", uniqueConstraints=@UniqueConstraint(columnNames={"name"}))
public class KitType {

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
        EntityManager em = new KitType().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
    
    public static long countKitTypes() {
        return entityManager().createQuery("SELECT COUNT(o) FROM KitType o", Long.class).getSingleResult();
    }
    
    public static List<KitType> findAllKitTypes() {
        return entityManager().createQuery("SELECT o FROM KitType o", KitType.class).getResultList();
    }
    
    public static KitType findKitType(Long id) {
        if (id == null) return null;
        return entityManager().find(KitType.class, id);
    }
    
    public static List<KitType> findKitTypeEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM KitType o", KitType.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
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
            KitType attached = findKitType(this.id);
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
    public KitType merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        KitType merged = this.entityManager.merge(this);
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
    
    public static KitType fromJsonToKitType(String json) {
        return new JSONDeserializer<KitType>().use(null, KitType.class).deserialize(json);
    }
    
    public static String toJsonArray(Collection<KitType> collection) {
        return new JSONSerializer().exclude("*.class").serialize(collection);
    }
    
    public static String toJsonArray(Collection<KitType> collection, String[] fields) {
        return new JSONSerializer().include(fields).exclude("*.class").serialize(collection);
    }
    
    public static Collection<KitType> fromJsonArrayToKitTypes(String json) {
        return new JSONDeserializer<List<KitType>>().use(null, ArrayList.class).use("values", KitType.class).deserialize(json);
    }
    //</editor-fold>
}
