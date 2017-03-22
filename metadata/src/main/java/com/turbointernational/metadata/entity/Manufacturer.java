package com.turbointernational.metadata.entity;

import java.io.Serializable;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.util.View;

import flexjson.JSONSerializer;

@Cacheable
@Entity
@Table(name = "manfr", uniqueConstraints = @UniqueConstraint(columnNames = { "name" }))
@NamedQueries({
    @NamedQuery(
            name = "findManufacurerByName",
            query = "from Manufacturer m where m.name=:name")
})
public class Manufacturer implements Serializable {

    private static final long serialVersionUID = 6137179824752987228L;

    public static final Long TI_ID = 11L;

    // <editor-fold defaultstate="collapsed" desc="Properties">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView({ View.Summary.class })
    private Long id;

    @Column(nullable = false, unique = true, length = 255)
    @JsonView({ View.Summary.class })
    private String name;

    @Column(name = "not_external", nullable = false)
    @JsonView({ View.Summary.class })
    private boolean notExternal;

    @OneToOne
    @JoinColumn(name = "manfr_type_id", nullable = false)
    @JsonView({ View.Detail.class })
    private ManufacturerType type;

    @Column(name = "import_pk")
    private Long importPK;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_manfr_id")
    @JsonView({ View.Detail.class })
    private Manufacturer parent;

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

    public boolean isNotExternal() {
        return notExternal;
    }

    public void setNotExternal(boolean notExternal) {
        this.notExternal = notExternal;
    }

    public ManufacturerType getType() {
        return type;
    }

    public void setType(ManufacturerType type) {
        this.type = type;
    }

    public Long getImportPK() {
        return importPK;
    }

    public void setImportPK(Long importPK) {
        this.importPK = importPK;
    }

    public Manufacturer getParent() {
        return parent;
    }

    public void setParent(Manufacturer parent) {
        this.parent = parent;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Serialization">
    public String toJson() {
        return new JSONSerializer().exclude("*.class").serialize(this);
    }
    // </editor-fold>

}
