package com.turbointernational.metadata.util;

import com.turbointernational.metadata.domain.car.CarModelEngineYear;
import com.turbointernational.metadata.domain.car.CarModelEngineYearDao;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger log = LoggerFactory.getLogger(CarModelEngineYearElasticSearch.class);

    @Autowired
    private CarModelEngineYearDao carModelEngineYearDao;

    @Value("${elasticsearch.type.carmodelengineyear}")
    String elasticSearchType = "carmodelengineyear";

    @Transactional(readOnly = true)
    public void indexAll() throws Exception {
        int maxPages = Integer.MAX_VALUE;
        int page = 0;
        int pageSize = 250;
        Client client = client();
        try {
            int result;
            do {
                // Clear Hibernate
                carModelEngineYearDao.clear();
                BulkRequest bulk = new BulkRequest();
                List<CarModelEngineYear> applications = carModelEngineYearDao.findApplicationEntries(page * pageSize,
                        pageSize);
                for (CarModelEngineYear application : applications) {
                    String searchId = application.getId().toString();
                    IndexRequest index = new IndexRequest(elasticSearchIndex, elasticSearchType, searchId);
                    String asJson = application.toSearchJson();
                    index.source(asJson);
                    bulk.add(index);
                }
                result = applications.size();
                log.info("Indexed carmodelengineyear {}-{}: {}", page * pageSize, (page * pageSize) + pageSize, result);
                page++;
                client.bulk(bulk).actionGet();
            } while (result >= pageSize && page < maxPages);
        } catch (Exception e) {
            log.error("Reindexing of application failed.", e);
            throw e;
        } finally {
            log.info("Indexing of application finished.");
            client.close();
        }
    }

    @Override
    protected String getElasticSearchType() {
        return elasticSearchType;
    }
}
