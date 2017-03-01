package com.turbointernational.metadata.entity.part.types;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import com.turbointernational.metadata.entity.part.Part;

/**
 * Created by dmytro.trunykov@zorallabs.com on 2016-08-01 17:06:08.756443.
 */
@Entity
@Table(name = "cartridge")
@DiscriminatorValue("2")
@PrimaryKeyJoinColumn(name = "part_id")
public class Cartridge extends Part {

    private static final long serialVersionUID = -3236163048076914437L;

    // <editor-fold defaultstate="collapsed" desc="Properties: critical
    // dimensions">

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Getters and setters: critical
    // dimensions">

    // </editor-fold>

}
