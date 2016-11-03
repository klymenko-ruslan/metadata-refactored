package com.turbointernational.metadata.dao;

import com.turbointernational.metadata.entity.part.types.kit.KitType;
import org.springframework.stereotype.Repository;

/**
 * Created by dmytro.trunykov@zorallabs.com on 2016-08-20.
 */
@Repository
public class KitTypeDao extends AbstractDao<KitType> {

    public KitTypeDao() {
        super(KitType.class);
    }

}
