package com.turbointernational.metadata.domain.other;
import com.turbointernational.metadata.domain.type.ManufacturerType;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;

@Cacheable
@Configurable
@Entity
@RooJpaActiveRecord
@RooJson
@Table(name="MANFR", uniqueConstraints=@UniqueConstraint(columnNames={"name"}))
public class Manufacturer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false)
    private String name;

    @OneToOne
    @JoinColumn(name="manfr_type_id", nullable=false)
    private ManufacturerType type;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="parent_manfr_id")
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

    public ManufacturerType getType() {
        return type;
    }

    public void setType(ManufacturerType type) {
        this.type = type;
    }

    public Manufacturer getParent() {
        return parent;
    }

    public void setParent(Manufacturer parent) {
        this.parent = parent;
    }
}
