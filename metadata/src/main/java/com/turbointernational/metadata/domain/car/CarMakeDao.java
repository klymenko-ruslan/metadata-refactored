package com.turbointernational.metadata.domain.car;

import com.turbointernational.metadata.domain.AbstractDao;
import org.springframework.stereotype.Repository;

/**
 * Created by trunikov on 12/9/15.
 */
@Repository
public class CarMakeDao extends AbstractDao<CarMake> {
    public CarMakeDao() {
        super(CarMake.class);
    }
}
