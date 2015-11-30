package com.turbointernational.metadata.util;

import com.turbointernational.metadata.domain.car.CarModelEngineYear;
import com.turbointernational.metadata.domain.part.types.TurboCarModelEngineYear;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by trunikov on 30.11.15.
 */
@Service
public class ApplicationsElasticSearch extends AbstractElasticSearch {

    private static final Logger log = Logger.getLogger(ApplicationsElasticSearch.class.toString());

    @Value("${elasticsearch.type.application}")
    String elasticSearchType;

    @Transactional(readOnly = true)
    public int indexApplications(int firstResult, int maxResults) throws Exception {

        BulkRequest bulk = new BulkRequest();

        List<CarModelEngineYear> applications = CarModelEngineYear.findApplicationEntries(firstResult, maxResults);

        for (CarModelEngineYear application : applications) {
            IndexRequest index = new IndexRequest(elasticSearchIndex, elasticSearchType,
                    application.getId().toString());
            index.source(application.toSearchJson());
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
}
