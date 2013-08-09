package com.turbointernational.metadata.domain.part;
import com.turbointernational.metadata.domain.type.KitType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.SecondaryTable;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;

@RooJavaBean
@RooJpaActiveRecord
@SecondaryTable(name="kit", pkJoinColumns=@PrimaryKeyJoinColumn(name = "part_id"))
public class Kit extends Part {
    @OneToOne
    @JoinColumn(name="kit_type_id", table = "kit")
    private KitType type;
}
