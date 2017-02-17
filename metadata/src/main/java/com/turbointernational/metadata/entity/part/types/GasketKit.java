package com.turbointernational.metadata.entity.part.types;

import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.entity.part.Part;
import com.turbointernational.metadata.util.View;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.REFRESH;
import static javax.persistence.FetchType.LAZY;


/**
 * Created by dmytro.trunykov@zorallabs.com on 2016-08-01 17:06:08.762678.
 */
@Entity
@Table(name = "gasket_kit")
@DiscriminatorValue("49")
@PrimaryKeyJoinColumn(name = "part_id")
public class GasketKit extends Part {

    //<editor-fold defaultstate="collapsed" desc="Properties: members">

    @OneToMany(cascade = REFRESH, mappedBy = "gasketKit", targetEntity = Turbo.class, fetch = LAZY)
    @JsonView({View.SummaryWithTurbos.class})
    private List<Turbo> turbos = new ArrayList<>();

    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Properties: critical dimensions">

    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Getters and setters: members">

    public List<Turbo> getTurbos() {
        return turbos;
    }

    public void setTurbos(List<Turbo> turbos) {
        this.turbos = turbos;
    }

    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Getters and setters: critical dimensions">

    //</editor-fold>

}
