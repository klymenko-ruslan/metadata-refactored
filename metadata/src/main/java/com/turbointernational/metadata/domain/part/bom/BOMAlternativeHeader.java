package com.turbointernational.metadata.domain.part.bom;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PersistenceContext;
import javax.persistence.Table;
import javax.persistence.Version;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

@Configurable
@Entity
@Table(name="BOM_ALT_HEADER")
public class BOMAlternativeHeader {
    
    //<editor-fold defaultstate="collapsed" desc="Properties">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable=false)
    private String name;
    
    private String description;
    
    @Version
    @Column(name = "version")
    private Integer version;
    
    
    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }
    
    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }
    
    /**
     * @return the name
     */
    public String getName() {
        return name;
    }
    
    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }
    
    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }
    
    public Integer getVersion() {
        return this.version;
    }
    
    public void setVersion(Integer version) {
        this.version = version;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="ActiveRecord">
    
    
    @PersistenceContext
    transient EntityManager entityManager;
    
    public static final EntityManager entityManager() {
        EntityManager em = new BOMAlternativeHeader().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
    
    public static long countBOMAlternativeHeaders() {
        return entityManager().createQuery("SELECT COUNT(o) FROM BOMAlternativeHeader o", Long.class).getSingleResult();
    }
    
    public static List<BOMAlternativeHeader> findAllBOMAlternativeHeaders() {
        return entityManager().createQuery("SELECT o FROM BOMAlternativeHeader o", BOMAlternativeHeader.class).getResultList();
    }
    
    public static BOMAlternativeHeader findBOMAlternativeHeader(Long id) {
        if (id == null) return null;
        return entityManager().find(BOMAlternativeHeader.class, id);
    }
    
    public static List<BOMAlternativeHeader> findBOMAlternativeHeaderEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM BOMAlternativeHeader o", BOMAlternativeHeader.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
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
            BOMAlternativeHeader attached = BOMAlternativeHeader.findBOMAlternativeHeader(this.id);
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
    public BOMAlternativeHeader merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        BOMAlternativeHeader merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Serialization">
    
    public String toJson() {
        return new JSONSerializer().exclude("*.class").serialize(this);
    }
    
    public String toJson(String[] fields) {
        return new JSONSerializer().include(fields).exclude("*.class").serialize(this);
    }
    
    public static BOMAlternativeHeader fromJsonToBOMAlternativeHeader(String json) {
        return new JSONDeserializer<BOMAlternativeHeader>().use(null, BOMAlternativeHeader.class).deserialize(json);
    }
    
    public static String toJsonArray(Collection<BOMAlternativeHeader> collection) {
        return new JSONSerializer().exclude("*.class").serialize(collection);
    }
    
    public static String toJsonArray(Collection<BOMAlternativeHeader> collection, String[] fields) {
        return new JSONSerializer().include(fields).exclude("*.class").serialize(collection);
    }
    
    public static Collection<BOMAlternativeHeader> fromJsonArrayToBOMAlternativeHeaders(String json) {
        return new JSONDeserializer<List<BOMAlternativeHeader>>().use(null, ArrayList.class).use("values", BOMAlternativeHeader.class).deserialize(json);
    }
    //</editor-fold>
}
