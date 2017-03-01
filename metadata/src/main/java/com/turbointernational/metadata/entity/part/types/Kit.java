package com.turbointernational.metadata.entity.part.types;

import static javax.persistence.FetchType.LAZY;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.entity.part.Part;
import com.turbointernational.metadata.entity.part.types.kit.KitType;
import com.turbointernational.metadata.util.View;

/**
 * Created by dmytro.trunykov@zorallabs.com on 2016-08-01 17:06:08.763014.
 */
@Entity
@Table(name = "kit")
@DiscriminatorValue("3")
@PrimaryKeyJoinColumn(name = "part_id")
public class Kit extends Part {

    private static final long serialVersionUID = 1072037002984372782L;

    @JsonView(View.Detail.class)
    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "kit_type_id")
    private KitType kitType;

    // <editor-fold defaultstate="collapsed" desc="Properties: critical
    // dimensions">

    // </editor-fold>

    public KitType getKitType() {
        return kitType;
    }

    public void setKitType(KitType kitType) {
        this.kitType = kitType;
    }

    // <editor-fold defaultstate="collapsed" desc="Getters and setters: critical
    // dimensions">

    // </editor-fold>

}
