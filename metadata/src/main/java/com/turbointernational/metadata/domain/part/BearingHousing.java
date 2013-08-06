package com.turbointernational.metadata.domain.part;
import com.turbointernational.metadata.domain.type.CoolType;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.SecondaryTable;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
@SecondaryTable(name="BEARING_HOUSING", pkJoinColumns=@PrimaryKeyJoinColumn(name = "part_id"))
public class BearingHousing extends Part {
    
    @ManyToOne
    @JoinColumn(name="cool_type_id", table = "BEARING_HOUSING")
    private CoolType coolType;

    @Column(name="oil_inlet", table = "BEARING_HOUSING")
    private String oilInlet;

    @Column(name="oil_outlet", table = "BEARING_HOUSING")
    private String oilOutlet;

    @Column(name="oil", table = "BEARING_HOUSING")
    private String oil;

    @Column(name="outlet_flange_holes", table = "BEARING_HOUSING")
    private String outletFlangeHoles;

    @Column(name="water_ports", table = "BEARING_HOUSING")
    private String waterPorts;

    @Column(name="design_features", table = "BEARING_HOUSING")
    private String designFeatures;

    @Column(name="bearing_type", table = "BEARING_HOUSING")
    private String bearingType;
}
