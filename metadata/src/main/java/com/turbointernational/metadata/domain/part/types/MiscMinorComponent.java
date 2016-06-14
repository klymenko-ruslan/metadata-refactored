package com.turbointernational.metadata.domain.part.types;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.domain.criticaldimension.CriticalDimensionEnumVal;
import com.turbointernational.metadata.domain.part.Part;
import com.turbointernational.metadata.web.View;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;


/**
 * Created by dmytro.trunykov@zorallabs.com on 2016-06-14 17:51:47.772536.
 */
@Entity
@Table(name = "misc_minor_component")
@PrimaryKeyJoinColumn(name = "part_id")
public class MiscMinorComponent extends Part {

    //<editor-fold defaultstate="collapsed" desc="Properties: critical dimensions">

    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Getters and setters: critical dimensions">

    //</editor-fold>

}
