package com.turbointernational.metadata.util;

import com.turbointernational.metadata.domain.GenericDao;
import com.turbointernational.metadata.domain.car.CarModel;
import com.turbointernational.metadata.domain.car.CarModelDao;
import com.turbointernational.metadata.domain.car.CarModelEngineYearDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

/**
 * Created by trunikov on 12/9/15.
 */
public class CarModelElasticSearch extends AbstractElasticSearch {

    @Autowired
    private CarModelDao carModelDao;

    @Value("${elasticsearch.type.carmodel}")
    String elasticSearchType = "carmodel";

    @Override
    protected String getElasticSearchType() {
        return elasticSearchType;
    }

    @Override
    protected GenericDao<?> getDao() {
        return carModelDao;
    }

    @Override
    protected String getSearchId(Object o) {
        CarModel carModel = (CarModel) o;
        return carModel.getId().toString();
    }

    @Override
    protected String toSearchJson(Object o) {
        CarModel carModel = (CarModel) o;
        return carModel.toSearchJson();
    }
}
