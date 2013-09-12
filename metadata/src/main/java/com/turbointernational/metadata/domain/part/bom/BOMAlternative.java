package com.turbointernational.metadata.domain.part.bom;
import com.turbointernational.metadata.domain.part.Part;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;

@Configurable
@Entity
@RooJpaActiveRecord(table="bom_alt_item")
@RooJson
public class BOMAlternative {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = {CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name="bom_id")
    private BOMItem bomItem;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="bom_alt_header_id")
    private BOMAlternativeHeader header;

    @OneToOne(cascade = {CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name="part_id")
    private Part part;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BOMItem getBomItem() {
        return bomItem;
    }

    public void setBomItem(BOMItem bomItem) {
        this.bomItem = bomItem;
    }

    public BOMAlternativeHeader getHeader() {
        return header;
    }

    public void setHeader(BOMAlternativeHeader header) {
        this.header = header;
    }

    public Part getPart() {
        return part;
    }

    public void setPart(Part part) {
        this.part = part;
    }

}
