package com.turbointernational.metadata.domain.part.types;
import com.turbointernational.metadata.domain.part.Part;
import com.turbointernational.metadata.domain.type.CoolType;
import java.util.Map;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.SecondaryTable;
import net.sf.jsog.JSOG;
import org.apache.commons.lang.ObjectUtils;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;

@Configurable
@Entity
@RooJpaActiveRecord
@SecondaryTable(name="bearing_housing", pkJoinColumns=@PrimaryKeyJoinColumn(name = "part_id"))
public class BearingHousing extends Part {
    
    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.REFRESH, CascadeType.MERGE})
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

    public CoolType getCoolType() {
        return coolType;
    }

    public void setCoolType(CoolType coolType) {
        this.coolType = coolType;
    }

    public String getOilInlet() {
        return oilInlet;
    }

    public void setOilInlet(String oilInlet) {
        this.oilInlet = oilInlet;
    }

    public String getOilOutlet() {
        return oilOutlet;
    }

    public void setOilOutlet(String oilOutlet) {
        this.oilOutlet = oilOutlet;
    }

    public String getOil() {
        return oil;
    }

    public void setOil(String oil) {
        this.oil = oil;
    }

    public String getOutletFlangeHoles() {
        return outletFlangeHoles;
    }

    public void setOutletFlangeHoles(String outletFlangeHoles) {
        this.outletFlangeHoles = outletFlangeHoles;
    }

    public String getWaterPorts() {
        return waterPorts;
    }

    public void setWaterPorts(String waterPorts) {
        this.waterPorts = waterPorts;
    }

    public String getDesignFeatures() {
        return designFeatures;
    }

    public void setDesignFeatures(String designFeatures) {
        this.designFeatures = designFeatures;
    }

    public String getBearingType() {
        return bearingType;
    }

    public void setBearingType(String bearingType) {
        this.bearingType = bearingType;
    }

    @Override
    public JSOG toJsog() {
        JSOG partObject = super.toJsog();
        
        partObject.put("oil_inlet", getOilInlet());
        partObject.put("oil_outlet", getOilOutlet());
        partObject.put("oil", getOil());
        partObject.put("outlet_flange_holes", getOutletFlangeHoles());
        partObject.put("water_ports", getWaterPorts());
        partObject.put("design_features", getDesignFeatures());
        partObject.put("bearing_type", getBearingType());
        
        if (getCoolType() != null) {
            partObject.put("cool_type_name", getCoolType().getName());
        }
        
        return partObject;
    }
    
    @Override
    public void csvColumns(Map<String, String> columns) {
        super.csvColumns(columns);
        
        columns.put("oil_inlet", ObjectUtils.toString(getOilInlet()));
        columns.put("oil_outlet", ObjectUtils.toString(getOilOutlet()));
        columns.put("oil", ObjectUtils.toString(getOil()));
        columns.put("outlet_flange_holes", ObjectUtils.toString(getOutletFlangeHoles()));
        columns.put("water_ports", ObjectUtils.toString(getWaterPorts()));
        columns.put("design_features", ObjectUtils.toString(getDesignFeatures()));
        columns.put("bearing_type", ObjectUtils.toString(getBearingType()));

        if (getCoolType() != null) {
            columns.put("cool_type", ObjectUtils.toString(getCoolType().getName()));
        }
    }

}
