package com.turbointernational.metadata.util;

import com.turbointernational.metadata.Application;
import com.turbointernational.metadata.domain.AbstractDao;
import com.turbointernational.metadata.domain.car.CarFuelType;
import com.turbointernational.metadata.domain.car.CarFuelTypeDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Created by trunikov on 12/9/15.
 */
@Service
public class CarFuelTypeElasticSearch extends AbstractElasticSearch {

    @Autowired
    private CarFuelTypeDao carFuelTypeDao;

    @Value("${elasticsearch.type.carfueltype}")
    String elasticSearchType = "carfueltype";

    public static CarFuelTypeElasticSearch instance() {
        return Application.getContext().getBean(CarFuelTypeElasticSearch.class);
    }

    @Override
    protected String getElasticSearchType() {
        return elasticSearchType;
    }

    @Override
    protected AbstractDao<?> getDao() {
        return carFuelTypeDao;
    }

    @Override
    protected String getSearchId(Object o) {
        CarFuelType carFuelType = (CarFuelType) o;
        return carFuelType.getId().toString();
    }

    @Override
    protected String toSearchJson(Object o) {
        CarFuelType carFuelType = (CarFuelType) o;
        return carFuelType.toSearchJson();
    }
}
