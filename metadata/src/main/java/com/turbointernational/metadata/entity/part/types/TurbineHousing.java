package com.turbointernational.metadata.entity.part.types;

import com.turbointernational.metadata.entity.part.Part;

import javax.persistence.*;


/**
 * Created by dmytro.trunykov@zorallabs.com on 2016-08-01 17:06:08.756617.
 */
@Entity
@Table(name = "turbine_housing")
@DiscriminatorValue("33")
@PrimaryKeyJoinColumn(name = "part_id")
public class TurbineHousing extends Part {

    //<editor-fold defaultstate="collapsed" desc="Properties: critical dimensions">

    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Getters and setters: critical dimensions">

    //</editor-fold>

}
