package com.turbointernational.metadata.domain.type;

import com.turbointernational.metadata.domain.AbstractDao;
import org.springframework.stereotype.Repository;

/**
 *
 * @author jrodriguez
 */
@Repository
public class GasketTypeDao extends AbstractDao<GasketType> {
    public GasketTypeDao() {
        super(GasketType.class);
    }
}
