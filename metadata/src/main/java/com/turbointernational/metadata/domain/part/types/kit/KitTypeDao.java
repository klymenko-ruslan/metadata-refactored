package com.turbointernational.metadata.domain.part.types.kit;

import com.turbointernational.metadata.domain.AbstractDao;
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
