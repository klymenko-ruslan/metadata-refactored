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
@NamedQueries({
        @NamedQuery(name = "findAllPartTypes", query = "SELECT pt FROM PartType pt ORDER BY pt.name"),
        @NamedQuery(name = "findPartTypeByValue", query = "SELECT pt FROM PartType pt WHERE pt.value=:value")
})
@Table(name = "part_type")
public class PartType implements Serializable {

    private static final long serialVersionUID = 8084028725671309821L;

    // PTID_XXXX constants are IDs of part types as they defined in the table 'part_type'.
    // These constants can be used where parts ids needed, e.g. in SQL queries.
    public final static int PTID_TURBO = 1;
    public final static int PTID_CARTRIDGE = 2;
    public final static int PTID_KIT = 3;
    public final static int PTID_PISTON_RING = 4;
    public final static int PTID_JOURNAL_BEARING = 5;
    public final static int PTID_GASKET = 6;
    public final static int PTID_FAST_WEARING_COMPONENT = 8;
    public final static int PTID_MAJOR_COMPONENT = 9;
    public final static int PTID_MINOR_COMPONENT = 10;
    public final static int PTID_COMPRESSOR_WHEEL = 11;
    public final static int PTID_TURBINE_WHEEL = 12;
    public final static int PTID_BEARING_HOUSING = 13;
    public final static int PTID_BACKPLATE_SEALPLATE = 14;
    public final static int PTID_HEATSHIELD_SHROUD = 15;
    public final static int PTID_NOZZLE_RING = 16;
    public final static int PTID_O_RING = 17;
    public final static int PTID_OIL_DEFLECTOR = 18;
    public final static int PTID_CLAMP = 19;
    public final static int PTID_THRUST_PARTS = 20;
    public final static int PTID_MISCELLANEOUS_MINOR_COMPONENTS = 21;
    public final static int PTID_ACTUATOR = 30;
    public final static int PTID_COMPRESSOR_COVER = 31;
    public final static int PTID_PLUG = 32;
    public final static int PTID_TURBINE_HOUSING = 33;
    public final static int PTID_BACKPLATE = 34;
    public final static int PTID_BOLT_SCREW = 35;
    public final static int PTID_FITTING = 36;
    public final static int PTID_JOURNAL_BEARING_SPACER = 37;
    public final static int PTID_NUT = 38;
    public final static int PTID_PIN = 39;
    public final static int PTID_RETAINING_RING = 40;
    public final static int PTID_SEAL_PLATE = 41;
    public final static int PTID_SPRING = 42;
    public final static int PTID_THRUST_BEARING = 43;
    public final static int PTID_THRUST_COLLAR = 44;
    public final static int PTID_THRUST_SPACER = 45;
    public final static int PTID_THRUST_WASHER = 46;
    public final static int PTID_WASHER = 47;
    public final static int PTID_CARBON_SEAL = 48;
    public final static int PTID_GASKET_KIT = 49;
    public final static int PTID_MISC = 50;
    public final static int PTID_PART = 51;
    public final static int PTID_SHROUD = 52;

    //<editor-fold defaultstate="collapsed" desc="properties">
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
    @JsonView({View.Detail.class})
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

}
