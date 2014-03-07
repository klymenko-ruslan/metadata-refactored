package com.turbointernational.metadata.domain.part;
import com.turbointernational.metadata.domain.other.Manufacturer;
import com.turbointernational.metadata.domain.part.bom.BOMItem;
import com.turbointernational.metadata.domain.part.types.Backplate;
import com.turbointernational.metadata.domain.part.types.BearingHousing;
import com.turbointernational.metadata.domain.part.types.BearingSpacer;
import com.turbointernational.metadata.domain.part.types.Cartridge;
import com.turbointernational.metadata.domain.part.types.CompressorWheel;
import com.turbointernational.metadata.domain.part.types.Gasket;
import com.turbointernational.metadata.domain.part.types.Heatshield;
import com.turbointernational.metadata.domain.part.types.JournalBearing;
import com.turbointernational.metadata.domain.part.types.Kit;
import com.turbointernational.metadata.domain.part.types.NozzleRing;
import com.turbointernational.metadata.domain.part.types.PistonRing;
import com.turbointernational.metadata.domain.part.types.TurbineWheel;
import com.turbointernational.metadata.domain.part.types.Turbo;
import com.turbointernational.metadata.domain.type.PartType;
import com.turbointernational.metadata.util.ElasticSearch;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import flexjson.ObjectBinder;
import flexjson.ObjectFactory;
import flexjson.transformer.HibernateTransformer;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PersistenceContext;
import javax.persistence.PreRemove;
import javax.persistence.Query;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;
import net.sf.jsog.JSOG;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

@Cacheable
@Configurable
@Entity
@Table(name = "PART")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn
@NamedQueries({
    @NamedQuery(name="bom_parent_ids", query=
          "SELECT DISTINCT p\n"
        + "FROM\n"
        + "  Part p\n"
        + "  JOIN p.bom b\n"
        + "  JOIN b.parent parent\n"
        + "WHERE\n"
        + "  b.child.id = :partId"),
    @NamedQuery(name="bom_alt_parent_ids", query=
          "SELECT DISTINCT p.id\n"
        + "FROM\n"
        + "  Part p\n"
        + "  JOIN p.bom b"
        + "  JOIN b.alternatives alt\n"
        + "  JOIN alt.part altPart\n"
        + "WHERE\n"
        + "  altPart.id = :partId")})
public class Part implements Comparable<Part> {
    private static final Logger log = Logger.getLogger(Part.class.toString());
    
    public static final ObjectFactory OBJECT_FACTORY = new ObjectFactory() {
        
        @Override
        public Object instantiate(ObjectBinder context, Object value, Type targetType, Class targetClass) {
            Map<String, Object> valueHash = (Map) value;
            Map<String, Object> partTypeHash = (Map) valueHash.get("partType");
            String partType = (String) partTypeHash.get("typeName");
            
            // Create the appropriate part type
            Part part = null;
            if ("Backplate".equals(partType)) {
                part = new Backplate();
            } else if ("BearingHousing".equals(partType)) {
                part = new BearingHousing();
            } else if ("BearingSpacer".equals(partType)) {
                part = new BearingSpacer();
            } else if ("Cartridge".equals(partType)) {
                part = new Cartridge();
            } else if ("CompressorWheel".equals(partType)) {
                part = new CompressorWheel();
            } else if ("Gasket".equals(partType)) {
                part = new Gasket();
            } else if ("Heatshield".equals(partType)) {
                part = new Heatshield();
            } else if ("JournalBearing".equals(partType)) {
                part = new JournalBearing();
            } else if ("Kit".equals(partType)) {
                part = new Kit();
            } else if ("NozzleRing".equals(partType)) {
                part = new NozzleRing();
            } else if ("PistonRing".equals(partType)) {
                part = new PistonRing();
            } else if ("TurbineWheel".equals(partType)) {
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
    
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "manfr_id", nullable = false)
    private Manufacturer manufacturer;
    
    @Column(name = "manfr_part_num")
    private String manufacturerPartNumber;
    
    @Column(name = "name")
    private String name;
    
    @Column(name="description")
    private String description;
    
    @OneToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="part_type_id")
    private PartType partType;
    
    @Column(nullable = false, columnDefinition = "BIT", length = 1)
    private Boolean inactive = false;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinTable(name="interchange_item",
            joinColumns=@JoinColumn(name="part_id"),
            inverseJoinColumns=@JoinColumn(name="interchange_header_id"))
    private Interchange interchange;
    
    @OneToMany(mappedBy="parent", fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<BOMItem> bom = new TreeSet<BOMItem>();
    
    @OneToMany(cascade = CascadeType.REFRESH)
    @JoinTable(name="part_turbo", joinColumns=@JoinColumn(name="part_id"), inverseJoinColumns=@JoinColumn(name="turbo_id"))
    private Set<Turbo> turbos = new TreeSet<Turbo>();
    
    @OneToMany(cascade = CascadeType.REFRESH, mappedBy = "part")
    private Set<ProductImage> productImages = new HashSet<ProductImage>();
    
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
    
    public Set<Turbo> getTurbos() {
        return turbos;
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
    public void removeIndex() throws Exception {
        try {
            elasticSearch.deletePart(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void updateIndex() throws Exception {
        log.info("Updating index.");
        indexPart();
    }
    
    public void indexPart() throws Exception {
        try {
            elasticSearch.indexPart(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Serialization">

    protected JSONSerializer buildJSONSerializer() {
        return new JSONSerializer()
                .transform(new HibernateTransformer(), this.getClass())
                .include("partType.id")
                .include("partType.version")
                .include("partType.name")
                .include("partType.typeName")
                .include("manufacturer.id")
                .include("manufacturer.version")
                .include("manufacturer.name")
                .include("interchange.id")
                .include("interchange.version")
                .include("interchange.parts.id")
                .include("interchange.parts.name")
                .include("interchange.parts.version")
                .include("interchange.parts.partType.id")
                .include("interchange.parts.partType.name")
                .include("interchange.parts.partType.typeName")
                .include("interchange.parts.manufacturerPartNumber")
                .include("interchange.parts.manufacturer.id")
                .include("interchange.parts.manufacturer.name")
                .exclude("interchange.parts.*")
                .include("bom.id")
                .include("bom.version")
                .include("bom.child.id")
                .include("bom.child.version")
                .include("bom.child.name")
                .include("bom.child.partType.name")
                .include("bom.child.partType.typeName")
                .include("bom.child.manufacturer.id")
                .include("bom.child.manufacturer.name")
                .include("bom.child.manufacturerPartNumber")
                .exclude("bom.child.*")
                .include("bom.alternatives")
                .include("bom.alternatives.header")
                .include("bom.alternatives.part.id")
                .include("bom.alternatives.part.version")
                .include("bom.alternatives.part.manufacturer.id")
                .include("bom.alternatives.part.manufacturer.name")
                .include("bom.alternatives.part.manufacturerPartNumber")
                .exclude("bom.alternatives.part.*");
    }
    
    public JSOG toJsog() {
        JSOG partObject = JSOG.object()
                .put("id", id)
                .put("_id", id)
                .put("name", name)
                .put("description", description)
                .put("manufacturer_name", manufacturer.getName())
                .put("manufacturer_type_name", manufacturer.getType().getName())
                .put("manufacturer_part_number", manufacturerPartNumber)
                .put("part_type", partType.getTypeName())
                .put("attribute_set_id", partType.getMagentoAttributeSet());
        
        
        if (interchange != null) {
            partObject.put("interchange_id", interchange.getId());
        }
        
        return partObject;
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
                .include("partType.typeName")
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
    
    public static List<Part> findAll() {
        return entityManager().createQuery("SELECT o FROM Part o ORDER BY o.id", Part.class).getResultList();
    }
    
    public static Part findPart(Long id) {
        if (id == null) return null;
        Query q = entityManager().createQuery("SELECT DISTINCT p FROM Part p LEFT JOIN p.interchange i WHERE p.id = :id");
        q.setParameter("id", id);
        return (Part) q.getSingleResult();
    }
    
    public static List<Part> findPartEntries(int firstResult, int maxResults) {
        return entityManager()
                .createQuery(
                  "SELECT p\n"
                + "FROM Part p\n"
                + "  JOIN FETCH p.partType pt\n"
                + "  JOIN FETCH p.manufacturer m", Part.class)
                .setFirstResult(firstResult)
                .setMaxResults(maxResults).getResultList();
    }
    
    public static List<Part> findPartEntriesForTurboIndexing(int firstResult, int maxResults) {
        return entityManager().createQuery(
                "SELECT DISTINCT p\n"
              + "FROM Part p\n"
              + "LEFT JOIN p.turbos t\n"
              + "ORDER BY p.id", Part.class)
        .setFirstResult(firstResult)
        .setMaxResults(maxResults).getResultList();
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
    
    //<editor-fold defaultstate="collapsed" desc="Turbo Indexing">
    @Transactional
    public void indexTurbos() {
        
        Set<Turbo> newTurbos = collectTurbos();
        
        turbos.retainAll(newTurbos);
        turbos.addAll(newTurbos);
        
        // Save the new values
        this.merge();
        this.flush();
    }
    
    public Set<Turbo> collectTurbos() {
        long start = System.currentTimeMillis();
        
        TreeSet<Part> visitedParts = new TreeSet<Part>();
        TreeSet<Turbo> collectedTurbos = new TreeSet<Turbo>();
        TreeSet<Part> partsToVisit = new TreeSet<Part>(getBomParentParts());
        
        while (!partsToVisit.isEmpty()) {
            
            // Get the next part to visit
            Part bomAncestor = partsToVisit.first();
            partsToVisit.remove(bomAncestor);
            
            // Prevent recursion
            if (visitedParts.contains(bomAncestor)) {
                continue;
            } else {
                visitedParts.add(bomAncestor);
            }
            
            // Collect turbos and add BOM parents for non-turbo parts
            if (bomAncestor instanceof Turbo) {
                collectedTurbos.add((Turbo) bomAncestor);
            } else {
                // TODO: It would be more efficient to fetch the BOM parents of ALL pending parts at once rather than one at a time.
                partsToVisit.addAll(bomAncestor.getBomParentParts());
            }
        }
        
        long end = System.currentTimeMillis();
        log.log(Level.INFO, "Visited {0} parts and found {1} turbos for part id {2} in {3}ms.", new Object[]{
            visitedParts.size(),
            collectedTurbos.size(),
            id,
            end - start});
        
        return collectedTurbos;
    }
    
    /**
     * Gets the Part IDs of this part's BOM parents
     * @return 
     */
    public List<Part> getBomParentParts() {
        return entityManager.createNamedQuery("bom_parent_ids", Part.class)
            .setParameter("partId", this.id)
            .getResultList();
    }
    //</editor-fold>
    
    @Override
    public int compareTo(Part o) {
        return ObjectUtils.compare(this.id, o.id);
    }
}
