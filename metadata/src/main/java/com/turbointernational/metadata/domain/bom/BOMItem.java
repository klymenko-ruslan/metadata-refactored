package com.turbointernational.metadata.domain.bom;
import com.turbointernational.metadata.domain.part.Part;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(table="BOM")
public class BOMItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name="parent_part_id")
    private Part parent;

    @OneToOne
    @JoinColumn(name="child_part_id")
    private Part child;

    @Column(nullable=false)
    private Integer quantity;

    @OneToMany(mappedBy="bomItem", cascade = CascadeType.ALL)
    private List<BOMAlternative> alternatives;
    
}
