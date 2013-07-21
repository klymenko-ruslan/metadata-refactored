package com.turbointernational.metadata.domain.bom;
import com.turbointernational.metadata.domain.part.Part;
import java.util.Map;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.MapKeyClass;
import javax.persistence.MapKeyColumn;
import javax.persistence.MapKeyJoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(table="BOM")
public class BOMItem {

    @OneToOne
    @JoinColumn(name="parent_part_id")
    private Part parent;

    @OneToOne
    @JoinColumn(name="child_part_id")
    private Part child;

    @Column(nullable=false)
    private Integer quantity;

    @OneToMany(mappedBy="bomItem")
    @MapKeyJoinColumn(name="bom_id", table="BOM_ALT_ITEM")
    private Map<BOMAlternative, BOMAlternativeHeader> alternatives;

    @OneToMany
    @MapKeyJoinColumn(name="child_part_id")
    @MapKeyColumn(name="quantity", nullable=false)
    private Map<Part, Integer> parts;
    
}
