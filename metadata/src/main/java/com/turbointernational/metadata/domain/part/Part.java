package com.turbointernational.metadata.domain.part;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.domain.SearchableEntity;
import com.turbointernational.metadata.domain.criticaldimension.CriticalDimension;
import com.turbointernational.metadata.domain.other.Manufacturer;
import com.turbointernational.metadata.domain.other.TurboType;
import com.turbointernational.metadata.domain.part.bom.BOMItem;
import com.turbointernational.metadata.domain.part.salesnote.SalesNotePart;
import com.turbointernational.metadata.domain.part.types.*;
import com.turbointernational.metadata.domain.type.PartType;
import com.turbointernational.metadata.services.CriticalDimensionService;
import com.turbointernational.metadata.services.SearchService;
import com.turbointernational.metadata.web.View;
import flexjson.JSONSerializer;
import flexjson.transformer.HibernateTransformer;
import flexjson.transformer.Transformer;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.ALWAYS;
import static com.fasterxml.jackson.annotation.JsonTypeInfo.As.PROPERTY;
import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.CLASS;
import static javax.persistence.GenerationType.IDENTITY;
import static javax.persistence.InheritanceType.JOINED;
import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

@Cacheable
@Entity
@Table(name = "part")
@Inheritance(strategy = JOINED)
@Component
@Scope(SCOPE_PROTOTYPE)
@JsonTypeInfo(use = CLASS, property = "class", include = PROPERTY, defaultImpl = Part.class)
@JsonSubTypes({
        @JsonSubTypes.Type(Actuator.class),
        @JsonSubTypes.Type(Backplate.class),
        @JsonSubTypes.Type(BearingHousing.class),
        @JsonSubTypes.Type(BearingSpacer.class),
        @JsonSubTypes.Type(BoltScrew.class),
        @JsonSubTypes.Type(CarbonSeal.class),
        @JsonSubTypes.Type(Cartridge.class),
        @JsonSubTypes.Type(Clamp.class),
        @JsonSubTypes.Type(CompressorCover.class),
        @JsonSubTypes.Type(CompressorWheel.class),
        @JsonSubTypes.Type(Fitting.class),
        @JsonSubTypes.Type(Gasket.class),
        @JsonSubTypes.Type(Heatshield.class),
        @JsonSubTypes.Type(JournalBearing.class),
        @JsonSubTypes.Type(JournalBearingSpacer.class),
        @JsonSubTypes.Type(Kit.class),
        @JsonSubTypes.Type(NozzleRing.class),
        @JsonSubTypes.Type(Nut.class),
        @JsonSubTypes.Type(OilDeflector.class),
        @JsonSubTypes.Type(ORing.class),
        @JsonSubTypes.Type(Pin.class),
        @JsonSubTypes.Type(PistonRing.class),
        @JsonSubTypes.Type(Plug.class),
        @JsonSubTypes.Type(RetainingRing.class),
        @JsonSubTypes.Type(SealPlate.class),
        @JsonSubTypes.Type(Spring.class),
        @JsonSubTypes.Type(ThrustBearing.class),
        @JsonSubTypes.Type(ThrustCollar.class),
        @JsonSubTypes.Type(ThrustWasher.class),
        @JsonSubTypes.Type(TurbineHousing.class),
        @JsonSubTypes.Type(TurbineWheel.class),
        @JsonSubTypes.Type(Turbo.class),
        @JsonSubTypes.Type(Washer.class),
})
@NamedQueries({
        @NamedQuery(
                name = "findOnePart",
                query = "SELECT DISTINCT p FROM Part p LEFT JOIN p.interchange i WHERE p.id = :id"
        ),
        @NamedQuery(
                name = "findByPartNumber",
                query = "FROM Part p WHERE p.manufacturerPartNumber = :partNumber"
        ),
        @NamedQuery(
                name = "findByPartNumberAndManufacturer",
                query = "FROM Part p WHERE p.manufacturer.id=:manufacturerId AND p.manufacturerPartNumber = :partNumber"
        ),
        @NamedQuery(
                name = "findAllPartsOrderedById",
                query = "FROM Part p ORDER BY p.id"
        )
})
@JsonInclude(ALWAYS)
public class Part implements Comparable<Part>, Serializable, SearchableEntity {

    private static final Logger log = LoggerFactory.getLogger(Part.class);

    //<editor-fold defaultstate="collapsed" desc="Properties">
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @JsonView({View.Summary.class})
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JsonView({View.Summary.class})
    @JoinColumn(name = "manfr_id", nullable = false)
    private Manufacturer manufacturer;

    @Column(name = "manfr_part_num")
    @JsonView({View.Summary.class})
    private String manufacturerPartNumber;

    @Column(name = "name")
    @JsonView({View.Summary.class})
    private String name;

    @Column(name = "description")
    @JsonView({View.Detail.class})
    private String description;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "part_type_id")
    @JsonView({View.Summary.class})
    private PartType partType;

    @Column(name = "inactive", nullable = false, columnDefinition = "BIT", length = 1)
    @JsonView({View.Detail.class})
    private Boolean inactive = false;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "part_turbo_type",
            joinColumns = @JoinColumn(name = "part_id"),
            inverseJoinColumns = @JoinColumn(name = "turbo_type_id"))
    @JsonView({View.Detail.class})
    private Set<TurboType> turboTypes = new TreeSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinTable(name = "interchange_item",
            joinColumns = @JoinColumn(name = "part_id"),
            inverseJoinColumns = @JoinColumn(name = "interchange_header_id"))
    @JsonView({View.Detail.class})
    private Interchange interchange;

    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY, orphanRemoval = true)
    @OrderBy("id")
    private Set<BOMItem> bom = new TreeSet<>();

    @OneToMany(cascade = CascadeType.REFRESH, mappedBy = "part", fetch = FetchType.LAZY)
    @JsonView({View.Detail.class})
    @OrderBy("id")
    private Set<ProductImage> productImages = new TreeSet<>();

    @OneToMany(cascade = CascadeType.REFRESH, mappedBy = "pk.part", fetch = FetchType.LAZY)
    @OrderBy("pk.salesNote.id")
    private List<SalesNotePart> salesNoteParts = new ArrayList<>();

    @Column(name = "import_pk")
    private Long importPk;

    @Version
    @Column(name = "version")
    @JsonView(View.Summary.class)
    @JsonInclude(ALWAYS)
    private int version;

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

    public List<SalesNotePart> getSalesNoteParts() {
        return salesNoteParts;
    }

    public void setSalesNoteParts(List<SalesNotePart> salesNoteParts) {
        this.salesNoteParts = salesNoteParts;
    }

    public Long getImportPk() {
        return importPk;
    }

    public void setImportPk(Long importPk) {
        this.importPk = importPk;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Lifecycle">
    @PostRemove
    @Override
    public void removeSearchIndex() throws Exception {
        log.info("Updating search index.");
        SearchService.instance().deletePart(this);
    }

    @PostUpdate
    @PostPersist
    @Override
    public void updateSearchIndex() throws Exception {
        log.info("Updating search index.");
        SearchService.instance().indexPart(this);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Serialization">

    protected JSONSerializer buildJSONSerializer(List<CriticalDimension> criticalDimensions) {
        JSONSerializer jsonSerializer = new JSONSerializer()
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
        // Add critical dimensions.
        addCriticalDimensionsToSerialization(criticalDimensions, jsonSerializer, false);
        return jsonSerializer;
    }

    public String toJson(List<CriticalDimension> criticalDimensions) {
        return buildJSONSerializer(criticalDimensions)
                .exclude("*.class")
                .serialize(this);
    }

    @Override
    public final String toSearchJson(List<CriticalDimension> criticalDimensions) {
        JSONSerializer jsonSerializer = new JSONSerializer()
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
        // Add critical dimensions.
        addCriticalDimensionsToSerialization(criticalDimensions, jsonSerializer, true);
        String json = jsonSerializer.exclude("*").serialize(this);
        return json;
    }

    /**
     * Add critical dimensions to a serialized part.
     *
     * @param criticalDimensions
     * @param jsonSerializer
     * @param searchSerialization true if a part is serialized to a search engine.
     * @see com.turbointernational.metadata.services.CriticalDimensionService#JsonIdxNameTransformer
     */
    private void addCriticalDimensionsToSerialization(List<CriticalDimension> criticalDimensions,
                                                      JSONSerializer jsonSerializer, boolean searchSerialization) {
        if (criticalDimensions != null) {
            for(CriticalDimension cd : criticalDimensions) {
                String jsonName = cd.getJsonName();
                jsonSerializer.include(jsonName);
                if (searchSerialization) {
                    Transformer t = cd.getJsonIdxNameTransformer();
                    if (t != null) {
                        jsonSerializer.transform(t, jsonName);
                    }
                }
            }
        }
    }

    @Override
    public String getSearchId() {
        return getId().toString();
    }

    public void csvColumns(Map<String, String> columns, List<CriticalDimension> criticalDimensions) {
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

        if (criticalDimensions != null) {
            for(CriticalDimension cd : criticalDimensions) {
                String fieldName = cd.getJsonName();
                CriticalDimensionService.extractValue(this, cd, new CriticalDimensionService.ValueExtractorCallback() {

                    @Override
                    public void processValue(Object value) {
                        columns.put(fieldName, ObjectUtils.toString(value));

                    }

                    @Override
                    public void onError(Exception e) {
                        String message = "Internal error. Extraction of a value of the field '" + fieldName
                            + "' failed for the part with ID="
                            + getId() + ". Does JPA entity declares this field? Details: " + e.getMessage();
                        log.warn(message);
                    }
                });

            }
        }

    }
    //</editor-fold>

    @Override
    public int compareTo(Part o) {
        return ObjectUtils.compare(this.id, o.id);
    }

    @Override
    public String toString() {
        return getClass().toString() + "#" + id;
    }

}
