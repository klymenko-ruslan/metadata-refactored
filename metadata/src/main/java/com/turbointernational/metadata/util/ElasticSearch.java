package com.turbointernational.metadata.util;

import com.turbointernational.metadata.Application;
import com.turbointernational.metadata.domain.part.Part;
import com.turbointernational.metadata.domain.part.PartDao;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.elasticsearch.ElasticSearchException;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author jrodriguez
 */
@Service
public class ElasticSearch {

    private static final Logger log = Logger.getLogger(ElasticSearch.class.toString());
    
    public static ElasticSearch instance() {
        return Application.getContext().getBean(ElasticSearch.class);
    }
    
    @Autowired
    PartDao partDao;
    
    @Value("${elasticsearch.index}")
    String elasticSearchIndex = "metadata";
    
    @Value("${elasticsearch.type}")
    String elasticSearchType = "part";

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
    
    @Transactional(readOnly = true)
    public void indexPart(Part part) throws Exception {
        String document = part.toSearchJson();
        
        IndexRequest index = new IndexRequest(elasticSearchIndex, elasticSearchType, part.getId().toString());
        index.source(document);
        
        Client client = client();
        try {
            client.index(index).actionGet(timeout);
        } catch (ElasticSearchException e) {
            log.log(Level.SEVERE, "Could not index part " + document, e);
        } finally {
            client.close();
        }
    }

    @Async("asyncExecutor")
    @Transactional(readOnly = true)
    public void indexAllParts() throws Exception {
        int maxPages = Integer.MAX_VALUE;
        int page = 0;
        int pageSize = 250;

        Client client = client();
        try {
            int result;
            do {
                // Clear hibernate
                partDao.clear();

                List<Part> parts = partDao.findAll(page * pageSize, pageSize);
                
                BulkRequest bulk = new BulkRequest();

                for (Part part : parts) {
                    IndexRequest index = new IndexRequest(elasticSearchIndex, elasticSearchType, part.getId().toString());
                    index.source(part.toSearchJson());
                    bulk.add(index);
                }

                result = parts.size();
                log.log(Level.INFO, "Indexed parts {0}-{1}: {2}", new Object[]{page * pageSize, (page * pageSize) + pageSize, result});
                page++;
                
                client.bulk(bulk).actionGet();

            } while (result >= pageSize && page < maxPages);
        } catch (Exception e) {
            log.log(Level.SEVERE, "Reindexing failed.", e);
            throw e;
        } finally {
            log.log(Level.INFO, "Reindexing complete.");
            client.close();
        }
    }

    @Async
    public void deletePart(Part part) throws Exception {
        DeleteRequest delete = new DeleteRequest(elasticSearchIndex, elasticSearchType, part.getId().toString());
        
        Client client = client();
        try {
            client.delete(delete).actionGet(timeout);
        } finally {
            client.close();
        }
    }

}
