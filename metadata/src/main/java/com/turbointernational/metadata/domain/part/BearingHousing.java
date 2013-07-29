package com.turbointernational.metadata.domain.part;
import com.turbointernational.metadata.domain.type.CoolType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(table="BEARING_HOUSING", inheritanceType = "JOINED")
@DiscriminatorValue(value = "2")
@PrimaryKeyJoinColumn(name = "part_id")
public class BearingHousing extends Part {
    
    @ManyToOne
    @JoinColumn(name="cool_type_id")
    private CoolType coolType;

    @Column(name="oil_inlet")
    private String oilInlet;

    @Column(name="oil_outlet")
    private String oilOutlet;

    @Column(name="oil")
    private String oil;

    @Column(name="outlet_flange_holes")
    private String outletFlangeHoles;

    @Column(name="water_ports")
    private String waterPorts;

    @Column(name="design_features")
    private String designFeatures;

    @Column(name="bearing_type")
    private String bearingType;
}
