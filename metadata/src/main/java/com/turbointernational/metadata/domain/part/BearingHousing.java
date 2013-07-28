package com.turbointernational.metadata.domain.part;
import com.turbointernational.metadata.domain.type.CoolType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(table="BEARING_HOUSING", inheritanceType = "JOINED")
public class BearingHousing extends Part {
    
    @ManyToOne
    @JoinColumn(name="cool_type_id")
    private CoolType coolType;

    private String oilInlet;

    private String oilOutlet;

    private String oil;

    private String outletFlangeHoles;

    private String waterPorts;

    private String designFeatures;

    private String bearingType;
}
