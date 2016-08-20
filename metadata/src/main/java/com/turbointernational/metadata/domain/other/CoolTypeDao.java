package com.turbointernational.metadata.domain.other;

import com.turbointernational.metadata.domain.AbstractDao;
import org.springframework.stereotype.Repository;

/**
 * Created by dmytro.trunykov@zorallabs.com 2016-08-20.
 */
@Repository
public class CoolTypeDao extends AbstractDao<CoolType> {

    public CoolTypeDao() {
        super(CoolType.class);
    }

}
