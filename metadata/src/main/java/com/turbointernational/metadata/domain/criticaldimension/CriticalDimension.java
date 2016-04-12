package com.turbointernational.metadata.domain.criticaldimension;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.domain.type.PartType;
import com.turbointernational.metadata.web.View;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by dmytro.trunykov@zorallabs.com on 06.04.16.
 */
@Cacheable
@Entity
@Table(name = "CRIT_DIM")
@NamedQueries({
    @NamedQuery(
            name = "findCriticalDimensionsForPartType",
            query = "FROM CriticalDimension WHERE partType.id=:partTypeId ORDER BY seqNum ASC"
    )
})
public class CriticalDimension implements Serializable {

    //<editor-fold defaultstate="collapsed" desc="Enumerations">
    public enum DataTypeEnum { DECIMAL, ENUMERATION, INTEGER, TEXT }
    public enum UnitEnum { DEGREES, GRAMS, INCHES }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Properties">
    @Id
    @JsonView({View.Summary.class})
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "part_type_id")
    @JsonView({View.Detail.class})
    private PartType partType;

    @Column(name = "seq_num", nullable = false)
    @JsonView({View.Summary.class})
    private int seqNum;

    @Column(name = "data_type", nullable = false)
    @Enumerated(EnumType.STRING)
    @JsonView({View.Summary.class})
    private DataTypeEnum dataType;

    @Column(name = "json_enum", nullable = false)
    @JsonView({View.Summary.class})
    private String jsonEnum;

    @Column(name = "unit")
    @Enumerated(EnumType.STRING)
    @JsonView({View.Summary.class})
    private UnitEnum unit;

    @Column(name = "tolerance")
    @JsonView({View.Summary.class})
    private Boolean tolerance;

    @Column(name = "name", nullable = false)
    @JsonView({View.Summary.class})
    private String name;

    @Column(name = "json_name", nullable = false)
    @JsonView({View.Summary.class})
    private String jsonName;

    @Column(name = "null_allowed", nullable = false)
    @JsonView({View.Summary.class})
    private boolean nullAllowed;

    @Column(name = "null_display")
    @JsonView({View.Summary.class})
    private String nullDisplay;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name="parent_id")
    @JsonView({View.Detail.class})
    private CriticalDimension parent;

    @Column(name = "length")
    @JsonView({View.Summary.class})
    private Byte length;

    @Column(name = "scale")
    @JsonView({View.Summary.class})
    private Byte scale;
    //</editor-fold>

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

    public String getJsonEnum() {
        return jsonEnum;
    }

    public void setJsonEnum(String jsonEnum) {
        this.jsonEnum = jsonEnum;
    }

    public UnitEnum getUnit() {
        return unit;
    }

    public void setUnit(UnitEnum unit) {
        this.unit = unit;
    }

    public Boolean getTolerance() {
        return tolerance;
    }

    public void setTolerance(Boolean tolerance) {
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
    //</editor-fold>

}
