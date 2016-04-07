package com.turbointernational.metadata.domain.part.types;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.domain.part.Part;
import com.turbointernational.metadata.domain.type.CoolType;
import com.turbointernational.metadata.web.View;
import flexjson.JSONSerializer;

import java.util.Map;
import javax.persistence.*;

import org.apache.commons.lang.ObjectUtils;

@Entity
@Table(name = "bearing_housing")
@PrimaryKeyJoinColumn(name = "part_id")
public class BearingHousing extends Part {

    public enum WaterCooledEnum {OIL, WATER}

    @JsonView(View.Detail.class)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cool_type_id")
    private CoolType coolType;

    @JsonView(View.Detail.class)
    @Column(name = "oil_inlet")
    private String oilInlet;

    @JsonView(View.Detail.class)
    @Column(name = "oil_outlet")
    private String oilOutlet;

    @JsonView(View.Detail.class)
    @Column(name = "oil")
    private String oil;

    @JsonView(View.Detail.class)
    @Column(name = "outlet_flange_holes")
    private String outletFlangeHoles;

    @JsonView(View.Detail.class)
    @Column(name = "water_ports")
    private String waterPorts;

    @JsonView(View.Detail.class)
    @Column(name = "design_features")
    private String designFeatures;

    @JsonView(View.Detail.class)
    @Column(name = "bearing_type")
    private String bearingType;



    @JsonView(View.Detail.class)
    @JsonProperty("waterCooled")
    @Column(name = "water_cooled")
    @Enumerated(EnumType.STRING)
    private WaterCooledEnum waterCooled;

    @JsonView(View.Detail.class)
    @JsonProperty("ceDiaA")
    @Column(name = "ce_dia_a")
    private Double ceDiaA;

    @JsonView(View.Detail.class)
    @JsonProperty("ceDiaATol")
    @Column(name = "ce_dia_a_tol")
    private Double ceDiaATol;

    @JsonView(View.Detail.class)
    @JsonProperty("ceDiaB")
    @Column(name = "ce_dia_b")
    private Double ceDiaB;

    @JsonView(View.Detail.class)
    @JsonProperty("ceDiaBTol")
    @Column(name = "ce_dia_b_tol")
    private Double ceDiaBTol;

    @JsonView(View.Detail.class)
    @JsonProperty("ceDiaC")
    @Column(name = "ce_dia_c")
    private Double ceDiaC;

    @JsonView(View.Detail.class)
    @JsonProperty("ceDiaCTol")
    @Column(name = "ce_dia_c_tol")
    private Double ceDiaCTol;

    @JsonView(View.Detail.class)
    @JsonProperty("cwcDia")
    @Column(name = "cwc_dia")
    private Double cwcDia;

    @JsonView(View.Detail.class)
    @JsonProperty("cwcDiaTol")
    @Column(name = "cwc_dia_tol")
    private Double cwcDiaTol;

    @JsonView(View.Detail.class)
    @JsonProperty("boreDiaMax")
    @Column(name = "bore_dia_max")
    private Double boreDiaMax;

    @JsonView(View.Detail.class)
    @JsonProperty("prBoreDia")
    @Column(name = "pr_bore_dia")
    private Double prBoreDia;

    @JsonView(View.Detail.class)
    @JsonProperty("prBoreDiaTol")
    @Column(name = "pr_bore_dia_tol")
    private Double prBoreDiaTol;

    @JsonView(View.Detail.class)
    @JsonProperty("spinningBearing")
    @Column(name = "spinning_bearing")
    private Boolean spinningBearing;

    @JsonView(View.Detail.class)
    @JsonProperty("teDiaD")
    @Column(name = "te_dia_d")
    private Double teDiaD;

    @JsonView(View.Detail.class)
    @JsonProperty("teDiaDTol")
    @Column(name = "te_dia_d_tol")
    private Double teDiaDTol;

    @JsonView(View.Detail.class)
    @JsonProperty("teDiaE")
    @Column(name = "te_dia_e")
    private Double teDiaE;

    @JsonView(View.Detail.class)
    @JsonProperty("teDiaETol")
    @Column(name = "te_dia_e_tol")
    private Double teDiaETol;

    @JsonView(View.Detail.class)
    @JsonProperty("teDiaF")
    @Column(name = "te_dia_f")
    private Double teDiaF;

    @JsonView(View.Detail.class)
    @JsonProperty("teDiaFTol")
    @Column(name = "te_dia_f_tol")
    private Double teDiaFTol;

    @JsonView(View.Detail.class)
    @JsonProperty("armAngle")
    @Column(name = "arm_angle")
    private Double armAngle;

    @JsonView(View.Detail.class)
    @JsonProperty("oal")
    @Column(name = "oal")
    private Double oal;

    @JsonView(View.Detail.class)
    @JsonProperty("oalTol")
    @Column(name = "oal_tol")
    private Double oalTol;

    @JsonView(View.Detail.class)
    @JsonProperty("weight")
    @Column(name = "weight")
    private Double weight;

    @JsonView(View.Detail.class)
    @JsonProperty("diagramNum")
    @Column(name = "diagram_num")
    private Integer diagramNum;

    @JsonView(View.Detail.class)
    @JsonProperty("ledInChmfrAngle")
    @Column(name = "led_in_chmfr_angle")
    private Double ledInChmfrAngle;

    @JsonView(View.Detail.class)
    @JsonProperty("ledInChmfrLen")
    @Column(name = "led_in_chmfr_len")
    private Double ledInChmfrLen;

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


    public WaterCooledEnum getWaterCooled() {
        return waterCooled;
    }

    public void setWaterCooled(WaterCooledEnum waterCooled) {
        this.waterCooled = waterCooled;
    }

    public Double getCeDiaA() {
        return ceDiaA;
    }

    public void setCeDiaA(Double ceDiaA) {
        this.ceDiaA = ceDiaA;
    }

    public Double getCeDiaATol() {
        return ceDiaATol;
    }

    public void setCeDiaATol(Double ceDiaATol) {
        this.ceDiaATol = ceDiaATol;
    }

    public Double getCeDiaB() {
        return ceDiaB;
    }

    public void setCeDiaB(Double ceDiaB) {
        this.ceDiaB = ceDiaB;
    }

    public Double getCeDiaBTol() {
        return ceDiaBTol;
    }

    public void setCeDiaBTol(Double ceDiaBTol) {
        this.ceDiaBTol = ceDiaBTol;
    }

    public Double getCeDiaC() {
        return ceDiaC;
    }

    public void setCeDiaC(Double ceDiaC) {
        this.ceDiaC = ceDiaC;
    }

    public Double getCeDiaCTol() {
        return ceDiaCTol;
    }

    public void setCeDiaCTol(Double ceDiaCTol) {
        this.ceDiaCTol = ceDiaCTol;
    }

    public Double getCwcDia() {
        return cwcDia;
    }

    public void setCwcDia(Double cwcDia) {
        this.cwcDia = cwcDia;
    }

    public Double getCwcDiaTol() {
        return cwcDiaTol;
    }

    public void setCwcDiaTol(Double cwcDiaTol) {
        this.cwcDiaTol = cwcDiaTol;
    }

    public Double getBoreDiaMax() {
        return boreDiaMax;
    }

    public void setBoreDiaMax(Double boreDiaMax) {
        this.boreDiaMax = boreDiaMax;
    }

    public Double getPrBoreDia() {
        return prBoreDia;
    }

    public void setPrBoreDia(Double prBoreDia) {
        this.prBoreDia = prBoreDia;
    }

    public Double getPrBoreDiaTol() {
        return prBoreDiaTol;
    }

    public void setPrBoreDiaTol(Double prBoreDiaTol) {
        this.prBoreDiaTol = prBoreDiaTol;
    }

    public Boolean getSpinningBearing() {
        return spinningBearing;
    }

    public void setSpinningBearing(Boolean spinningBearing) {
        this.spinningBearing = spinningBearing;
    }

    public Double getTeDiaD() {
        return teDiaD;
    }

    public void setTeDiaD(Double teDiaD) {
        this.teDiaD = teDiaD;
    }

    public Double getTeDiaDTol() {
        return teDiaDTol;
    }

    public void setTeDiaDTol(Double teDiaDTol) {
        this.teDiaDTol = teDiaDTol;
    }

    public Double getTeDiaE() {
        return teDiaE;
    }

    public void setTeDiaE(Double teDiaE) {
        this.teDiaE = teDiaE;
    }

    public Double getTeDiaETol() {
        return teDiaETol;
    }

    public void setTeDiaETol(Double teDiaETol) {
        this.teDiaETol = teDiaETol;
    }

    public Double getTeDiaF() {
        return teDiaF;
    }

    public void setTeDiaF(Double teDiaF) {
        this.teDiaF = teDiaF;
    }

    public Double getTeDiaFTol() {
        return teDiaFTol;
    }

    public void setTeDiaFTol(Double teDiaFTol) {
        this.teDiaFTol = teDiaFTol;
    }

    public Double getArmAngle() {
        return armAngle;
    }

    public void setArmAngle(Double armAngle) {
        this.armAngle = armAngle;
    }

    public Double getOal() {
        return oal;
    }

    public void setOal(Double oal) {
        this.oal = oal;
    }

    public Double getOalTol() {
        return oalTol;
    }

    public void setOalTol(Double oalTol) {
        this.oalTol = oalTol;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Integer getDiagramNum() {
        return diagramNum;
    }

    public void setDiagramNum(Integer diagramNum) {
        this.diagramNum = diagramNum;
    }

    public Double getLedInChmfrAngle() {
        return ledInChmfrAngle;
    }

    public void setLedInChmfrAngle(Double ledInChmfrAngle) {
        this.ledInChmfrAngle = ledInChmfrAngle;
    }

    public Double getLedInChmfrLen() {
        return ledInChmfrLen;
    }

    public void setLedInChmfrLen(Double ledInChmfrLen) {
        this.ledInChmfrLen = ledInChmfrLen;
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
