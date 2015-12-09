package com.turbointernational.metadata.util;

import com.turbointernational.metadata.domain.GenericDao;
import com.turbointernational.metadata.domain.car.CarEngine;
import com.turbointernational.metadata.domain.car.CarEngineDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

/**
 * Created by trunikov on 12/9/15.
 */
public class CarEngineElasticSearch extends AbstractElasticSearch {

    @Autowired
    private CarEngineDao carEngineDao;

    @Value("${elasticsearch.type.carengine}")
    String elasticSearchType = "carengine";

    @Override
    protected String getElasticSearchType() {
        return elasticSearchType;
    }

    @Override
    protected GenericDao<?> getDao() {
        return carEngineDao;
    }

    @Override
    protected String getSearchId(Object o) {
        CarEngine carEngine = (CarEngine) o;
        return carEngine.getId().toString();
    }

    @Override
    protected String toSearchJson(Object o) {
        CarEngine carEngine = (CarEngine) o;
        return carEngine.toSearchJson();
    }
}
