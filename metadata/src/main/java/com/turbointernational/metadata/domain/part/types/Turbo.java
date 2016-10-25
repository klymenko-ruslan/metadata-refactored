package com.turbointernational.metadata.domain.part.types;

import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.domain.car.*;
import com.turbointernational.metadata.domain.criticaldimension.CriticalDimension;
import com.turbointernational.metadata.domain.other.CoolType;
import com.turbointernational.metadata.domain.other.TurboModel;
import com.turbointernational.metadata.domain.part.Part;
import com.turbointernational.metadata.domain.part.bom.BOMItem;
import com.turbointernational.metadata.domain.part.bom.BOMItemDao;
import com.turbointernational.metadata.web.View;
import flexjson.JSONSerializer;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.apache.commons.lang3.StringUtils.isNotBlank;


/**
 * Created by dmytro.trunykov@zorallabs.com on 2016-08-01 17:06:08.756706.
 */
@Entity
@Table(name = "turbo")
@PrimaryKeyJoinColumn(name = "part_id")
public class Turbo extends Part {

    //<editor-fold defaultstate="collapsed" desc="Properties: members">

    @JsonView(View.Detail.class)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="turbo_model_id")
    private TurboModel turboModel;

    @JsonView(View.Detail.class)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="cool_type_id")
    private CoolType coolType;

    @Transient
    @JsonView(View.Summary.class)
    private Set<String> cmeyYear = new HashSet<>();

    @Transient
    @JsonView(View.Summary.class)
    private Set<String> cmeyMake = new HashSet<>();

    @Transient
    @JsonView(View.Summary.class)
    private Set<String> cmeyModel = new HashSet<>();


    @Transient
    @JsonView(View.Summary.class)
    private Set<String> cmeyEngine = new HashSet<>();

    @Transient
    @JsonView(View.Summary.class)
    private Set<String> cmeyFuelType = new HashSet<>();

    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Getters and setters: members">

    public TurboModel getTurboModel() {
        return turboModel;
    }

    public void setTurboModel(TurboModel turboModel) {
        this.turboModel = turboModel;
    }

    public CoolType getCoolType() {
        return coolType;
    }

    public void setCoolType(CoolType coolType) {
        this.coolType = coolType;
    }

    public Set<String> getCmeyYear() {
        return cmeyYear;
    }

    public void setCmeyYear(Set<String> cmeyYear) {
        this.cmeyYear = cmeyYear;
    }

    public Set<String> getCmeyMake() {
        return cmeyMake;
    }

    public void setCmeyMake(Set<String> cmeyMake) {
        this.cmeyMake = cmeyMake;
    }

    public Set<String> getCmeyModel() {
        return cmeyModel;
    }

    public void setCmeyModel(Set<String> cmeyModel) {
        this.cmeyModel = cmeyModel;
    }

    public Set<String> getCmeyEngine() {
        return cmeyEngine;
    }

    public void setCmeyEngine(Set<String> cmeyEngine) {
        this.cmeyEngine = cmeyEngine;
    }

    public Set<String> getCmeyFuelType() {
        return cmeyFuelType;
    }

    public void setCmeyFuelType(Set<String> cmeyFuelType) {
        this.cmeyFuelType = cmeyFuelType;
    }

    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Serialization: members">

    private void addCmeyOfPart(TurboCarModelEngineYearDao tcmeyDao, Long partId) {
        for (TurboCarModelEngineYear tcmey : tcmeyDao.getPartLinkedApplications(partId)) {
            CarModelEngineYear cmey = tcmey.getCarModelEngineYear();
            if (cmey == null) continue;
            CarYear cyear = cmey.getYear();
            if (cyear != null) {
                String yearName = cyear.getName();
                if (isNotBlank(yearName)) {
                    cmeyYear.add(yearName);
                }
            }
            CarModel cmodel = cmey.getModel();
            if (cmodel != null) {
                String modelName = cmodel.getName();
                if (isNotBlank(modelName)) {
                    cmeyModel.add(modelName);
                }
                CarMake cmake = cmodel.getMake();
                if (cmake != null) {
                    String makeName = cmake.getName();
                    if (isNotBlank(makeName)) {
                        cmeyMake.add(makeName);
                    }
                }
            }
            CarEngine cengine = cmey.getEngine();
            if (cengine != null) {
                String engineName = cengine.getEngineSize();
                if (isNotBlank(engineName)) {
                    cmeyEngine.add(engineName);
                }
                CarFuelType cfueltype = cengine.getFuelType();
                if (cfueltype != null) {
                    String fuelTypeName = cfueltype.getName();
                    if (isNotBlank(fuelTypeName)) {
                        cmeyFuelType.add(fuelTypeName);
                    }
                }
            }
        }
    }

    @Override
    protected JSONSerializer buildJsonSerializerSearch(List<CriticalDimension> criticalDimensions,
                                                       TurboCarModelEngineYearDao tcmeyDao, BOMItemDao bomItemDao) {
        JSONSerializer jsonSerializer = super.buildJsonSerializerSearch(criticalDimensions, tcmeyDao, bomItemDao);
            jsonSerializer.include("turboModel.id");
            jsonSerializer.include("turboModel.name");
            jsonSerializer.include("turboModel.turboType.id");
            jsonSerializer.include("turboModel.turboType.name");
        Long partId = getId();
        // Ticket #807.
        addCmeyOfPart(tcmeyDao, partId);
        List<Number> partIds = bomItemDao.bomChildren(partId);
        for(Number childId : partIds) {
            addCmeyOfPart(tcmeyDao, childId.longValue());
        }
        jsonSerializer.include("cmeyYear");
        jsonSerializer.include("cmeyMake");
        jsonSerializer.include("cmeyModel");
        jsonSerializer.include("cmeyEngine");
        jsonSerializer.include("cmeyFuelType");
        return jsonSerializer;
    }

    //</editor-fold>

}
