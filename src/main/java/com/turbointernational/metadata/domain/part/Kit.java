package com.turbointernational.metadata.domain.part;
import com.turbointernational.metadata.domain.type.KitType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(table="KIT", inheritanceType = "JOINED")
@DiscriminatorValue(value = "3")
@PrimaryKeyJoinColumn(name = "part_id")
public class Kit extends Part {
    @ManyToOne
    @JoinColumn(name="kit_type_id")
    private KitType type;
}
