package com.turbointernational.metadata.domain.part;
import com.turbointernational.metadata.domain.type.CoolType;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.SecondaryTable;
import net.sf.jsog.JSOG;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;

@RooJavaBean
@RooJpaActiveRecord
@RooJson
@SecondaryTable(name="bearing_housing", pkJoinColumns=@PrimaryKeyJoinColumn(name = "part_id"))
public class BearingHousing extends Part {
    
    @OneToOne
    @JoinColumn(name="cool_type_id", table = "bearing_housing")
    private CoolType coolType;

    @Column(name="oil_inlet", table = "bearing_housing")
    private String oilInlet;

    @Column(name="oil_outlet", table = "bearing_housing")
    private String oilOutlet;

    @Column(name="oil", table = "bearing_housing")
    private String oil;

    @Column(name="outlet_flange_holes", table = "bearing_housing")
    private String outletFlangeHoles;

    @Column(name="water_ports", table = "bearing_housing")
    private String waterPorts;

    @Column(name="design_features", table = "bearing_housing")
    private String designFeatures;

    @Column(name="bearing_type", table = "bearing_housing")
    private String bearingType;

    @Override
    public JSOG toJsog() {
        JSOG partObject = super.toJsog();
        
        if (coolType != null) {
            partObject.put("cool_type_name", coolType.getName());
        }
        partObject.put("oil_inlet", oilInlet);
        partObject.put("oil_outlet", oilOutlet);
        partObject.put("oil", oil);
        partObject.put("outlet_flange_holes", outletFlangeHoles);
        partObject.put("water_ports", waterPorts);
        partObject.put("design_features", designFeatures);
        partObject.put("bearing_type", bearingType);
        
        return partObject;
    }
}
