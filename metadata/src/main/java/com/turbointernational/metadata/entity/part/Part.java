package com.turbointernational.metadata.entity.part;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.ALWAYS;
import static com.fasterxml.jackson.annotation.JsonTypeInfo.As.PROPERTY;
import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.CLASS;
import static javax.persistence.CascadeType.REFRESH;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;
import static javax.persistence.InheritanceType.JOINED;
import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;

import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.entity.CriticalDimension;
import com.turbointernational.metadata.entity.Manufacturer;
import com.turbointernational.metadata.entity.PartType;
import com.turbointernational.metadata.entity.SalesNotePart;
import com.turbointernational.metadata.entity.TurboType;
import com.turbointernational.metadata.entity.part.types.Actuator;
import com.turbointernational.metadata.entity.part.types.Backplate;
import com.turbointernational.metadata.entity.part.types.BackplateSealplate;
import com.turbointernational.metadata.entity.part.types.BearingHousing;
import com.turbointernational.metadata.entity.part.types.BoltScrew;
import com.turbointernational.metadata.entity.part.types.CarbonSeal;
import com.turbointernational.metadata.entity.part.types.Cartridge;
import com.turbointernational.metadata.entity.part.types.Clamp;
import com.turbointernational.metadata.entity.part.types.CompressorCover;
import com.turbointernational.metadata.entity.part.types.CompressorWheel;
import com.turbointernational.metadata.entity.part.types.FastWearingComponent;
import com.turbointernational.metadata.entity.part.types.Fitting;
import com.turbointernational.metadata.entity.part.types.Gasket;
import com.turbointernational.metadata.entity.part.types.GasketKit;
import com.turbointernational.metadata.entity.part.types.HeatshieldShroud;
import com.turbointernational.metadata.entity.part.types.JournalBearing;
import com.turbointernational.metadata.entity.part.types.JournalBearingSpacer;
import com.turbointernational.metadata.entity.part.types.Kit;
import com.turbointernational.metadata.entity.part.types.MajorComponent;
import com.turbointernational.metadata.entity.part.types.MinorComponent;
import com.turbointernational.metadata.entity.part.types.Misc;
import com.turbointernational.metadata.entity.part.types.MiscMinorComponent;
import com.turbointernational.metadata.entity.part.types.NozzleRing;
import com.turbointernational.metadata.entity.part.types.Nut;
import com.turbointernational.metadata.entity.part.types.ORing;
import com.turbointernational.metadata.entity.part.types.OilDeflector;
import com.turbointernational.metadata.entity.part.types.P;
import com.turbointernational.metadata.entity.part.types.Pin;
import com.turbointernational.metadata.entity.part.types.PistonRing;
import com.turbointernational.metadata.entity.part.types.Plug;
import com.turbointernational.metadata.entity.part.types.RetainingRing;
import com.turbointernational.metadata.entity.part.types.SealPlate;
import com.turbointernational.metadata.entity.part.types.Shroud;
import com.turbointernational.metadata.entity.part.types.Spring;
import com.turbointernational.metadata.entity.part.types.ThrustBearing;
import com.turbointernational.metadata.entity.part.types.ThrustCollar;
import com.turbointernational.metadata.entity.part.types.ThrustPart;
import com.turbointernational.metadata.entity.part.types.ThrustSpacer;
import com.turbointernational.metadata.entity.part.types.ThrustWasher;
import com.turbointernational.metadata.entity.part.types.TurbineHousing;
import com.turbointernational.metadata.entity.part.types.TurbineWheel;
import com.turbointernational.metadata.entity.part.types.Turbo;
import com.turbointernational.metadata.entity.part.types.Washer;
import com.turbointernational.metadata.service.InterchangeService;
import com.turbointernational.metadata.service.SearchService;
import com.turbointernational.metadata.service.SearchableEntity;
import com.turbointernational.metadata.util.View;
import com.turbointernational.metadata.web.dto.Interchange;

import flexjson.JSONSerializer;
import flexjson.transformer.HibernateTransformer;
import flexjson.transformer.Transformer;

@Entity
@Table(name = "part")
@Inheritance(strategy = JOINED)
//@DiscriminatorColumn(name="part_type_id", discriminatorType= INTEGER )
@Component
@Scope(SCOPE_PROTOTYPE)
@JsonTypeInfo(use = CLASS, property = "class", include = PROPERTY, defaultImpl = Part.class)
@JsonSubTypes({
        @JsonSubTypes.Type(Actuator.class),
        @JsonSubTypes.Type(Backplate.class),
        @JsonSubTypes.Type(BackplateSealplate.class),
        @JsonSubTypes.Type(BearingHousing.class),
        @JsonSubTypes.Type(BoltScrew.class),
        @JsonSubTypes.Type(CarbonSeal.class),
        @JsonSubTypes.Type(Cartridge.class),
        @JsonSubTypes.Type(Clamp.class),
        @JsonSubTypes.Type(CompressorCover.class),
        @JsonSubTypes.Type(CompressorWheel.class),
        @JsonSubTypes.Type(FastWearingComponent.class),
        @JsonSubTypes.Type(Fitting.class),
        @JsonSubTypes.Type(Gasket.class),
        @JsonSubTypes.Type(GasketKit.class),
        @JsonSubTypes.Type(HeatshieldShroud.class),
        @JsonSubTypes.Type(JournalBearing.class),
        @JsonSubTypes.Type(JournalBearingSpacer.class),
        @JsonSubTypes.Type(Kit.class),
        @JsonSubTypes.Type(MajorComponent.class),
        @JsonSubTypes.Type(MinorComponent.class),
        @JsonSubTypes.Type(Misc.class),
        @JsonSubTypes.Type(MiscMinorComponent.class),
        @JsonSubTypes.Type(NozzleRing.class),
        @JsonSubTypes.Type(Nut.class),
        @JsonSubTypes.Type(OilDeflector.class),
        @JsonSubTypes.Type(ORing.class),
        @JsonSubTypes.Type(P.class),
        @JsonSubTypes.Type(Pin.class),
        @JsonSubTypes.Type(PistonRing.class),
        @JsonSubTypes.Type(Plug.class),
        @JsonSubTypes.Type(RetainingRing.class),
        @JsonSubTypes.Type(SealPlate.class),
        @JsonSubTypes.Type(Shroud.class),
        @JsonSubTypes.Type(Spring.class),
        @JsonSubTypes.Type(ThrustBearing.class),
        @JsonSubTypes.Type(ThrustCollar.class),
        @JsonSubTypes.Type(ThrustPart.class),
        @JsonSubTypes.Type(ThrustSpacer.class),
        @JsonSubTypes.Type(ThrustWasher.class),
        @JsonSubTypes.Type(TurbineHousing.class),
        @JsonSubTypes.Type(TurbineWheel.class),
        @JsonSubTypes.Type(Turbo.class),
        @JsonSubTypes.Type(Washer.class),
})
@NamedQueries({
        @NamedQuery(
                name = "findByPartNumber",
                query = "SELECT o FROM Part o WHERE o.manufacturerPartNumber = :partNumber"
        ),
        @NamedQuery(
                name = "findByPartNumberAndManufacturer",
                query = "SELECT o FROM Part o WHERE o.manufacturer.id=:manufacturerId AND o.manufacturerPartNumber = :partNumber"
        ),
        @NamedQuery(
                name = "findAllPartsOrderedById",
                query = "SELECT o FROM Part o ORDER BY o.id"
        ),
        @NamedQuery(
                name = "findPartsByIds",
                query = "SELECT DISTINCT o FROM Part o WHERE o.id IN :ids ORDER BY o.manufacturerPartNumber"
        ),
        @NamedQuery(  // TODO: move usage to PartDao
                name = "findPartsByMnfrsAndNumbers",
                query = "SELECT DISTINCT o " +
                        "FROM Part o WHERE o.manufacturer.id=:mnfrId AND o.manufacturerPartNumber IN(:mnfrPrtNmbrs)"
        ),
        @NamedQuery(
                name = "numPartsOfManufacturer",
                query = "SELECT COUNT(o) FROM Part o WHERE o.manufacturer.id=:manufacturerId"
        )
})
@JsonInclude(ALWAYS)
public abstract class Part implements Comparable<Part>, Serializable, SearchableEntity {

    private static final long serialVersionUID = 5769786866644880614L;

    private static final Logger log = LoggerFactory.getLogger(Part.class);

    //<editor-fold defaultstate="collapsed" desc="Properties">
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @JsonView({View.Summary.class})
    private Long id;

    @OneToOne(fetch = LAZY)
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

    @Column(name = "dim_length")
    @JsonView({View.Summary.class})
    private Double dimLength;

    @Column(name = "dim_width")
    @JsonView({View.Summary.class})
    private Double dimWidth;

    @Column(name = "dim_height")
    @JsonView({View.Summary.class})
    private Double dimHeight;

    @Column(name = "weight")
    @JsonView({View.Summary.class})
    private Double weight;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "part_type_id")
    @JsonView({View.Summary.class})
    private PartType partType;

    @Column(name = "inactive", nullable = false, columnDefinition = "BIT", length = 1)
    @JsonView({View.Detail.class})
    private Boolean inactive = false;

    @OneToMany(fetch = LAZY)
    @JoinTable(name = "part_turbo_type",
            joinColumns = @JoinColumn(name = "part_id"),
            inverseJoinColumns = @JoinColumn(name = "turbo_type_id"))
    @JsonView({View.Detail.class})
    private List<TurboType> turboTypes = new ArrayList<>();

    @OneToMany(cascade = REFRESH, mappedBy = "part", fetch = LAZY)
    @JsonView({View.Detail.class})
    @OrderBy("id")
    private List<ProductImage> productImages = new ArrayList<>();

    @OneToMany(cascade = REFRESH, mappedBy = "pk.part", fetch = LAZY)
    @OrderBy("pk.salesNote.id")
    private List<SalesNotePart> salesNoteParts = new ArrayList<>();

    @Column(name = "import_pk")
    private Long importPk;

    @Version
    @Column(name = "version")
    @JsonView(View.Summary.class)
    @JsonInclude(ALWAYS)
    private int version;

    @Column(name = "legend_img_filename")
    @JsonView(View.Summary.class)
    private String legendImgFilename;

    /**
     * Interchangeable of a part.
     *
     * This field must be initialized manually where it is needed because information about
     * interchanges is stored in other storage (GraphDB).
     */
    @Transient
    private Interchange interchange = null;

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

    public Double getDimLength() {
        return dimLength;
    }

    public void setDimLength(Double dimLength) {
        this.dimLength = dimLength;
    }

    public Double getDimWidth() {
        return dimWidth;
    }

    public void setDimWidth(Double dimWidth) {
        this.dimWidth = dimWidth;
    }

    public Double getDimHeight() {
        return dimHeight;
    }

    public void setDimHeight(Double dimHeight) {
        this.dimHeight = dimHeight;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
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

    @JsonView({View.Detail.class})
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public Interchange getInterchange() {
        return interchange;
    }

    public void setInterchange(Interchange interchange) {
        this.interchange = interchange;
    }

    public List<TurboType> getTurboTypes() {
        return turboTypes;
    }

    public List<ProductImage> getProductImages() {
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

    public String getLegendImgFilename() {
        return legendImgFilename;
    }

    public void setLegendImgFilename(String legendImgFilename) {
        this.legendImgFilename = legendImgFilename;
    }

    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Utilities">

    /**
     * Create an instance of a Part descendant with specified part type.
     *
     * @param partTypeId part type ID
     * @return
     */
    public static Part newInstance(Long partTypeId) {
        Part retVal;
        switch (partTypeId.intValue()) {
            case 1:
                retVal = new Turbo();
                break;
            case 2:
                retVal = new Cartridge();
                break;
            case 3:
                retVal = new Kit();
                break;
            case 4:
                retVal = new PistonRing();
                break;
            case 5:
                retVal = new JournalBearing();
                break;
            case 6:
                retVal = new Gasket();
                break;
            case 8:
                retVal = new FastWearingComponent();
                break;
            case 9:
                retVal =  new MajorComponent();
                break;
            case 10:
                retVal = new MinorComponent();
                break;
            case 11:
                retVal = new CompressorWheel();
                break;
            case 12:
                retVal = new TurbineWheel();
                break;
            case 13:
                retVal = new BearingHousing();
                break;
            case 14:
                retVal = new BackplateSealplate();
                break;
            case 15:
                retVal = new HeatshieldShroud();
                break;
            case 16:
                retVal = new NozzleRing();
                break;
            case 17:
                retVal = new ORing();
                break;
            case 18:
                retVal = new OilDeflector();
                break;
            case 19:
                retVal = new Clamp();
                break;
            case 20:
                retVal = new ThrustPart();
                break;
            case 21:
                retVal = new MiscMinorComponent();
                break;
            case 30:
                retVal = new Actuator();
                break;
            case 31:
                retVal = new CompressorWheel();
                break;
            case 32:
                retVal = new Plug();
                break;
            case 33:
                retVal = new TurbineWheel();
                break;
            case 34:
                retVal = new Backplate();
                break;
            case 35:
                retVal = new BoltScrew();
                break;
            case 36:
                retVal = new Fitting();
                break;
            case 37:
                retVal = new JournalBearingSpacer();
                break;
            case 38:
                retVal = new Nut();
                break;
            case 39:
                retVal = new Pin();
                break;
            case 40:
                retVal = new RetainingRing();
                break;
            case 41:
                retVal = new SealPlate();
                break;
            case 42:
                retVal = new Spring();
                break;
            case 43:
                retVal = new ThrustBearing();
                break;
            case 44:
                retVal = new ThrustCollar();
                break;
            case 45:
                retVal = new ThrustSpacer();
                break;
            case 46:
                retVal = new ThrustWasher();
                break;
            case 47:
                retVal = new Washer();
                break;
            case 48:
                retVal = new CarbonSeal();
                break;
            case 49:
                retVal = new GasketKit();
                break;
            case 50:
                retVal = new Misc();
                break;
            case 51:
                retVal = new P();
                break;
            case 52:
                retVal = new Shroud();
                break;
            default:
                throw new AssertionError("Unsupported part type: " + partTypeId);
        }
        return retVal;
    }

    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Lifecycle">
    @PostRemove
    @Override
    public void removeSearchIndex() throws Exception {
        log.info("Updating search index: delete [{}] {} {}", id,
                partType == null ? "" : partType.getName(), manufacturerPartNumber);
        SearchService.instance().deletePart(this);
    }

    @PostUpdate
    @PostPersist
    @Override
    public void updateSearchIndex() throws Exception {
        log.info("Updating search index: update [{}] {} {}", id,
                partType == null ? "" : partType.getName(), manufacturerPartNumber);
        SearchService.instance().indexPart(this);
    }

    @Override
    public void beforeIndexing(InterchangeService interchangeService) {
        interchangeService.initInterchange(this);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Serialization">

    protected JSONSerializer buildJSONSerializer(List<CriticalDimension> criticalDimensions) {
        JSONSerializer jsonSerializer = new JSONSerializer()
                .transform(new HibernateTransformer(), this.getClass())
                .include("name")
                .include("description")
                .include("active")
                .include("dimLength")
                .include("dimWidth")
                .include("dimHeight")
                .include("weight")
                .include("turboTypes.id")
                .include("turboTypes.name")
                .include("partType.id")
                .include("partType.name")
                .include("manufacturer.id")
                .include("manufacturer.name")
                .exclude("manufacturer.*")
                .include("interchange.id")
                .include("interchange.parts.partId")
                .include("interchange.parts.partNumber")
                .include("interchange.parts.*");
        // Add critical dimensions.
        addCriticalDimensionsToSerialization(criticalDimensions, jsonSerializer, false);
        return jsonSerializer;
    }

    public String toJson(List<CriticalDimension> criticalDimensions) {
        return buildJSONSerializer(criticalDimensions)
                .exclude("*.class")
                .serialize(this);
    }

    protected JSONSerializer buildJsonSerializerSearch(List<CriticalDimension> criticalDimensions) {
        JSONSerializer jsonSerializer = new JSONSerializer()
            .include("id")
            .include("name")
            .include("manufacturerPartNumber")
            .include("description")
            .include("dimLength")
            .include("dimWidth")
            .include("dimHeight")
            .include("weight")
            .include("inactive")
            .include("partType.id")
            .include("partType.name")
            .exclude("partType.*")
            .include("manufacturer.id")
            .include("manufacturer.name")
            .exclude("manufacturer.*")
            .include("interchange.id")
            .include("interchange.parts.partId")
            .include("interchange.parts.partNumber")
            .exclude("interchange.parts.*")
            .exclude("turbos")
            .exclude("*.class");
        // Add critical dimensions.
        addCriticalDimensionsToSerialization(criticalDimensions, jsonSerializer, true);
        return jsonSerializer;
    }

    @Override
    public final String toSearchJson(List<CriticalDimension> criticalDimensions) {
        JSONSerializer jsonSerializer = buildJsonSerializerSearch(criticalDimensions);
        String json = jsonSerializer.exclude("*").serialize(this);
        return json;
    }

    /**
     * Add critical dimensions to a serialized part.
     *
     * @param criticalDimensions
     * @param jsonSerializer
     * @param searchSerialization true if a part is serialized to a search engine.
     * @see com.turbointernational.metadata.service.CriticalDimensionService#JsonIdxNameTransformer
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
