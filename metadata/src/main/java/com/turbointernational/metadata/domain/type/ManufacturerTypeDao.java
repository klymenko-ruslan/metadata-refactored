package com.turbointernational.metadata.domain.type;

import com.turbointernational.metadata.domain.AbstractDao;
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
