package com.turbointernational.metadata.domain.bom;
import com.turbointernational.metadata.domain.part.Part;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;

@RooJavaBean
@RooJpaActiveRecord(table="bom")
public class BOMItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="parent_part_id")
    private Part parent;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="child_part_id")
    private Part child;

    @Column(nullable=false)
    private Integer quantity;

    @OneToMany(mappedBy="bomItem", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<BOMAlternative> alternatives;
    
}
