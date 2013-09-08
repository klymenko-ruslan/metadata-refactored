package com.turbointernational.metadata.domain.part;
import com.turbointernational.metadata.domain.bom.BOMItem;
import com.turbointernational.metadata.domain.other.Interchange;
import com.turbointernational.metadata.domain.other.Manufacturer;
import com.turbointernational.metadata.domain.type.PartType;
import com.turbointernational.metadata.util.ElasticSearch;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import javax.persistence.CascadeType;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import javax.persistence.Column;
import javax.persistence.EntityManager;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.persistence.Transient;
import javax.persistence.TypedQuery;
import javax.persistence.Version;
import net.sf.jsog.JSOG;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.json.RooJson;

@RooJpaActiveRecord(table="part", inheritanceType = "SINGLE_TABLE")
public class Part {
    
    public static List<Part> findPartEntries(int firstResult, int maxResults, String type) {
        EntityManager em = Part.entityManager();
        TypedQuery<Part> q;
        
        if (type == null) {
            q = em.createQuery("SELECT o FROM Part o", Part.class);
        } else {
            q = em.createQuery("SELECT o FROM Part o WHERE o.partType.name = ?", Part.class);
            q.setParameter(1, type);
        }
        
        return q.setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

    public static List<Part> getPartsUpdatedAfter(Date lastUpdated, int i, int pageSize) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

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
    
//    @Column(name="ti_part_num")
//    private String tiPartNumber;
    
    @Column(name="magento_product_id")
    private Integer magentoProductId;

    @OneToOne
    @JoinColumn(name="part_type_id")
    private PartType partType;

    @Column(nullable = false, columnDefinition = "BIT", length = 1)
    private Boolean inactive;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinTable(name="interchange_item",
               joinColumns=@JoinColumn(name="part_id"),
               inverseJoinColumns=@JoinColumn(name="interchange_header_id"))
    private Interchange interchange;

    @OneToMany(mappedBy="parent", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Collection<BOMItem> bom;
    
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

//    public String getTiPartNumber() {
//        return tiPartNumber;
//    }
//
//    public void setTiPartNumber(String tiPartNumber) {
//        this.tiPartNumber = tiPartNumber;
//    }

    public Integer getMagentoProductId() {
        return magentoProductId;
    }

    public void setMagentoProductId(Integer magentoProductId) {
        this.magentoProductId = magentoProductId;
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

    public void setInterchangeByPartId(Long interchangePartId) throws Exception {
        Part interchangePart = Part.findPart(interchangePartId);

        // Create the interchange if no interchange and a part were specified
        if (interchange == null && interchangePart != null) {

            // Check the other part to make sure it's blank
            if (interchangePart.interchange != null) {
                this.setInterchange(interchangePart.interchange);
            } else {
                setInterchange(new Interchange());
                this.setInterchange(interchange);
                interchange.setName("");
                interchange.persist();
            }
        }

        if (interchange != null) {

            // Create the set if necessary
            if (interchange.getParts() == null) {
                interchange.setParts(new HashSet());
            }

            // Set the bidirectional relationship
            interchange.getParts().add(this);

            if (interchangePart != null && interchange != interchangePart.interchange) {
                interchange.getParts().add(interchangePart);
                interchangePart.setInterchange(interchange);
                interchangePart.merge();
            }

            interchange.merge();
        }

    }

    public Collection<BOMItem> getBom() {
        return bom;
    }

    public void setBom(Collection<BOMItem> bom) {
        this.bom = bom;
    }
    
    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
    
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

    @PrePersist
    @PreUpdate
    public void preUpdatePersist() throws Exception {
        indexPart();
    }

    public void indexPart() throws Exception {
        try {
            elasticSearch.indexPart(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public JSOG toJsog() {
        JSOG partObject = JSOG.object()
            .put("id", id)
            .put("name", name)
            .put("description", description)
            .put("manufacturer_name", manufacturer.getName())
            .put("manufacturer_type_name", manufacturer.getType().getName())
            .put("manufacturer_part_number", manufacturerPartNumber);

        if (partType != null) {
            partObject.put("part_type", partType.getTypeName());
        }

        if (interchange != null) {
            partObject.put("interchange_id", interchange.getId());
        }
        
        return partObject;
    }
    
    public String toJson() {
        return new JSONSerializer()
                .include("bom")
                .exclude("*.class")
                .serialize(this);
    }
    
    public String toJson(String[] fields) {
        return new JSONSerializer()
                .include(fields)
                .exclude("*.class")
                .serialize(this);
    }
    
    public static Part fromJsonToPart(String json) {
        return new JSONDeserializer<Part>()
                .use(null, Part.class)
                .deserialize(json);
    }
    
    public static String toJsonArray(Collection<Part> collection) {
        return new JSONSerializer()
                .include("bom")
                .exclude("*.class")
                .serialize(collection);
    }
    
    public static String toJsonArray(Collection<Part> collection, String[] fields) {
        return new JSONSerializer()
                .include(fields)
                .exclude("*")
                .serialize(collection);
    }
    
    public static Collection<Part> fromJsonArrayToParts(String json) {
        return new JSONDeserializer<List<Part>>()
                .use(null, ArrayList.class)
                .use("values", Part.class)
                .deserialize(json);
    }
}
