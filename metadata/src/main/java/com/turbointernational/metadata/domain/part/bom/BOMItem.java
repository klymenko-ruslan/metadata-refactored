package com.turbointernational.metadata.domain.part.bom;
import com.turbointernational.metadata.domain.other.Manufacturer;
import com.turbointernational.metadata.domain.part.Part;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PersistenceContext;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

@Configurable
@Entity
@Table(name="bom")
public class BOMItem implements Comparable<BOMItem> {

    //<editor-fold defaultstate="collapsed" desc="properties">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name="parent_part_id")
    private Part parent;
    
    @OneToOne(fetch = FetchType.EAGER, cascade = {CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name="child_part_id")
    private Part child;
    
    @Column(nullable=false)
    private Integer quantity;
    
    @OneToMany(mappedBy="bomItem", fetch = FetchType.LAZY, cascade = {CascadeType.ALL}, orphanRemoval = true)
    private Set<BOMAlternative> alternatives = new TreeSet<BOMAlternative>();
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Part getParent() {
        return parent;
    }
    
    public void setParent(Part parent) {
        this.parent = parent;
    }
    
    public Part getChild() {
        return child;
    }
    
    public void setChild(Part child) {
        this.child = child;
    }
    
    public Integer getQuantity() {
        return quantity;
    }
    
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
    
    public Set<BOMAlternative> getAlternatives() {
        return alternatives;
    }
    
    public void setAlternatives(Set<BOMAlternative> alternatives) {
        this.alternatives.clear();
        this.alternatives.addAll(alternatives);
    }
    
    public List<Part> getTIAlternates() {
        List<Part> tiAlts = new ArrayList<Part>();
        
        for (BOMAlternative alt : alternatives) {
            if (Manufacturer.TI_ID.equals(alt.getPart().getManufacturer().getId())) {
                tiAlts.add(alt.getPart());
            }
        }
        
        return tiAlts;
    }
    
    @PrePersist
    @PreUpdate
    public void validate() {
        if (ObjectUtils.equals(child.getId(), parent.getId())) {
            throw new IllegalStateException("Child cannot be it's own parent.");
        }
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="activerecord">
    @PersistenceContext
    transient EntityManager entityManager;
    
    public static final EntityManager entityManager() {
        EntityManager em = new BOMItem().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
    
    public static long countBOMItems() {
        return entityManager().createQuery("SELECT COUNT(o) FROM BOMItem o", Long.class).getSingleResult();
    }
    
    public static List<BOMItem> findAllBOMItems() {
        return entityManager().createQuery("SELECT o FROM BOMItem o", BOMItem.class).getResultList();
    }
    
    public static BOMItem findBOMItem(Long id) {
        if (id == null) return null;
        return entityManager().find(BOMItem.class, id);
    }
    
    public static List<BOMItem> findBOMItemEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM BOMItem o", BOMItem.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
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
            BOMItem attached = findBOMItem(this.id);
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
    public BOMItem merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        BOMItem merged = this.entityManager.merge(this);
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
    
    public static BOMItem fromJsonToBOMItem(String json) {
        return new JSONDeserializer<BOMItem>().use(null, BOMItem.class).deserialize(json);
    }
    
    public static String toJsonArray(Collection<BOMItem> collection) {
        return new JSONSerializer().exclude("*.class").serialize(collection);
    }
    
    public static String toJsonArray(Collection<BOMItem> collection, String[] fields) {
        return new JSONSerializer().include(fields).exclude("*.class").serialize(collection);
    }
    
    public static Collection<BOMItem> fromJsonArrayToBOMItems(String json) {
        return new JSONDeserializer<List<BOMItem>>().use(null, ArrayList.class).use("values", BOMItem.class).deserialize(json);
    }
    //</editor-fold>

    @Override
    public int compareTo(BOMItem o) {
        return ObjectUtils.compare(this.id, o.id);
    }
    
}
