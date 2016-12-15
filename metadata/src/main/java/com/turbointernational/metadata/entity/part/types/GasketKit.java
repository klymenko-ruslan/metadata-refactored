package com.turbointernational.metadata.entity.part.types;

import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.entity.part.Part;
import com.turbointernational.metadata.util.View;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.REFRESH;
import static javax.persistence.FetchType.LAZY;


/**
 * Created by dmytro.trunykov@zorallabs.com on 2016-08-01 17:06:08.762678.
 */
@Entity
@Table(name = "gasket_kit")
@PrimaryKeyJoinColumn(name = "part_id")
public class GasketKit extends Part {

    //<editor-fold defaultstate="collapsed" desc="Properties: members">

    /**
     * A list of turbos linked to this Gasket Kit.
     *
     * This field is declared as type of List<Part> (not List<GasketKit>) for workaround of an issue
     * described in the ticket #878.
     */
    @OneToMany(cascade = REFRESH, mappedBy = "gasketKit", targetEntity = Turbo.class, fetch = LAZY)
    @JsonView({View.SummaryWithTurbos.class})
    private List<Part> turbos = new ArrayList<>();

    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Properties: critical dimensions">

    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Getters and setters: members">

    public List<Part> getTurbos() {
        return turbos;
    }

    public void setTurbos(List<Part> turbos) {
        this.turbos = turbos;
    }

    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Getters and setters: critical dimensions">

    //</editor-fold>

}
