package com.turbointernational.metadata.domain.part.types;

import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.domain.other.CoolType;
import com.turbointernational.metadata.domain.other.TurboModel;
import com.turbointernational.metadata.domain.part.Part;
import com.turbointernational.metadata.web.View;

import javax.persistence.*;


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

    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Properties: critical dimensions">

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

    //</editor-fold>


    //<editor-fold defaultstate="collapsed" desc="Getters and setters: critical dimensions">

    //</editor-fold>

}
