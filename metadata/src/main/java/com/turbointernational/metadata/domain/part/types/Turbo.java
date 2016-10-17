package com.turbointernational.metadata.domain.part.types;

import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.domain.car.CarModelEngineYear;
import com.turbointernational.metadata.domain.car.CarYear;
import com.turbointernational.metadata.domain.criticaldimension.CriticalDimension;
import com.turbointernational.metadata.domain.other.CoolType;
import com.turbointernational.metadata.domain.other.TurboModel;
import com.turbointernational.metadata.domain.part.Part;
import com.turbointernational.metadata.web.View;
import flexjson.JSONSerializer;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


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
    private List<String> cmeyYear = new ArrayList<>();

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

    public List<String> getCmeyYear() {
        return cmeyYear;
    }

    public void setCmeyYear(List<String> cmeyYear) {
        this.cmeyYear = cmeyYear;
    }

    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Serialization: members">

    @Override
    protected JSONSerializer buildJsonSerializerSearch(List<CriticalDimension> criticalDimensions,
                                                       TurboCarModelEngineYearDao tcmeyDao) {
        JSONSerializer jsonSerializer = super.buildJsonSerializerSearch(criticalDimensions, tcmeyDao);
            jsonSerializer.include("turboModel.id");
            jsonSerializer.include("turboModel.name");
            jsonSerializer.include("turboModel.turboType.id");
            jsonSerializer.include("turboModel.turboType.name");
        Long partId = getId();
        for (TurboCarModelEngineYear tcmey : tcmeyDao.getPartLinkedApplications(partId)) {
            CarModelEngineYear cmey = tcmey.getCarModelEngineYear();
            if (cmey == null) continue;
            CarYear cyear = cmey.getYear();
            if (cyear != null) {
                String yearName = cyear.getName();
                if (yearName != null) {
                    cmeyYear.add(yearName);
                }
            }
        }
        jsonSerializer.include("cmeyYear");
        return jsonSerializer;
    }

    //</editor-fold>

}
