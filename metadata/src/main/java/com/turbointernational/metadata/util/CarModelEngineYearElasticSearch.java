package com.turbointernational.metadata.util;

import com.turbointernational.metadata.domain.GenericDao;
import com.turbointernational.metadata.domain.car.CarModelEngineYear;
import com.turbointernational.metadata.domain.car.CarModelEngineYearDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Created by trunikov on 30.11.15.
 */
@Service
public class CarModelEngineYearElasticSearch extends AbstractElasticSearch {

    @Autowired
    private CarModelEngineYearDao carModelEngineYearDao;

    @Value("${elasticsearch.type.carmodelengineyear}")
    private String elasticSearchType = "carmodelengineyear";

    @Override
    protected String getElasticSearchType() {
        return elasticSearchType;
    }

    @Override
    protected GenericDao<?> getDao() {
        return carModelEngineYearDao;
    }

    @Override
    protected String getSearchId(Object o) {
        CarModelEngineYear cmey = (CarModelEngineYear) o;
        return cmey.getId().toString();
    }

    @Override
    protected String toSearchJson(Object o) {
        CarModelEngineYear cmey = (CarModelEngineYear) o;
        return cmey.toSearchJson();
    }

}
