package com.turbointernational.metadata.domain.part.types;

import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.domain.part.Part;
import com.turbointernational.metadata.web.View;
import java.util.Map;
import javax.persistence.*;

import org.apache.commons.lang.ObjectUtils;

@Entity
@Table(name="compressor_wheel")
@PrimaryKeyJoinColumn(name = "part_id")
public class CompressorWheel extends Part {
    
    @JsonView(View.Detail.class)
    @Column(name="inducer_oa")
    private Float inducerOa;

    @JsonView(View.Detail.class)
    @Column(name="tip_height_b")
    private Float tipHeightB;

    @JsonView(View.Detail.class)
    @Column(name="exducer_oc")
    private Float exducerOc;

    @JsonView(View.Detail.class)
    @Column(name="hub_length_d")
    private Float hubLengthD;

    @JsonView(View.Detail.class)
    @Column(name="bore_oe")
    private Float boreOe;

    @JsonView(View.Detail.class)
    @Column(name="trim_no_blades")
    private String numberOfBlades;

    @JsonView(View.Detail.class)
    @Column(name="application")
    private String application;

    public Float getInducerOa() {
        return inducerOa;
    }

    public void setInducerOa(Float inducerOa) {
        this.inducerOa = inducerOa;
    }

    public Float getTipHeightB() {
        return tipHeightB;
    }

    public void setTipHeightB(Float tipHeightB) {
        this.tipHeightB = tipHeightB;
    }

    public Float getExducerOc() {
        return exducerOc;
    }

    public void setExducerOc(Float exducerOc) {
        this.exducerOc = exducerOc;
    }

    public Float getHubLengthD() {
        return hubLengthD;
    }

    public void setHubLengthD(Float hubLengthD) {
        this.hubLengthD = hubLengthD;
    }

    public Float getBoreOe() {
        return boreOe;
    }

    public void setBoreOe(Float boreOe) {
        this.boreOe = boreOe;
    }

    public String getNumberOfBlades() {
        return numberOfBlades;
    }

    public void setNumberOfBlades(String numberOfBlades) {
        this.numberOfBlades = numberOfBlades;
    }

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }
    
    @Override
    public void csvColumns(Map<String, String> columns) {
        super.csvColumns(columns);
        
        columns.put("inducer_oa", ObjectUtils.toString(getInducerOa()));
        columns.put("tip_height_b", ObjectUtils.toString(getTipHeightB()));
        columns.put("exducer_oc", ObjectUtils.toString(getExducerOc()));
        columns.put("hub_length_d", ObjectUtils.toString(getHubLengthD()));
        columns.put("bore_oe", ObjectUtils.toString(getBoreOe()));
        columns.put("number_of_blades", ObjectUtils.toString(getNumberOfBlades()));
        columns.put("application", ObjectUtils.toString(getApplication()));
    }
}
