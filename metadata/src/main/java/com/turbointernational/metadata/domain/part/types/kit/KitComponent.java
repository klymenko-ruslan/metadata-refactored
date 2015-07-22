package com.turbointernational.metadata.domain.part.types.kit;
import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.domain.part.Part;
import com.turbointernational.metadata.domain.part.types.Kit;
import com.turbointernational.metadata.web.View;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.io.Serializable;
import javax.persistence.CascadeType;
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

@Entity
@Table(name="kit_part_common_component", uniqueConstraints=@UniqueConstraint(columnNames={"kit_id", "part_id"}))
public class KitComponent implements Serializable {

    //<editor-fold defaultstate="collapsed" desc="properties">
    @Id
    @JsonView({View.Summary.class, View.Detail.class})
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne(fetch = FetchType.EAGER, cascade = {CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name="kit_id")
    private Kit kit;
    
    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name="part_id")
    @JsonView(View.Summary.class)
    private Part part;
    
    @JsonView(View.Summary.class)
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
