package com.turbointernational.metadata.dao;

import com.turbointernational.metadata.entity.part.types.GasketKit;
import org.springframework.stereotype.Repository;

/**
 * Created by trunikov on 12/16/16.
 */
@Repository
public class GasketKitDao extends AbstractDao<GasketKit> {

    public GasketKitDao() {
        super(GasketKit.class);
    }

}
