package com.turbointernational.metadata.domain.part.bom;
import com.turbointernational.metadata.domain.other.Manufacturer;
import com.turbointernational.metadata.domain.part.Part;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;

@Configurable
@Entity
@RooJpaActiveRecord(table="bom")
@RooJson
public class BOMItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name="parent_part_id")
    private Part parent;

    @OneToOne(fetch = FetchType.EAGER, cascade = {CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name="child_part_id")
    private Part child;

    @Column(nullable=false)
    private Integer quantity;

    @OneToMany(mappedBy="bomItem", cascade = {CascadeType.DETACH, CascadeType.REFRESH}, fetch = FetchType.LAZY)
    private Set<BOMAlternative> alternatives;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Part getParent() {
        return parent;
    }

    public void setParent(Part parent) {
        this.parent = parent;
    }
    
    public Part getChild() {
        return child;
    }

    public void setChild(Part child) {
        this.child = child;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Set<BOMAlternative> getAlternatives() {
        return alternatives;
    }

    public void setAlternatives(Set<BOMAlternative> alternatives) {
        this.alternatives = alternatives;
    }
    
    public List<Part> getTIAlternates() {
        List<Part> tiAlts = new ArrayList<Part>();
        
        for (BOMAlternative alt : alternatives) {
            if (Manufacturer.TI_ID.equals(alt.getPart().getManufacturer().getId())) {
                tiAlts.add(alt.getPart());
            }
        }
        
        return tiAlts;
    }
    
    @PrePersist
    @PreUpdate
    public void validate() {
        if (ObjectUtils.equals(child.getId(), parent.getId())) {
            throw new IllegalStateException("Child cannot be it's own parent.");
        }
    }
    
}
