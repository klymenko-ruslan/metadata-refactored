package com.turbointernational.metadata.domain.car;

import com.turbointernational.metadata.domain.AbstractDao;
import org.springframework.stereotype.Repository;

/**
 * Created by trunikov on 12/4/15.
 */
@Repository
public class CarModelEngineYearDao extends AbstractDao<CarModelEngineYear> {
    public CarModelEngineYearDao() {
        super(CarModelEngineYear.class);
    }
}
