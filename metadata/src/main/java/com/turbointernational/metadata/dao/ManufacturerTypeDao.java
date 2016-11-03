package com.turbointernational.metadata.dao;

import com.turbointernational.metadata.entity.ManufacturerType;
import org.springframework.stereotype.Repository;

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
