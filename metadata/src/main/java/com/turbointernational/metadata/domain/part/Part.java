package com.turbointernational.metadata.domain.part;
import com.turbointernational.metadata.domain.other.Interchange;
import com.turbointernational.metadata.domain.other.Manufacturer;
import com.turbointernational.metadata.domain.type.PartType;
import com.turbointernational.metadata.util.ElasticSearch;
import java.util.HashSet;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.persistence.Transient;
import net.sf.jsog.JSOG;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.json.RooJson;

@RooJavaBean
@RooJpaActiveRecord(table="PART", inheritanceType = "SINGLE_TABLE")
@RooJson
public class Part {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "manfr_id", nullable = false)
    private Manufacturer manufacturer;

    @Column(name = "manfr_part_num")
    private String manufacturerPartNumber;

    @Column(name = "name")
    private String name;

    @Column(name="description")
    private String description;

    @OneToOne
    @JoinColumn(name="part_type_id")
    private transient PartType partType;

    @Column(nullable = false, columnDefinition = "BIT", length = 1)
    private Boolean inactive;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinTable(name="interchange_item",
               joinColumns=@JoinColumn(name="part_id"),
               inverseJoinColumns=@JoinColumn(name="interchange_header_id"))
    private Interchange interchange;

    @Transient
    private Part interchangePart;

    @Autowired(required=true)
    @Transient
    private ElasticSearch elasticSearch;

    public void updateInterchanges() throws Exception {

        // Create the interchange if no interchange and a part were specified
        if (interchange == null && interchangePart != null) {

            // Check the other part to make sure it's blank
            if (interchangePart.getInterchange() != null) {
                this.setInterchange(interchangePart.getInterchange());
            } else {
                interchange = new Interchange();
                this.setInterchange(interchange);
                interchange.setName("");
                interchange.persist();
            }
        }

        if (interchange != null) {

            // Create the set if necessary
            if (interchange.getParts() == null) {
                interchange.setParts(new HashSet());
            }

            // Set the bidirectional relationship
            interchange.getParts().add(this);

            if (interchangePart != null && interchange != interchangePart.getInterchange()) {
                interchange.getParts().add(interchangePart);
                interchangePart.setInterchange(interchange);
                interchangePart.merge();
            }

            interchange.merge();
        }

    }

    public void indexPart() throws Exception {
        elasticSearch.indexPart(this);
    }

    public void removeIndex() throws Exception {
        elasticSearch.deletePart(this);
    }

//    @OneToMany(mappedBy="parent", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    @JoinColumn(name="parent_part_id", table="bom")
//    private Collection<BOMItem> bom;

    public void toJson(JSOG partObject) {}
}
