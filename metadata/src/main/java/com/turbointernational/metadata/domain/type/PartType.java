package com.turbointernational.metadata.domain.type;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;

@Cacheable
@Configurable
@Entity
@RooJpaActiveRecord(table = "PART_TYPE")
@RooJson
public class PartType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false)
    private String name;

    @Column(name="DTYPE", nullable=false)
    private String typeName;

    // TODO: Is this a duplicate of parentTypes?
    @OneToOne
    @JoinColumn(name="parent_part_type_id")
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

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public PartType getParent() {
        return parent;
    }

    public void setParent(PartType parent) {
        this.parent = parent;
    }

}
