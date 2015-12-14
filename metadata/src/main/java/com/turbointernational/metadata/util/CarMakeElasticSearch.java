package com.turbointernational.metadata.util;

import com.turbointernational.metadata.Application;
import com.turbointernational.metadata.domain.AbstractDao;
import com.turbointernational.metadata.domain.car.CarMake;
import com.turbointernational.metadata.domain.car.CarMakeDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Created by trunikov on 12/9/15.
 */
@Service
public class CarMakeElasticSearch  extends AbstractElasticSearch {

    @Autowired
    private CarMakeDao carMakeDao;

    @Value("${elasticsearch.type.carmake}")
    String elasticSearchType = "carmake";

    public static CarMakeElasticSearch instance() {
        return Application.getContext().getBean(CarMakeElasticSearch.class);
    }

    @Override
    protected String getElasticSearchType() {
        return elasticSearchType;
    }

    @Override
    protected AbstractDao<?> getDao() {
        return carMakeDao;
    }

    @Override
    protected String getSearchId(Object o) {
        CarMake carMake = (CarMake) o;
        return carMake.getId().toString();
    }

    @Override
    protected String toSearchJson(Object o) {
        CarMake carMake = (CarMake) o;
        return carMake.toSearchJson();
    }

}
