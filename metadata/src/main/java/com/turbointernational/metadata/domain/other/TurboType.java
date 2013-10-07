package com.turbointernational.metadata.domain.other;
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
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.PersistenceContext;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.transaction.annotation.Transactional;

@Configurable
@Entity
@RooJpaActiveRecord
@RooJson
@Table(name="TURBO_TYPE", uniqueConstraints=@UniqueConstraint(columnNames={"name"}))
public class TurboType {
    
    //<editor-fold defaultstate="collapsed" desc="Properties">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne
    @JoinColumn(name="manfr_id", nullable=false)
    private Manufacturer manufacturer;
    
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
    
    public Integer getVersion() {
        return version;
    }
    
    public void setVersion(Integer version) {
        this.version = version;
    }
    
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Serialization">
    
    public String toJson() {
        return new JSONSerializer()
                .exclude("version")
                .exclude("*.class").serialize(this);
    }
    
    public String toJson(String[] fields) {
        return new JSONSerializer()
                .include(fields)
                .exclude("version")
                .exclude("*.class").serialize(this);
    }
    
    public static TurboType fromJsonToTurboType(String json) {
        return new JSONDeserializer<TurboType>().use(null, TurboType.class).deserialize(json);
    }
    
    public static String toJsonArray(Collection<TurboType> collection) {
        return new JSONSerializer()
                .include("manufacturer.id")
                .exclude("manufacturer.*")
                .exclude("version")
                .exclude("*.class")
                .serialize(collection);
    }
    
    public static String toJsonArray(Collection<TurboType> collection, String[] fields) {
        return new JSONSerializer()
                .include(fields)
                .exclude("version")
                .exclude("*.class")
                .serialize(collection);
    }
    
    public static Collection<TurboType> fromJsonArrayToTurboTypes(String json) {
        return new JSONDeserializer<List<TurboType>>().use(null, ArrayList.class).use("values", TurboType.class).deserialize(json);
    }
    
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="ActiveRecord">
    public static List<TurboType> findTurboTypesByManufacturerId(Long manufacturerId) {
        return entityManager().createQuery(
              "SELECT o\n"
            + "FROM\n"
            + "  TurboType o\n"
            + "  JOIN o.manufacturer\n"
            + "WHERE o.manufacturer.id = :manufacturerId",
            TurboType.class)
            .setParameter("manufacturerId", manufacturerId)
            .getResultList();
    }
    
    @PersistenceContext
    transient EntityManager entityManager;
    
    public static final EntityManager entityManager() {
        EntityManager em = new TurboType().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
    
    public static long countTurboTypes() {
        return entityManager().createQuery("SELECT COUNT(o) FROM TurboType o", Long.class).getSingleResult();
    }
    
    public static List<TurboType> findAllTurboTypes() {
        return entityManager().createQuery("SELECT o FROM TurboType o", TurboType.class).getResultList();
    }
    
    public static TurboType findTurboType(Long id) {
        if (id == null) return null;
        return entityManager().find(TurboType.class, id);
    }
    
    public static List<TurboType> findTurboTypeEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM TurboType o", TurboType.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
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
            TurboType attached = findTurboType(this.id);
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
    public TurboType merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        TurboType merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    //</editor-fold>
}
