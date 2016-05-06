package com.turbointernational.metadata.domain.part.types;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.domain.criticaldimension.CriticalDimensionEnumVal;
import com.turbointernational.metadata.domain.part.Part;
import com.turbointernational.metadata.domain.type.SealType;
import com.turbointernational.metadata.web.View;
import flexjson.JSONSerializer;
import java.util.Map;
import javax.persistence.*;

import org.apache.commons.lang.ObjectUtils;

import static javax.persistence.FetchType.EAGER;

@Entity
@Table(name="backplate")
@PrimaryKeyJoinColumn(name = "part_id")
public class Backplate extends Part {

    //<editor-fold defaultstate="collapsed" desc="Properties">
    @JsonView(View.Detail.class)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="seal_type_id")
    private SealType sealType;

    @JsonView(View.Detail.class)
    @Column(name="style_compressor_wheel")
    private String styleCompressorWheel;

    @JsonView(View.Detail.class)
    // ???: How is this different from the seal type's name?
    @Column(name="seal_type")
    private String sealTypeString;

    @JsonView(View.Detail.class)
    @Column(name="overall_diameter")
    private Float overallDiameter;

    @JsonView(View.Detail.class)
    @Column(name="compressor_wheel_diameter")
    private Float compressorWheelDiameter;

    @JsonView(View.Detail.class)
    @Column(name="piston_ring_diameter")
    private Float pistonRingDiameter;

    @JsonView(View.Detail.class)
    @Column(name="compressor_housing_diameter")
    private Float compressorHousingDiameter;

    @JsonView(View.Detail.class)
    @Column(name="notes")
    private String notes;

    @JsonView(View.Detail.class)
    @Column(name="secondary_diameter")
    private Float secondaryDiameter;

    @JsonView(View.Detail.class)
    @Column(name="overall_height")
    private Float overallHeight;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Critical dimensions">
    @JsonView(View.Summary.class)
    @JsonProperty("dynCs")
    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "dyn_cs")
    private CriticalDimensionEnumVal dynCs;

    @JsonView(View.Summary.class)
    @JsonProperty("superbackFlatback")
    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "superback_flatback")
    private CriticalDimensionEnumVal superbackFlatback;

    @JsonView(View.Summary.class)
    @Column(name = "num_mounting_holes")
    private Integer numMountingHoles;

    @JsonView(View.Summary.class)
    @JsonProperty("mountingHoleThreadCallout")
    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "mounting_hole_thread_callout")
    private CriticalDimensionEnumVal mountingHoleThreadCallout;

    @JsonView(View.Summary.class)
    @Column(name = "dia_a")
    private Double diaA;

    @JsonView(View.Summary.class)
    @Column(name = "dia_a_tol")
    private Double diaATol;

    @JsonView(View.Summary.class)
    @Column(name = "dia_b")
    private Double diaB;

    @JsonView(View.Summary.class)
    @Column(name = "dia_b_tol")
    private Double diaBTol;

    @JsonView(View.Summary.class)
    @Column(name = "dia_c")
    private Double diaC;

    @JsonView(View.Summary.class)
    @Column(name = "dia_c_tol")
    private Double diaCTol;

    @JsonView(View.Summary.class)
    @Column(name = "dia_d")
    private Double diaD;

    @JsonView(View.Summary.class)
    @Column(name = "dia_d_tol")
    private Double diaDTol;

    @JsonView(View.Summary.class)
    @Column(name = "dia_e")
    private Double diaE;

    @JsonView(View.Summary.class)
    @Column(name = "dia_e_tol")
    private Double diaETol;

    @JsonView(View.Summary.class)
    @Column(name = "bore_dia")
    private Double boreDia;

    @JsonView(View.Summary.class)
    @Column(name = "bore_dia_tol")
    private Double boreDiaTol;

    @JsonView(View.Summary.class)
    @Column(name = "mounting_hole_dia")
    private Double mountingHoleDia;

    @JsonView(View.Summary.class)
    @Column(name = "oal")
    private Double oal;

    @JsonView(View.Summary.class)
    @Column(name = "oalTol")
    private Double oalTol;

    @JsonView(View.Summary.class)
    @Column(name = "hub_pos_f")
    private Double hubPosF;

    @JsonView(View.Summary.class)
    @Column(name = "hub_pos_f_tol")
    private Double hubPosFTol;

    @JsonView(View.Summary.class)
    @Column(name = "cc_loc_pos_g")
    private Double ccLocPosG;

    @JsonView(View.Summary.class)
    @Column(name = "cc_loc_pos_g_tol")
    private Double ccLocPosGTol;

    @JsonView(View.Summary.class)
    @Column(name = "lead_in_chmfr_angle")
    private Double leadInChmfrAngle;

    @JsonView(View.Summary.class)
    @Column(name = "lead_in_chmfr_len")
    private Double leadInChmfrLen;

    @JsonView(View.Summary.class)
    @JsonProperty("matl")
    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "matl")
    private CriticalDimensionEnumVal matl;

    @JsonView(View.Summary.class)
    @Column(name = "weight")
    private Double weight;

    @JsonView(View.Summary.class)
    @Column(name = "diagram_num")
    private Integer diagramNum;
    //</editor-fold>

    public SealType getSealType() {
        return sealType;
    }

    public void setSealType(SealType sealType) {
        this.sealType = sealType;
    }

    public String getStyleCompressorWheel() {
        return styleCompressorWheel;
    }

    public void setStyleCompressorWheel(String styleCompressorWheel) {
        this.styleCompressorWheel = styleCompressorWheel;
    }

    public String getSealTypeString() {
        return sealTypeString;
    }

    public void setSealTypeString(String sealTypeString) {
        this.sealTypeString = sealTypeString;
    }

    public Float getOverallDiameter() {
        return overallDiameter;
    }

    public void setOverallDiameter(Float overallDiameter) {
        this.overallDiameter = overallDiameter;
    }

    public Float getCompressorWheelDiameter() {
        return compressorWheelDiameter;
    }

    public void setCompressorWheelDiameter(Float compressorWheelDiameter) {
        this.compressorWheelDiameter = compressorWheelDiameter;
    }

    public Float getPistonRingDiameter() {
        return pistonRingDiameter;
    }

    public void setPistonRingDiameter(Float pistonRingDiameter) {
        this.pistonRingDiameter = pistonRingDiameter;
    }

    public Float getCompressorHousingDiameter() {
        return compressorHousingDiameter;
    }

    public void setCompressorHousingDiameter(Float compressorHousingDiameter) {
        this.compressorHousingDiameter = compressorHousingDiameter;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Float getSecondaryDiameter() {
        return secondaryDiameter;
    }

    public void setSecondaryDiameter(Float secondaryDiameter) {
        this.secondaryDiameter = secondaryDiameter;
    }

    public Float getOverallHeight() {
        return overallHeight;
    }

    public void setOverallHeight(Float overallHeight) {
        this.overallHeight = overallHeight;
    }

    public CriticalDimensionEnumVal getDynCs() {
        return dynCs;
    }

    public void setDynCs(CriticalDimensionEnumVal dynCs) {
        this.dynCs = dynCs;
    }

    public CriticalDimensionEnumVal getSuperbackFlatback() {
        return superbackFlatback;
    }

    public void setSuperbackFlatback(CriticalDimensionEnumVal superbackFlatback) {
        this.superbackFlatback = superbackFlatback;
    }

    public Integer getNumMountingHoles() {
        return numMountingHoles;
    }

    public void setNumMountingHoles(Integer numMountingHoles) {
        this.numMountingHoles = numMountingHoles;
    }

    public CriticalDimensionEnumVal getMountingHoleThreadCallout() {
        return mountingHoleThreadCallout;
    }

    public void setMountingHoleThreadCallout(CriticalDimensionEnumVal mountingHoleThreadCallout) {
        this.mountingHoleThreadCallout = mountingHoleThreadCallout;
    }

    public Double getDiaA() {
        return diaA;
    }

    public void setDiaA(Double diaA) {
        this.diaA = diaA;
    }

    public Double getDiaATol() {
        return diaATol;
    }

    public void setDiaATol(Double diaATol) {
        this.diaATol = diaATol;
    }

    public Double getDiaB() {
        return diaB;
    }

    public void setDiaB(Double diaB) {
        this.diaB = diaB;
    }

    public Double getDiaBTol() {
        return diaBTol;
    }

    public void setDiaBTol(Double diaBTol) {
        this.diaBTol = diaBTol;
    }

    public Double getDiaC() {
        return diaC;
    }

    public void setDiaC(Double diaC) {
        this.diaC = diaC;
    }

    public Double getDiaCTol() {
        return diaCTol;
    }

    public void setDiaCTol(Double diaCTol) {
        this.diaCTol = diaCTol;
    }

    public Double getDiaD() {
        return diaD;
    }

    public void setDiaD(Double diaD) {
        this.diaD = diaD;
    }

    public Double getDiaDTol() {
        return diaDTol;
    }

    public void setDiaDTol(Double diaDTol) {
        this.diaDTol = diaDTol;
    }

    public Double getDiaE() {
        return diaE;
    }

    public void setDiaE(Double diaE) {
        this.diaE = diaE;
    }

    public Double getDiaETol() {
        return diaETol;
    }

    public void setDiaETol(Double diaETol) {
        this.diaETol = diaETol;
    }

    public Double getBoreDia() {
        return boreDia;
    }

    public void setBoreDia(Double boreDia) {
        this.boreDia = boreDia;
    }

    public Double getBoreDiaTol() {
        return boreDiaTol;
    }

    public void setBoreDiaTol(Double boreDiaTol) {
        this.boreDiaTol = boreDiaTol;
    }

    public Double getMountingHoleDia() {
        return mountingHoleDia;
    }

    public void setMountingHoleDia(Double mountingHoleDia) {
        this.mountingHoleDia = mountingHoleDia;
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

    public Double getHubPosF() {
        return hubPosF;
    }

    public void setHubPosF(Double hubPosF) {
        this.hubPosF = hubPosF;
    }

    public Double getHubPosFTol() {
        return hubPosFTol;
    }

    public void setHubPosFTol(Double hubPosFTol) {
        this.hubPosFTol = hubPosFTol;
    }

    public Double getCcLocPosG() {
        return ccLocPosG;
    }

    public void setCcLocPosG(Double ccLocPosG) {
        this.ccLocPosG = ccLocPosG;
    }

    public Double getCcLocPosGTol() {
        return ccLocPosGTol;
    }

    public void setCcLocPosGTol(Double ccLocPosGTol) {
        this.ccLocPosGTol = ccLocPosGTol;
    }

    public Double getLeadInChmfrAngle() {
        return leadInChmfrAngle;
    }

    public void setLeadInChmfrAngle(Double leadInChmfrAngle) {
        this.leadInChmfrAngle = leadInChmfrAngle;
    }

    public Double getLeadInChmfrLen() {
        return leadInChmfrLen;
    }

    public void setLeadInChmfrLen(Double leadInChmfrLen) {
        this.leadInChmfrLen = leadInChmfrLen;
    }

    public CriticalDimensionEnumVal getMatl() {
        return matl;
    }

    public void setMatl(CriticalDimensionEnumVal matl) {
        this.matl = matl;
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

    /*
    @Override
    protected JSONSerializer getSearchSerializer() {
        return super.getSearchSerializer()
                    .include("sealType.id")
                    .include("sealType.name");
    }
    */
    
    @Override
    public void csvColumns(Map<String, String> columns) {
        super.csvColumns(columns);
        
        if (getSealType() != null) {
            columns.put("seal_type", ObjectUtils.toString(getSealType().getName()));
        }

        columns.put("overall_diameter", ObjectUtils.toString(getOverallDiameter()));
        columns.put("compressor_wheel_diameter", ObjectUtils.toString(getCompressorWheelDiameter()));
        columns.put("piston_ring_diameter", ObjectUtils.toString(getPistonRingDiameter()));
        columns.put("compressor_housing_diameter", ObjectUtils.toString(getCompressorHousingDiameter()));
        columns.put("secondary_diameter", ObjectUtils.toString(getSecondaryDiameter()));
        columns.put("overall_height", ObjectUtils.toString(getOverallHeight()));
        columns.put("style_compressor_wheel", ObjectUtils.toString(getStyleCompressorWheel()));
    }

}
