package com.turbointernational.metadata.util;

import com.turbointernational.metadata.Application;
import com.turbointernational.metadata.domain.AbstractDao;
import com.turbointernational.metadata.domain.car.CarModel;
import com.turbointernational.metadata.domain.car.CarModelDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Created by trunikov on 12/9/15.
 */
@Service
public class CarModelElasticSearch extends AbstractElasticSearch {

    @Autowired
    private CarModelDao carModelDao;

    @Value("${elasticsearch.type.carmodel}")
    String elasticSearchType = "carmodel";

    public static CarModelElasticSearch instance() {
        return Application.getContext().getBean(CarModelElasticSearch.class);
    }

    @Override
    protected String getElasticSearchType() {
        return elasticSearchType;
    }

    @Override
    protected AbstractDao<?> getDao() {
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
