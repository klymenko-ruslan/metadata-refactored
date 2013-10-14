package com.turbointernational.metadata.domain.other;
import com.turbointernational.metadata.domain.type.ManufacturerType;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.Cacheable;
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
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

@Cacheable
@Configurable
@Entity
@Table(name="MANFR", uniqueConstraints=@UniqueConstraint(columnNames={"name"}))
public class Manufacturer {
    
    //<editor-fold defaultstate="collapsed" desc="Properties">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable=false)
    private String name;
    
    @OneToOne
    @JoinColumn(name="manfr_type_id", nullable=false)
    private ManufacturerType type;
<<<<<<< HEAD

=======
    
>>>>>>> Using lazy loading where possible.
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="parent_manfr_id")
    private Manufacturer parent;
    
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
        EntityManager em = new Manufacturer().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
    
    public static long countManufacturers() {
        return entityManager().createQuery("SELECT COUNT(o) FROM Manufacturer o", Long.class).getSingleResult();
    }
    
    public static List<Manufacturer> findAllManufacturers() {
        return entityManager().createQuery("SELECT o FROM Manufacturer o", Manufacturer.class).getResultList();
    }
    
    public static Manufacturer findManufacturer(Long id) {
        if (id == null) return null;
        return entityManager().find(Manufacturer.class, id);
    }
    
    public static List<Manufacturer> findManufacturerEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM Manufacturer o", Manufacturer.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

    public static Manufacturer findByName(String name) {
        return entityManager()
            .createQuery("SELECT m FROM Manufacturer m WHERE m.name = :name", Manufacturer.class)
            .setParameter("name", name)
            .getSingleResult();
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
            Manufacturer attached = Manufacturer.findManufacturer(this.id);
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
    public Manufacturer merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        Manufacturer merged = this.entityManager.merge(this);
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
