package com.turbointernational.metadata.domain.criticaldimension;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.domain.type.PartType;
import com.turbointernational.metadata.web.View;
import flexjson.transformer.Transformer;

import javax.persistence.*;
import java.io.Serializable;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.ALWAYS;
import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.EAGER;
import static javax.persistence.FetchType.LAZY;

/**
 * Created by dmytro.trunykov@zorallabs.com on 06.04.16.
 */
@Cacheable
@Entity
@Table(name = "crit_dim")
@NamedQueries({
    @NamedQuery(
            name = "findCriticalDimensionsForPartType",
            query = "FROM CriticalDimension WHERE partType.id=:partTypeId ORDER BY seqNum ASC"
    )
})
@JsonInclude(ALWAYS)
public class CriticalDimension implements Serializable {

    //<editor-fold defaultstate="collapsed" desc="Enumerations">
    public enum DataTypeEnum { DECIMAL, ENUMERATION, INTEGER, TEXT }
    public enum UnitEnum { DEGREES, GRAMS, INCHES }
    public enum ToleranceTypeEnum { LOWER, UPPER, BOTH }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Properties">
    @Id
    @JsonView({View.Summary.class})
    private Long id;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "part_type_id")
    @JsonView({View.Detail.class})
    private PartType partType;

    @Column(name = "seq_num", nullable = false)
    @JsonView({View.Summary.class})
    private int seqNum;

    @Column(name = "data_type", nullable = false)
    @Enumerated(STRING)
    @JsonView({View.Summary.class})
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private DataTypeEnum dataType;

    @OneToOne(fetch = EAGER)
    @JoinColumn(name = "enum_id")
    @JsonView({View.Summary.class})
    private CriticalDimensionEnum enumeration;

    @Column(name = "unit")
    @Enumerated(STRING)
    @JsonView({View.Summary.class})
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private UnitEnum unit;

    @Column(name = "tolerance")
    @Enumerated(STRING)
    @JsonView({View.Summary.class})
    private ToleranceTypeEnum tolerance;

    @Column(name = "name", nullable = false)
    @JsonView({View.Summary.class})
    private String name;

    /**
     * Name of a property in serialized to JSON part''s object. It must be the exact name of the property
     * in JPA entity. So we can use reflection to get value of the critical dimension in a part instance at runtime.
     */
    @Column(name = "json_name", nullable = false)
    @JsonView({View.Summary.class})
    private String jsonName;

    /**
     * Name of a property in the ElasticSearch mapping.
     *
     * In theory parts of different types can have critical dimensions with the same name.
     * But properties in the index definition for ElasticSearch must be unique.
     * So we introduce this member as compliment to {@link #jsonName}.
     */
    @Column(name = "idx_name", nullable = false, unique = true)
    @JsonView({View.Summary.class})
    private String idxName;

    @Column(name = "null_allowed", nullable = false)
    @JsonView({View.Summary.class})
    private boolean nullAllowed;

    @Column(name = "null_display")
    @JsonView({View.Summary.class})
    private String nullDisplay;

    @Column(name = "min_val")
    @JsonView({View.Summary.class})
     private Double minVal;

    @Column(name = "max_val")
    @JsonView({View.Summary.class})
    private Double maxVal;

    @Column(name = "regex")
    @JsonView({View.Summary.class})
    private String regex;

    @OneToOne(fetch = EAGER)
    @JoinColumn(name="parent_id")
    @JsonView({View.Summary.class})
    private CriticalDimension parent;

    @Column(name = "length")
    @JsonView({View.Summary.class})
    private Byte length;

    @Column(name = "scale")
    @JsonView({View.Summary.class})
    private Byte scale;

    @Column(name = "length_web")
    @JsonView({View.Summary.class})
    private Byte lengthWeb;

    @Column(name = "scale_web")
    @JsonView({View.Summary.class})
    private Byte scaleWeb;
    //</editor-fold>

    /**
     * Transformer for properties name during serialization to JSON for ElasticSearch.
     *
     * Because name of a property in a Part instance that holds critical dimension value
     * can have different name in an ElasticSearch index we use this transformer to
     * convert {@link #jsonName} => {@link #idxName}.
     *
     * In case when {@link #jsonName} is equal to {@link #idxName} this property is null.
     */
    @Transient
    private Transformer jsonIdxNameTransformer;

    //<editor-fold defaultstate="collapsed" desc="Getters and Setters">
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PartType getPartType() {
        return partType;
    }

    public void setPartType(PartType partType) {
        this.partType = partType;
    }

    public int getSeqNum() {
        return seqNum;
    }

    public void setSeqNum(int seqNum) {
        this.seqNum = seqNum;
    }

    public DataTypeEnum getDataType() {
        return dataType;
    }

    public void setDataType(DataTypeEnum dataType) {
        this.dataType = dataType;
    }

    public CriticalDimensionEnum getEnumeration() {
        return enumeration;
    }

    public void setJsonEnum(CriticalDimensionEnum enumeration) {
        this.enumeration = enumeration;
    }

    public UnitEnum getUnit() {
        return unit;
    }

    public void setUnit(UnitEnum unit) {
        this.unit = unit;
    }

    public ToleranceTypeEnum getTolerance() {
        return tolerance;
    }

    public void setTolerance(ToleranceTypeEnum tolerance) {
        this.tolerance = tolerance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJsonName() {
        return jsonName;
    }

    public void setJsonName(String jsonName) {
        this.jsonName = jsonName;
    }

    public String getIdxName() {
        return idxName;
    }

    public void setIdxName(String idxName) {
        this.idxName = idxName;
    }

    public boolean isNullAllowed() {
        return nullAllowed;
    }

    public void setNullAllowed(boolean nullAllowed) {
        this.nullAllowed = nullAllowed;
    }

    public String getNullDisplay() {
        return nullDisplay;
    }

    public void setNullDisplay(String nullDisplay) {
        this.nullDisplay = nullDisplay;
    }

    public CriticalDimension getParent() {
        return parent;
    }

    public void setParent(CriticalDimension parent) {
        this.parent = parent;
    }

    public Byte getLength() {
        return length;
    }

    public void setLength(Byte length) {
        this.length = length;
    }

    public Byte getScale() {
        return scale;
    }

    public void setScale(Byte scale) {
        this.scale = scale;
    }

    public Double getMinVal() {
        return minVal;
    }

    public void setMinVal(Double minVal) {
        this.minVal = minVal;
    }

    public Double getMaxVal() {
        return maxVal;
    }

    public void setMaxVal(Double maxVal) {
        this.maxVal = maxVal;
    }

    public String getRegex() {
        return regex;
    }

    public void setRegex(String regex) {
        this.regex = regex;
    }

    public void setEnumeration(CriticalDimensionEnum enumeration) {
        this.enumeration = enumeration;
    }

    public Byte getLengthWeb() {
        return lengthWeb;
    }

    public void setLengthWeb(Byte lengthWeb) {
        this.lengthWeb = lengthWeb;
    }

    public Byte getScaleWeb() {
        return scaleWeb;
    }

    public void setScaleWeb(Byte scaleWeb) {
        this.scaleWeb = scaleWeb;
    }

    public Transformer getJsonIdxNameTransformer() {
        return jsonIdxNameTransformer;
    }

    public void setJsonIdxNameTransformer(Transformer jsonIdxNameTransformer) {
        this.jsonIdxNameTransformer = jsonIdxNameTransformer;
    }

//</editor-fold>

}
