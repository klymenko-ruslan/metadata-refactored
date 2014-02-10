package com.turbointernational.metadata.domain.part;
import com.google.common.collect.Sets;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.PersistenceContext;
import javax.persistence.Table;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

@Configurable
@Entity
@Table(name="interchange_header")
public class Interchange {

    //<editor-fold defaultstate="collapsed" desc="Properties">
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    private String description;
    
    @OneToMany(mappedBy = "interchange", fetch = FetchType.LAZY)
    private Set<Part> parts = Sets.newTreeSet();
    
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
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public Set<Part> getParts() {
        return parts;
    }
    
    public void setParts(Set<Part> parts) {
        this.parts = parts;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="ActiveRecord">
    
    @PersistenceContext
    transient EntityManager entityManager;
    
    public static final EntityManager entityManager() {
        EntityManager em = new Interchange().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
    
    public static long countInterchanges() {
        return entityManager().createQuery("SELECT COUNT(o) FROM Interchange o", Long.class).getSingleResult();
    }
    
    public static List<Interchange> findAllInterchanges() {
        return entityManager().createQuery("SELECT o FROM Interchange o", Interchange.class).getResultList();
    }
    
    public static Interchange findInterchange(Long id) {
        if (id == null) return null;
        return entityManager().find(Interchange.class, id);
    }
    
    public static List<Interchange> findInterchangeEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM Interchange o", Interchange.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
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
            Interchange attached = findInterchange(this.id);
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
    public Interchange merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        Interchange merged = this.entityManager.merge(this);
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
    
    public static Interchange fromJsonToInterchange(String json) {
        return new JSONDeserializer<Interchange>().use(null, Interchange.class).deserialize(json);
    }
    
    public static String toJsonArray(Collection<Interchange> collection) {
        return new JSONSerializer().exclude("*.class").serialize(collection);
    }
    
    public static String toJsonArray(Collection<Interchange> collection, String[] fields) {
        return new JSONSerializer().include(fields).exclude("*.class").serialize(collection);
    }
    
    public static Collection<Interchange> fromJsonArrayToInterchanges(String json) {
        return new JSONDeserializer<List<Interchange>>().use(null, ArrayList.class).use("values", Interchange.class).deserialize(json);
    }
    //</editor-fold>
    
}
