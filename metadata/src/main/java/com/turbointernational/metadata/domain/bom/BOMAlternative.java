package com.turbointernational.metadata.domain.bom;
import com.turbointernational.metadata.domain.part.Part;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;

@RooJavaBean
@RooJpaActiveRecord(table="BOM_ALT_ITEM")
@RooJson
public class BOMAlternative {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name="bom_id")
    private BOMItem bomItem;

    @OneToOne
    @JoinColumn(name="bom_alt_header_id")
    private BOMAlternativeHeader header;

    @OneToOne
    @JoinColumn(name="part_id")
    private Part part;

}
