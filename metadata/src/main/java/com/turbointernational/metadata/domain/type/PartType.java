package com.turbointernational.metadata.domain.type;
import com.google.common.collect.Lists;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javax.persistence.Cacheable;
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
import javax.persistence.Version;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

@Cacheable
@Configurable
@Entity
@Table(name = "PART_TYPE")
public class PartType {

    //<editor-fold defaultstate="collapsed" desc="properties">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable=false)
    private String name;
    
    @Column(name="DTYPE", nullable=false)
    private String typeName;
    
    @Column(name="magento_attribute_set")
    private String magentoAttributeSet;
    
    @Column(name="magento_category")
    private String magentoCategory;
    
    @Version
    @Column(name = "version")
    private Integer version;
    
    // TODO: Is this a duplicate of parentTypes?
    @OneToOne
    @JoinColumn(name="parent_part_type_id")
    private PartType parent;
    
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
    
    public String getTypeName() {
        return typeName;
    }
    
    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
    
    public String getMagentoAttributeSet() {
        return magentoAttributeSet;
    }
    
    public void setMagentoAttributeSet(String magentoName) {
        this.magentoAttributeSet = magentoName;
    }
    
    public String getMagentoCategory() {
        return magentoCategory;
    }
    
    public void setMagentoCategory(String magentoCategory) {
        this.magentoCategory = magentoCategory;
    }
    
    public PartType getParent() {
        return parent;
    }
    
    public void setParent(PartType parent) {
        this.parent = parent;
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
        EntityManager em = new PartType().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
    
    public static long countPartTypes() {
        return entityManager().createQuery("SELECT COUNT(o) FROM PartType o", Long.class).getSingleResult();
    }
    
    public static List<PartType> findAllPartTypes() {
        return entityManager().createQuery("SELECT o FROM PartType o", PartType.class).getResultList();
    }
    
    public static PartType findPartType(Long id) {
        if (id == null) return null;
        return entityManager().find(PartType.class, id);
    }
    
    public static List<PartType> findPartTypeEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM PartType o", PartType.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
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
            PartType attached = findPartType(this.id);
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
    public PartType merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        PartType merged = this.entityManager.merge(this);
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
    
    public static PartType fromJsonToPartType(String json) {
        return new JSONDeserializer<PartType>().use(null, PartType.class).deserialize(json);
    }
    
    public static String toJsonArray(Collection<PartType> collection) {
        return new JSONSerializer().exclude("*.class").serialize(collection);
    }
    
    public static String toJsonArray(Collection<PartType> collection, String[] fields) {
        return new JSONSerializer().include(fields).exclude("*.class").serialize(collection);
    }
    
    public static Collection<PartType> fromJsonArrayToPartTypes(String json) {
        return new JSONDeserializer<List<PartType>>().use(null, ArrayList.class).use("values", PartType.class).deserialize(json);
    }
    //</editor-fold>
    
    /**
     * Create a category of part types:
     * parent/child/grandchild
     */
    public String toMagentoCategories() {
        
        // Walk up the parent tree and build the hierarchy using "Part Type" as the root
        List<String> stack = Lists.newArrayList();
        
        // Get the first part type
        PartType partType = this;
        
        while (partType != null) {
            stack.add(partType.getName());

            // Setup next iteration
            partType = partType.getParent();
        }
        
        stack.add("Part Type");
        
        // The stack is upside down; reverse it.
        Collections.reverse(stack);
        
        return StringUtils.join(stack, '/');
    }

}
