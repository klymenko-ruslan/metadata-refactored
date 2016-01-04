package com.turbointernational.metadata.domain.part.types;

import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.domain.part.Part;
import com.turbointernational.metadata.domain.type.CoolType;
import com.turbointernational.metadata.web.View;
import flexjson.JSONSerializer;
import java.util.Map;
import javax.persistence.*;

import org.apache.commons.lang.ObjectUtils;

@Entity
@Table(name="bearing_housing")
@PrimaryKeyJoinColumn(name = "part_id")
public class BearingHousing extends Part {
    
    @JsonView(View.Detail.class)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="cool_type_id")
    private CoolType coolType;

    @JsonView(View.Detail.class)
    @Column(name="oil_inlet")
    private String oilInlet;

    @JsonView(View.Detail.class)
    @Column(name="oil_outlet")
    private String oilOutlet;

    @JsonView(View.Detail.class)
    @Column(name="oil")
    private String oil;

    @JsonView(View.Detail.class)
    @Column(name="outlet_flange_holes")
    private String outletFlangeHoles;

    @JsonView(View.Detail.class)
    @Column(name="water_ports")
    private String waterPorts;

    @JsonView(View.Detail.class)
    @Column(name="design_features")
    private String designFeatures;

    @JsonView(View.Detail.class)
    @Column(name="bearing_type")
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
    protected JSONSerializer buildJSONSerializer() {
        return super.buildJSONSerializer()
            .include("coolType.id")
            .include("coolType.name")
            .include("coolType.version");
    }

    @Override
    protected JSONSerializer getSearchSerializer() {
        return super.getSearchSerializer()
                    .include("coolType.id")
                    .include("coolType.name");
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
