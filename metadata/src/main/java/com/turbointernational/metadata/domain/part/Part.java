package com.turbointernational.metadata.domain.part;

import com.turbointernational.metadata.domain.other.Manufacturer;
import com.turbointernational.metadata.domain.other.TurboType;
import com.turbointernational.metadata.domain.part.bom.BOMItem;
import com.turbointernational.metadata.domain.part.types.*;
import com.turbointernational.metadata.domain.type.PartType;
import com.turbointernational.metadata.util.ElasticSearch;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import flexjson.ObjectBinder;
import flexjson.ObjectFactory;
import flexjson.transformer.HibernateTransformer;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import javax.persistence.*;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.*;
import java.util.logging.Logger;

@Cacheable
@Configurable
@Entity
@Table(name = "PART")
@Inheritance(strategy = InheritanceType.JOINED)
public class Part implements Comparable<Part>, Serializable {
    private static final Logger log = Logger.getLogger(Part.class.toString());
    
    public static final ObjectFactory OBJECT_FACTORY = new ObjectFactory() {
        
        @Override
        public Object instantiate(ObjectBinder context, Object value, Type targetType, Class targetClass) {
            Map<String, Object> valueHash = (Map) value;
            Map<String, Object> partTypeHash = (Map) valueHash.get("partType");
            String partType = (String) partTypeHash.get("magentoAttributeSet");
            
            // Create the appropriate part type
            Part part = null;
            if ("Backplate".equals(partType)) {
                part = new Backplate();
            } else if ("Bearing Housing".equals(partType)) {
                part = new BearingHousing();
            } else if ("Bearing Spacer".equals(partType)) {
                part = new BearingSpacer();
            } else if ("Cartridge".equals(partType)) {
                part = new Cartridge();
            } else if ("Compressor Wheel".equals(partType)) {
                part = new CompressorWheel();
            } else if ("Gasket".equals(partType)) {
                part = new Gasket();
            } else if ("Heatshield".equals(partType)) {
                part = new Heatshield();
            } else if ("Journal Bearing".equals(partType)) {
                part = new JournalBearing();
            } else if ("Kit".equals(partType)) {
                part = new Kit();
            } else if ("Nozzle Ring".equals(partType)) {
                part = new NozzleRing();
            } else if ("Piston Ring".equals(partType)) {
                part = new PistonRing();
            } else if ("Turbine Wheel".equals(partType)) {
                part = new TurbineWheel();
            } else if ("Turbo".equals(partType)) {
                part = new Turbo();
            } else {
                part = new Part();
            }
            
            
            context.bind(value, part);
            return part;
        }
    };
    
    //<editor-fold defaultstate="collapsed" desc="Properties">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manfr_id", nullable = false)
    private Manufacturer manufacturer;
    
    @Column(name = "manfr_part_num")
    private String manufacturerPartNumber;
    
    @Column(name = "name")
    private String name;
    
    @Column(name="description")
    private String description;
    
    @OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="part_type_id")
    private PartType partType;
    
    @Column(nullable = false, columnDefinition = "BIT", length = 1)
    private Boolean inactive = false;
    
    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable(name="part_turbo_type",
                joinColumns = @JoinColumn(name="part_id"),
                inverseJoinColumns = @JoinColumn(name="turbo_type_id"))
    private Set<TurboType> turboTypes = new TreeSet<TurboType>();
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinTable(name="interchange_item",
            joinColumns=@JoinColumn(name="part_id"),
            inverseJoinColumns=@JoinColumn(name="interchange_header_id"))
    private Interchange interchange;
    
    @OneToMany(mappedBy="parent", fetch = FetchType.LAZY, orphanRemoval = true)
    @OrderBy("id")
    private Set<BOMItem> bom = new TreeSet<BOMItem>();
    
    @OneToMany(cascade = CascadeType.REFRESH, mappedBy = "part", fetch=FetchType.LAZY)
    private Set<ProductImage> productImages = new TreeSet<ProductImage>();
    
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
    
    public String getManufacturerPartNumber() {
        return manufacturerPartNumber;
    }
    
    public void setManufacturerPartNumber(String manufacturerPartNumber) {
        this.manufacturerPartNumber = manufacturerPartNumber;
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
    
    public PartType getPartType() {
        return partType;
    }
    
    public void setPartType(PartType partType) {
        this.partType = partType;
    }
    
    public Boolean getInactive() {
        return inactive;
    }
    
    public void setInactive(Boolean inactive) {
        this.inactive = inactive;
    }

    public Interchange getInterchange() {
        return interchange;
    }
    
    public void setInterchange(Interchange interchange) {
        this.interchange = interchange;
    }
    
    public Set<BOMItem> getBom() {
        return bom;
    }
    
    public void setBom(Set<BOMItem> bom) {
        this.bom.clear();
        this.bom.addAll(bom);
    }
    
    public Set<TurboType> getTurboTypes() {
        return turboTypes;
    }

    public Set<ProductImage> getProductImages() {
        return productImages;
    }
    
    public Integer getVersion() {
        return version;
    }
    
    public void setVersion(Integer version) {
        this.version = version;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Lifecycle">
    @Autowired(required=true)
    @Transient
    private ElasticSearch elasticSearch;
    
    @PreRemove
    public void removeSearchIndex() throws Exception {
        try {
            elasticSearch.deletePart(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @PostUpdate
    @PostPersist
    public void updateSearchIndex() throws Exception {
        log.info("Updating search index.");
        elasticSearch.indexPart(this);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Serialization">

    protected JSONSerializer buildJSONSerializer() {
        return new JSONSerializer()
                .transform(new HibernateTransformer(), this.getClass())
                .include("turboTypes.id")
                .include("turboTypes.name")
                .include("partType.id")
                .include("partType.name")
                .include("manufacturer.id")
                .include("manufacturer.name")
                .include("interchange.id")
                .include("interchange.version")
                .include("interchange.parts.id")
                .include("interchange.parts.name")
                .include("interchange.parts.version")
                .include("interchange.parts.partType.id")
                .include("interchange.parts.partType.name")
                .include("interchange.parts.manufacturerPartNumber")
                .include("interchange.parts.manufacturer.id")
                .include("interchange.parts.manufacturer.name")
                .exclude("interchange.parts.*")
                .include("bomParentParts.id")
                .include("bomParentParts.name")
                .include("bomParentParts.partType.id")
                .include("bomParentParts.partType.name")
                .include("bomParentParts.manufacturerPartNumber")
                .include("bomParentParts.manufacturer.id")
                .include("bomParentParts.manufacturer.name")
                .exclude("bomParentParts.*")
                .include("bom.id")
                .include("bom.version")
                .include("bom.child.id")
                .include("bom.child.version")
                .include("bom.child.name")
                .include("bom.child.partType.id")
                .include("bom.child.partType.name")
                .include("bom.child.manufacturer.id")
                .include("bom.child.manufacturer.name")
                .include("bom.child.manufacturerPartNumber")
                .exclude("bom.child.*")
                .include("bom.alternatives")
                .include("bom.alternatives.header")
                .include("bom.alternatives.part.id")
                .include("bom.alternatives.part.name")
                .include("bom.alternatives.part.version")
                .include("bom.alternatives.part.partType.id")
                .include("bom.alternatives.part.partType.name")
                .include("bom.alternatives.part.manufacturerPartNumber")
                .include("bom.alternatives.part.manufacturer.id")
                .include("bom.alternatives.part.manufacturer.name")
                .exclude("bom.alternatives.part.*")
                .include("productImages.id")
                .include("productImages.filename")
                .exclude("productImages.*");
    }
    
    public String toJson() {
        return buildJSONSerializer()
                .exclude("*.class")
                .serialize(this);
    }
    
    protected JSONSerializer getSearchSerializer() {
        return new JSONSerializer()
                .include("id")
                .include("name")
                .include("manufacturerPartNumber")
                .include("description")
                .include("partType.id")
                .include("partType.name")
                .exclude("partType.*")
                .include("manufacturer.id")
                .include("manufacturer.name")
                .exclude("manufacturer.*")
                .exclude("bomParentParts")
                .exclude("bom")
                .exclude("interchange")
                .exclude("turbos")
                .exclude("productImages")
                .exclude("*.class");
    }
    
    public String toSearchJson() {
        return getSearchSerializer().exclude("*").serialize(this);
    }
    
    public static Part fromJsonToPart(String json) {
        Part part = new JSONDeserializer<Part>()
                .use((String) null, OBJECT_FACTORY)
//                .use("bom", TreeSet.class)
                .use("bom.values", BOMItem.class)
                .deserialize(json);
        
        return part;
    }
    
    public static String toJsonArray(Collection<Part> collection) {
        return new JSONSerializer()
                .transform(new HibernateTransformer(), Part.class)
//                .include("bom")
                .exclude("*.class")
                .serialize(collection);
    }
    
    public static String toJsonArray(Collection<Part> collection, String[] fields) {
        return new JSONSerializer()
                .transform(new HibernateTransformer(), Part.class)
                .include(fields)
                .exclude("*")
                .serialize(collection);
    }

    public void csvColumns(Map<String, String> columns) {
        // part_type
        columns.put("part_type", getPartType().getName());
        
        // sku
        columns.put("sku", getId().toString());
        
        // attribute_set
        columns.put("attribute_set", getPartType().getMagentoAttributeSet());
        
        // type
        columns.put("type", "simple");
        
        // visibility
        columns.put("visibility", "Catalog, Search"); // See magmi genericmapper visibility.csv
        
        // type
        columns.put("status", "Enabled"); // See magmi genericmapper status.csv
        
        // name
        columns.put("name", ObjectUtils.toString(getName()));
        
        // description
        columns.put("description", ObjectUtils.toString(getDescription()));
        
        // manufacturer
        columns.put("manufacturer", ObjectUtils.toString(getManufacturer().getName()));
        
        // part_number
        columns.put("part_number", ObjectUtils.toString(getManufacturerPartNumber()));
        
        // part_number
        columns.put("part_number_short", ObjectUtils.toString(getManufacturerPartNumber()).replaceAll("\\W", ""));
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="ActiveRecord">
    @PersistenceContext
    @Transient
    private EntityManager entityManager;
    
    @Autowired(required=true)
    @Transient
    private PlatformTransactionManager txManager;
    
    public static final EntityManager entityManager() {
        EntityManager em = new Part().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
    
    public static long count() {
        return entityManager().createQuery("SELECT COUNT(o) FROM Part o", Long.class).getSingleResult();
    }

    public static List<Part> getPartsUpdatedAfter(Date lastUpdated, int i, int pageSize) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public static Part findPart(Long id) {
        if (id == null) return null;
        EntityManager em = entityManager();
        Part retVal = em.find(Part.class, id);
        return retVal;
    }
    
    public static List<Part> findPartEntries(int firstResult, int maxResults) {
        return entityManager()
                .createQuery(
                  "SELECT p\n"
                + "FROM Part p", Part.class)
                .setFirstResult(firstResult)
                .setMaxResults(maxResults)
                .getResultList();
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
            Part attached = Part.findPart(this.id);
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
    public Part merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        Part merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
    @Transactional
    public void refresh() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.refresh(this);
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="BOM Ancestry">
    
    /**
     * Contains the date when we started the BOM rebuild, or null if not currently rebuilding.
     */
    private volatile static Date bomRebuildStart = null;
    
    public static final Date getBomRebuildStart() {
        return bomRebuildStart;
    }
    
    @Async("bomRebuildExecutor") // One at a time
    public static void rebuildBomDescendancy() {
        try {
            // Track the rebuild so users can get status
            bomRebuildStart = new Date();
            
            new TransactionTemplate(new Part().txManager).execute(new TransactionCallback() {
                @Override
                public Object doInTransaction(TransactionStatus status) {
                    log.info("Rebuilding BOM descendancy.");
                    EntityManager em = entityManager();

                    // Delete the old ancestry
                    em.createNativeQuery("CALL RebuildBomDescendancy()").executeUpdate();
                    em.clear();
                    log.info("BOM descendancy rebuild completed.");

                    return null;
                }
            });
        } finally {
            bomRebuildStart = null;
        }
    }
    //</editor-fold>
    
    @Override
    public int compareTo(Part o) {
        return ObjectUtils.compare(this.id, o.id);
    }
}
