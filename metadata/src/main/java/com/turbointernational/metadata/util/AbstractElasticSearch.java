package com.turbointernational.metadata.util;

import com.turbointernational.metadata.domain.AbstractDao;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

/**
 * Created by trunikov on 30.11.15.
 */
public abstract class AbstractElasticSearch implements Serializable {

    private final static Logger log = LoggerFactory.getLogger("ElascticSearch");

    @Value("${elasticsearch.index}")
    protected String elasticSearchIndex = "metadata";

    @Value("${elasticsearch.host}")
    private  String elasticSearchHost;

    @Value("${elasticsearch.port}")
    private int elasticSearchPort = 9300;

    @Value("${elasticsearch.timeout}")
    protected int timeout = 10000;

    @Value("${elasticsearch.cluster.name}")
    private String clusterName = "elasticsearch";

    protected abstract String getElasticSearchType();

    protected abstract AbstractDao<?> getDao();

    protected abstract String getSearchId(Object o);

    protected abstract String toSearchJson(Object o);

    public String search(String searchJson) throws Exception {
        Client client = client();
        try {
            String searchType = getElasticSearchType();
            SearchRequest request = new SearchRequest(elasticSearchIndex).types(searchType).source(searchJson);
            return client.search(request).actionGet(timeout).toString();
        } finally {
            client.close();
        }
    }

    @Transactional(readOnly = true)
    public void indexAll() throws Exception {
        int maxPages = Integer.MAX_VALUE;
        int page = 0;
        int pageSize = 250;
        String searchType = getElasticSearchType();
        Client client = client();
        try {
            int result;
            AbstractDao<?> dao = getDao();
            do {
                // Clear Hibernate
                dao.clear();
                BulkRequest bulk = new BulkRequest();
                List<?> applications = dao.findAll(page * pageSize, pageSize);
                for (Object o: applications) {
                    String searchId = getSearchId(o);
                    IndexRequest index = new IndexRequest(elasticSearchIndex, searchType, searchId);
                    String asJson = toSearchJson(o);
                    index.source(asJson);
                    bulk.add(index);
                }
                result = applications.size();
                log.info("Indexed '{}' {}-{}: {}", searchType, page * pageSize, (page * pageSize) + pageSize, result);
                page++;
                client.bulk(bulk).actionGet();
            } while (result >= pageSize && page < maxPages);
        } catch (Exception e) {
            log.error("Reindexing of '" + searchType + "' failed.", e);
            throw e;
        } finally {
            log.info("Indexing of '{}' finished.", searchType);
            client.close();
        }
    }
    protected Client client() {
        Settings settings = ImmutableSettings.settingsBuilder()
            .put("cluster.name", clusterName)
            .build();

        return new TransportClient(settings)
            .addTransportAddress(
                new InetSocketTransportAddress(elasticSearchHost, elasticSearchPort));
    }

}
