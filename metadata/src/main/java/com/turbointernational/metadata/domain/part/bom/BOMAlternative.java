package com.turbointernational.metadata.domain.part.bom;
import com.turbointernational.metadata.domain.part.Part;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
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
import javax.persistence.Version;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

@Configurable
@Entity
@Table(name="bom_alt_item")
public class BOMAlternative {

    //<editor-fold defaultstate="collapsed" desc="properties">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name="bom_id")
    private BOMItem bomItem;
    
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name="bom_alt_header_id")
    private BOMAlternativeHeader header;
    
    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name="part_id")
    private Part part;
    
    @Version
    @Column(name = "version")
    private Integer version;
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public BOMItem getBomItem() {
        return bomItem;
    }
    
    public void setBomItem(BOMItem bomItem) {
        this.bomItem = bomItem;
    }
    
    public BOMAlternativeHeader getHeader() {
        return header;
    }
    
    public void setHeader(BOMAlternativeHeader header) {
        this.header = header;
    }
    
    public Part getPart() {
        return part;
    }
    
    public void setPart(Part part) {
        this.part = part;
    }
    
    public Integer getVersion() {
        return this.version;
    }
    
    public void setVersion(Integer version) {
        this.version = version;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="activerecord">
    @PersistenceContext
    transient EntityManager entityManager;
    
    public static final EntityManager entityManager() {
        EntityManager em = new BOMAlternative().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
    
    public static long countBOMAlternatives() {
        return entityManager().createQuery("SELECT COUNT(o) FROM BOMAlternative o", Long.class).getSingleResult();
    }
    
    public static List<BOMAlternative> findAllBOMAlternatives() {
        return entityManager().createQuery("SELECT o FROM BOMAlternative o", BOMAlternative.class).getResultList();
    }
    
    public static BOMAlternative findBOMAlternative(Long id) {
        if (id == null) return null;
        return entityManager().find(BOMAlternative.class, id);
    }
    
    public static List<BOMAlternative> findBOMAlternativeEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM BOMAlternative o", BOMAlternative.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
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
            BOMAlternative attached = findBOMAlternative(this.id);
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
    public BOMAlternative merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        BOMAlternative merged = this.entityManager.merge(this);
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
    
    public static BOMAlternative fromJsonToBOMAlternative(String json) {
        return new JSONDeserializer<BOMAlternative>().use(null, BOMAlternative.class).deserialize(json);
    }
    
    public static String toJsonArray(Collection<BOMAlternative> collection) {
        return new JSONSerializer().exclude("*.class").serialize(collection);
    }
    
    public static String toJsonArray(Collection<BOMAlternative> collection, String[] fields) {
        return new JSONSerializer().include(fields).exclude("*.class").serialize(collection);
    }
    
    public static Collection<BOMAlternative> fromJsonArrayToBOMAlternatives(String json) {
        return new JSONDeserializer<List<BOMAlternative>>().use(null, ArrayList.class).use("values", BOMAlternative.class).deserialize(json);
    }
    //</editor-fold>
}
