package com.turbointernational.metadata.entity.part.types;

import com.turbointernational.metadata.entity.part.Part;

import javax.persistence.*;


/**
 * Created by dmytro.trunykov@zorallabs.com on 2016-08-01 17:06:08.756212.
 */
@Entity
@Table(name = "actuator")
@DiscriminatorValue("30")
@PrimaryKeyJoinColumn(name = "part_id")
public class Actuator extends Part {

    //<editor-fold defaultstate="collapsed" desc="Properties: critical dimensions">

    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Getters and setters: critical dimensions">

    //</editor-fold>

}
