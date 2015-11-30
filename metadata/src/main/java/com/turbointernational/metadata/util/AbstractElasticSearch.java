package com.turbointernational.metadata.util;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.springframework.beans.factory.annotation.Value;

import java.io.Serializable;

/**
 * Created by trunikov on 30.11.15.
 */
public abstract class AbstractElasticSearch implements Serializable {

    @Value("${elasticsearch.index}")
    String elasticSearchIndex = "metadata";

    //@Value("${elasticsearch.type}")
    String elasticSearchType = null; // Must be initialized in descendants.

    @Value("${elasticsearch.host}")
    String elasticSearchHost;

    @Value("${elasticsearch.port}")
    int elasticSearchPort = 9300;

    @Value("${elasticsearch.timeout}")
    int timeout = 10000;

    @Value("${elasticsearch.cluster.name}")
    String clusterName = "elasticsearch";

    public Client client() {
        Settings settings = ImmutableSettings.settingsBuilder()
            .put("cluster.name", clusterName)
            .build();

        return new TransportClient(settings)
            .addTransportAddress(
                new InetSocketTransportAddress(elasticSearchHost, elasticSearchPort));
    }

    public String search(String searchJson) throws Exception {
        Client client = client();
        try {
            SearchRequest request = new SearchRequest(elasticSearchIndex).source(searchJson);
            return client.search(request).actionGet(timeout).toString();
        } finally {
            client.close();
        }
    }

}
