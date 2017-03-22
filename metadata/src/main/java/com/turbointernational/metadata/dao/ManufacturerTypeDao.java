package com.turbointernational.metadata.dao;

import org.springframework.stereotype.Repository;

import com.turbointernational.metadata.entity.ManufacturerType;

/**
 *
 * @author jrodriguez
 */
@Repository
public class ManufacturerTypeDao extends AbstractDao<ManufacturerType> {

    public ManufacturerTypeDao() {
        super(ManufacturerType.class);
    }

}
