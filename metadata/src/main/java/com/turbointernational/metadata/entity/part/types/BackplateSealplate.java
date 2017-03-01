package com.turbointernational.metadata.entity.part.types;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import com.turbointernational.metadata.entity.part.Part;

/**
 * Created by dmytro.trunykov@zorallabs.com on 2016-08-01 17:06:08.763437.
 */
@Entity
@Table(name = "backplate_sealplate")
@DiscriminatorValue("14")
@PrimaryKeyJoinColumn(name = "part_id")
public class BackplateSealplate extends Part {

    private static final long serialVersionUID = -6671284283733775203L;

    // <editor-fold defaultstate="collapsed" desc="Properties: critical
    // dimensions">

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Getters and setters: critical
    // dimensions">

    // </editor-fold>

}
