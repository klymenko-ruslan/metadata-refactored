package com.turbointernational.metadata.util;

import com.turbointernational.metadata.domain.car.CarModelEngineYear;
import com.turbointernational.metadata.domain.car.CarModelEngineYearDao;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by trunikov on 30.11.15.
 */
@Service
public class CarModelEngineYearElasticSearch extends AbstractElasticSearch {

    @Autowired
    private CarModelEngineYearDao carModelEngineYearDao;

    @Value("${elasticsearch.type.carmodelengineyear}")
    String elasticSearchType = "carmodelengineyear";

    @Transactional(readOnly = true)
    public int indexApplications(int firstResult, int maxResults) throws Exception {

        BulkRequest bulk = new BulkRequest();

        List<CarModelEngineYear> applications = carModelEngineYearDao.findApplicationEntries(firstResult, maxResults);

        for (CarModelEngineYear application : applications) {
            String searchId = application.getId().toString();
            IndexRequest index = new IndexRequest(elasticSearchIndex, elasticSearchType, searchId);
            String asJson = application.toSearchJson();
            index.source(asJson);
            bulk.add(index);
        }

        Client client = client();
        try {
            client.bulk(bulk).actionGet();
        } finally {
            client.close();
        }

        return applications.size();
    }

    @Override
    protected String getElasticSearchType() {
        return elasticSearchType;
    }
}
