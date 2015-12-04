package com.turbointernational.metadata.domain.type;

import com.turbointernational.metadata.domain.GenericDao;
import org.springframework.stereotype.Repository;

/**
 *
 * @author jrodriguez
 */
@Repository
public class ManufacturerTypeDao extends GenericDao<ManufacturerType> {
    public ManufacturerTypeDao() {
        super(ManufacturerType.class);
    }
}
