package com.turbointernational.metadata.dao;

import com.turbointernational.metadata.entity.CoolType;
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
