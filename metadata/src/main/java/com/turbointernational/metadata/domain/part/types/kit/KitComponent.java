package com.turbointernational.metadata.domain.part.types.kit;

import com.turbointernational.metadata.domain.part.Part;
import com.turbointernational.metadata.domain.part.types.Kit;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;

@Configurable
@Entity
@Table(name="kit_part_common_component", uniqueConstraints=@UniqueConstraint(columnNames={"kit_id", "part_id"}))
public class KitComponent {

    //<editor-fold defaultstate="collapsed" desc="properties">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne(fetch = FetchType.EAGER, cascade = {CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name="kit_id")
    private Kit kit;
    
    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name="part_id")
    private Part part;
    
    @Column(nullable=false)
    private boolean exclude;
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }

    public Part getPart() {
        return part;
    }

    public void setPart(Part part) {
        this.part = part;
    }

    public Kit getKit() {
        return kit;
    }

    public void setKit(Kit kit) {
        this.kit = kit;
    }

    public boolean isExclude() {
        return exclude;
    }

    public void setExclude(boolean exclude) {
        this.exclude = exclude;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="activerecord">
    @PersistenceContext
    transient EntityManager entityManager;
    
    public static final EntityManager entityManager() {
        EntityManager em = new KitComponent().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
    
    @Transactional
    public void persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }
    
    public static KitComponent find(Long id) {
        if (id == null) return null;
        
        return entityManager()
                .createQuery("SELECT DISTINCT i FROM KitComponent i WHERE id = ?", KitComponent.class)
                .setParameter(1, id)
                .getSingleResult();
    }
    
    
    @Transactional
    public void remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            KitComponent attached = find(this.id);
            this.entityManager.remove(attached);
        }
    }
    
    @Transactional
    public KitComponent merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        KitComponent merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Serialization">
    public String toJson() {
        return new JSONSerializer()
            .include("kit.id")
            .include("kit.name")
            .include("kit.manufacturerPartNumber")
            .include("kit.manufacturer.name")
            .include("part.id")
            .include("part.name")
            .include("part.manufacturerPartNumber")
            .include("part.manufacturer.name")
            .include("exclude")
            .exclude("*").serialize(this);
    }
    
    public static KitComponent fromJson(String json) {
        return new JSONDeserializer<KitComponent>().use(null, KitComponent.class).deserialize(json);
    }
    //</editor-fold>

}
