package com.turbointernational.metadata.domain.part;
import com.turbointernational.metadata.domain.other.Interchange;
import com.turbointernational.metadata.domain.other.Manufacturer;
import com.turbointernational.metadata.domain.type.PartType;
import com.turbointernational.metadata.util.ElasticSearch;
import java.util.HashSet;
import java.util.List;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import javax.persistence.Column;
import javax.persistence.EntityManager;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
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

@RooJpaActiveRecord(table="PART", inheritanceType = "SINGLE_TABLE")
@RooJson
public class Part {

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

    @Transient
    private Part interchangePart;
    
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
     * @return the manufacturer
     */
    public Manufacturer getManufacturer() {
        return manufacturer;
    }

    /**
     * @param manufacturer the manufacturer to set
     */
    public void setManufacturer(Manufacturer manufacturer) {
        this.manufacturer = manufacturer;
    }

    /**
     * @return the manufacturerPartNumber
     */
    public String getManufacturerPartNumber() {
        return manufacturerPartNumber;
    }

    /**
     * @param manufacturerPartNumber the manufacturerPartNumber to set
     */
    public void setManufacturerPartNumber(String manufacturerPartNumber) {
        this.manufacturerPartNumber = manufacturerPartNumber;
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

    /**
     * @return the partType
     */
    public PartType getPartType() {
        return partType;
    }

    /**
     * @param partType the partType to set
     */
    public void setPartType(PartType partType) {
        this.partType = partType;
    }

    /**
     * @return the inactive
     */
    public Boolean getInactive() {
        return inactive;
    }

    /**
     * @param inactive the inactive to set
     */
    public void setInactive(Boolean inactive) {
        this.inactive = inactive;
    }

    /**
     * @return the interchange
     */
    public Interchange getInterchange() {
        return interchange;
    }

    /**
     * @param interchange the interchange to set
     */
    public void setInterchange(Interchange interchange) {
        this.interchange = interchange;
    }

    /**
     * @return the interchangePart
     */
    public Part getInterchangePart() {
        return interchangePart;
    }

    /**
     * @param interchangePart the interchangePart to set
     */
    public void setInterchangePart(Part interchangePart) {
        this.interchangePart = interchangePart;
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
    
    public static List<Part> findPartEntries(int firstResult, int maxResults, PartType partType) {
        EntityManager em = entityManager();
        TypedQuery<Part> q;
        
        if (partType == null) {
            q = em.createQuery("SELECT o FROM Part o", Part.class);
        } else {
            q = em.createQuery("SELECT o FROM Part o WHERE o.partType = ?", Part.class);
        }
        
        return q.setParameter(1, partType).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

    @PrePersist
    @PreUpdate
    public void preUpdatePersist() throws Exception {
        indexPart();
        updateInterchanges();
    }

    public void updateInterchanges() throws Exception {

        // Create the interchange if no interchange and a part were specified
        if (getInterchange() == null && getInterchangePart() != null) {

            // Check the other part to make sure it's blank
            if (getInterchangePart().getInterchange() != null) {
                this.setInterchange(getInterchangePart().getInterchange());
            } else {
                setInterchange(new Interchange());
                this.setInterchange(getInterchange());
                getInterchange().setName("");
                getInterchange().persist();
            }
        }

        if (getInterchange() != null) {

            // Create the set if necessary
            if (getInterchange().getParts() == null) {
                getInterchange().setParts(new HashSet());
            }

            // Set the bidirectional relationship
            getInterchange().getParts().add(this);

            if (getInterchangePart() != null && getInterchange() != getInterchangePart().getInterchange()) {
                getInterchange().getParts().add(getInterchangePart());
                getInterchangePart().setInterchange(getInterchange());
                getInterchangePart().merge();
            }

            getInterchange().merge();
        }

    }

    public void indexPart() throws Exception {
        try {
            elasticSearch.indexPart(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @PreRemove
    public void removeIndex() throws Exception {
        try {
            elasticSearch.deletePart(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    @OneToMany(mappedBy="parent", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    @JoinColumn(name="parent_part_id", table="bom")
//    private Collection<BOMItem> bom;

    public void addIndexFields(JSOG partObject) {}

}
