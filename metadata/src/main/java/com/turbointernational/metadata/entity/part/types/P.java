package com.turbointernational.metadata.entity.part.types;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import com.turbointernational.metadata.entity.part.Part;

/**
 * Created by dmytro.trunykov@zorallabs.com on 2016-08-01 17:06:08.762847.
 */
@Entity
@Table(name = "p")
@DiscriminatorValue("51")
@PrimaryKeyJoinColumn(name = "part_id")
public class P extends Part {

    private static final long serialVersionUID = -3287278863290284747L;

    // <editor-fold defaultstate="collapsed" desc="Properties: critical
    // dimensions">

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Getters and setters: critical
    // dimensions">

    // </editor-fold>

}
