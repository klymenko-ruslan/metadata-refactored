package com.turbointernational.metadata.entity.part.types;

import com.turbointernational.metadata.entity.part.Part;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;


/**
 * Created by dmytro.trunykov@zorallabs.com on 2016-08-01 17:06:08.762678.
 */
@Entity
@Table(name = "gasket_kit")
@PrimaryKeyJoinColumn(name = "part_id")
public class GasketKit extends Part {

    //<editor-fold defaultstate="collapsed" desc="Properties: critical dimensions">

    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Getters and setters: critical dimensions">

    //</editor-fold>

}
