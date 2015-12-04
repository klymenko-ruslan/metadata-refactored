package com.turbointernational.metadata.domain.type;

import com.turbointernational.metadata.domain.GenericDao;
import org.springframework.stereotype.Repository;

/**
 *
 * @author jrodriguez
 */
@Repository
public class GasketTypeDao extends GenericDao<GasketType> {
    public GasketTypeDao() {
        super(GasketType.class);
    }
}
