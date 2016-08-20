package com.turbointernational.metadata.domain.part.types;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.domain.criticaldimension.CriticalDimensionEnumVal;
import com.turbointernational.metadata.domain.part.Part;
import com.turbointernational.metadata.domain.part.types.kit.KitType;
import com.turbointernational.metadata.web.View;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;


/**
 * Created by dmytro.trunykov@zorallabs.com on 2016-08-01 17:06:08.763014.
 */
@Entity
@Table(name = "kit")
@PrimaryKeyJoinColumn(name = "part_id")
public class Kit extends Part {

    @JsonView(View.Detail.class)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="kit_type_id")
    private KitType kitType;

    //<editor-fold defaultstate="collapsed" desc="Properties: critical dimensions">

    //</editor-fold>

    public KitType getKitType() {
        return kitType;
    }

    public void setKitType(KitType kitType) {
        this.kitType = kitType;
    }

    //<editor-fold defaultstate="collapsed" desc="Getters and setters: critical dimensions">

    //</editor-fold>

}
