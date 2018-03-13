package com.turbointernational.metadata.entity;

import static javax.persistence.FetchType.EAGER;
import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.util.View;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

@Cacheable
@Entity
@NamedQueries({ @NamedQuery(name = "findAllPartTypes", query = "SELECT pt FROM PartType pt ORDER BY pt.name"),
        @NamedQuery(name = "findPartTypeByValue", query = "SELECT pt FROM PartType pt WHERE pt.value=:value") })
@Table(name = "part_type")
public class PartType implements Serializable {

    private static final long serialVersionUID = 8084028725671309821L;

    public static enum PartTypeEnum {

        // Enumeration of part types as they defined in the table 'part_type'.
        // These constants can be used where parts IDs/table names needed, e.g. in SQL queries.

        //@formatter:off
        TURBO(1, "turbo"), CARTRIDGE(2, "cartridge"), KIT(3, "kit"), PISTON_RING(4, "piston_ring"),
        JOURNAL_BEARING(5, "journal_bearing"), GASKET(6, "gasket"), FAST_WEARING_COMPONENT(8, "fast_wearing_component"),
        MAJOR_COMPONENT(9, "major_component"), MINOR_COMPONENT(10, "minor_component"),
        COMPRESSOR_WHEEL(11, "compressor_wheel"), TURBINE_WHEEL(12, "turbine_wheel"),
        BEARING_HOUSING(13, "bearing_housing"), BACKPLATE_SEALPLATE(14, "backplate_sealplate"),
        HEATSHIELD_SHROUD(15, "heatshield"), NOZZLE_RING(16, "nozzle_ring"), O_RING(17, "o_ring"),
        OIL_DEFLECTOR(18, "oil_deflector"), CLAMP(19, "clamp"), THRUST_PARTS(20, "thrust_part"),
        MISCELLANEOUS_MINOR_COMPONENTS(21, "misc_minor_component"), ACTUATOR(30, "actuator"),
        COMPRESSOR_COVER(31, "compressor_cover"), PLUG(32, "plug"), TURBINE_HOUSING(33, "turbine_housing"),
        BACKPLATE(34, "backplate"), BOLT_SCREW(35, "bolt_screw"), FITTING(36, "fitting"),
        JOURNAL_BEARING_SPACER(37, "journal_bearing_spacer"), NUT(38, "nut"), PIN(39, "pin"),
        RETAINING_RING(40, "retaining_ring"), SEAL_PLATE(41, "seal_plate"), SPRING(42, "spring"),
        THRUST_BEARING(43, "thrust_bearing"), THRUST_COLLAR(44, "thrust_collar"), THRUST_SPACER(45, "thrust_spacer"),
        THRUST_WASHER(46, "thrust_washer"), WASHER(47, "washer"), CARBON_SEAL(48, "carbon_seal"),
        GASKET_KIT(49, "gasket_kit"), MISC(50, "misc"), PART(51, "p"), SHROUD(52, "shroud");
        //@formatter:on

        /**
         * Part type ID.
         */
        public final int id;

        /**
         * Table name.
         */
        public final String table;

        private PartTypeEnum(int id, String table) {
            this.id = id;
            this.table = table;
        }

        public static PartTypeEnum fromId(long id) {
            if (id == ACTUATOR.id) {
                return ACTUATOR;
            } else if (id == BACKPLATE.id) {
                return BACKPLATE;
            } else if (id == BACKPLATE_SEALPLATE.id) {
                return BACKPLATE_SEALPLATE;
            } else if (id == BEARING_HOUSING.id) {
                return BEARING_HOUSING;
            } else if (id == BOLT_SCREW.id) {
                return BOLT_SCREW;
            } else if (id == CARBON_SEAL.id) {
                return CARBON_SEAL;
            } else if (id == CARTRIDGE.id) {
                return CARTRIDGE;
            } else if (id == CLAMP.id) {
                return CLAMP;
            } else if (id == COMPRESSOR_COVER.id) {
                return COMPRESSOR_COVER;
            } else if (id == COMPRESSOR_WHEEL.id) {
                return COMPRESSOR_WHEEL;
            } else if (id == FAST_WEARING_COMPONENT.id) {
                return FAST_WEARING_COMPONENT;
            } else if (id == FITTING.id) {
                return FITTING;
            } else if (id == GASKET.id) {
                return GASKET;
            } else if (id == GASKET_KIT.id) {
                return GASKET_KIT;
            } else if (id == HEATSHIELD_SHROUD.id) {
                return HEATSHIELD_SHROUD;
            } else if (id == JOURNAL_BEARING.id) {
                return JOURNAL_BEARING;
            } else if (id == JOURNAL_BEARING_SPACER.id) {
                return JOURNAL_BEARING_SPACER;
            } else if (id == KIT.id) {
                return KIT;
            } else if (id == MAJOR_COMPONENT.id) {
                return MAJOR_COMPONENT;
            } else if (id == MINOR_COMPONENT.id) {
                return MINOR_COMPONENT;
            } else if (id == MISC.id) {
                return MISC;
            } else if (id == MISCELLANEOUS_MINOR_COMPONENTS.id) {
                return MISCELLANEOUS_MINOR_COMPONENTS;
            } else if (id == NOZZLE_RING.id) {
                return NOZZLE_RING;
            } else if (id == NUT.id) {
                return NUT;
            } else if (id == O_RING.id) {
                return O_RING;
            } else if (id == OIL_DEFLECTOR.id) {
                return OIL_DEFLECTOR;
            } else if (id == PART.id) {
                return PART;
            } else if (id == PIN.id) {
                return PIN;
            } else if (id == PISTON_RING.id) {
                return PISTON_RING;
            } else if (id == PLUG.id) {
                return PLUG;
            } else if (id == RETAINING_RING.id) {
                return RETAINING_RING;
            } else if (id == SEAL_PLATE.id) {
                return SEAL_PLATE;
            } else if (id == SHROUD.id) {
                return SHROUD;
            } else if (id == SPRING.id) {
                return SPRING;
            } else if (id == THRUST_BEARING.id) {
                return THRUST_BEARING;
            } else if (id == THRUST_COLLAR.id) {
                return THRUST_COLLAR;
            } else if (id == THRUST_PARTS.id) {
                return THRUST_PARTS;
            } else if (id == THRUST_SPACER.id) {
                return THRUST_SPACER;
            } else if (id == THRUST_WASHER.id) {
                return THRUST_WASHER;
            } else if (id == TURBINE_HOUSING.id) {
                return TURBINE_HOUSING;
            } else if (id == TURBINE_WHEEL.id) {
                return TURBINE_WHEEL;
            } else if (id == TURBO.id) {
                return TURBO;
            } else if (id == WASHER.id) {
                return WASHER;
            } else {
                throw new AssertionError("Unsupported part type: " + id);
            }
        }

    }

    // <editor-fold defaultstate="collapsed" desc="properties">
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @JsonView(View.Summary.class)
    private Long id;

    @Column(name = "name", nullable = false)
    @JsonView(View.Summary.class)
    private String name;

    @Column(name = "import_pk")
    private Long importPK;

    @Column(name = "legend_img_filename")
    @JsonView(View.Summary.class)
    private String legendImgFilename;

    /**
     * Used externally
     */
    @Column(name = "value", nullable = false, unique = true)
    @JsonView(View.Summary.class)
    private String value;

    @Column(name = "magento_attribute_set")
    @JsonView(View.Summary.class)
    private String magentoAttributeSet;

    // TODO: Is this a duplicate of parentTypes?
    @OneToOne(fetch = EAGER)
    @JoinColumn(name = "parent_part_type_id")
    @JsonView({ View.Detail.class })
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

    public Long getImportPK() {
        return importPK;
    }

    public void setImportPK(Long importPK) {
        this.importPK = importPK;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getMagentoAttributeSet() {
        return magentoAttributeSet;
    }

    public void setMagentoAttributeSet(String magentoName) {
        this.magentoAttributeSet = magentoName;
    }

    public PartType getParent() {
        return parent;
    }

    public void setParent(PartType parent) {
        this.parent = parent;
    }

    public String getLegendImgFilename() {
        return legendImgFilename;
    }

    public void setLegendImgFilename(String legendImgFilename) {
        this.legendImgFilename = legendImgFilename;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="json">
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
        return new JSONDeserializer<List<PartType>>().use(null, ArrayList.class).use("values", PartType.class)
                .deserialize(json);
    }

    // </editor-fold>

}
