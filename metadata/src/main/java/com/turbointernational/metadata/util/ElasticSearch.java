package com.turbointernational.metadata.util;

import com.turbointernational.metadata.domain.part.Part;
import java.util.List;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
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

    public Client client() {
        return new TransportClient().addTransportAddress(new InetSocketTransportAddress(elasticSearchHost, elasticSearchPort));
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

        IndexRequest index = new IndexRequest(elasticSearchIndex, elasticSearchType, part.getId().toString());
        index.source(part.toJson());
        
        Client client = client();
        try {
            client.index(index).actionGet(timeout);
        } finally {
            client.close();
        }
    }

    @Transactional(readOnly = true)
    public int indexParts(int firstResult, int maxResults) throws Exception {
        
        BulkRequest bulk = new BulkRequest();
        
        List<Part> parts = Part.findPartEntries(firstResult, maxResults);
        
        for (Part part : parts) {
            IndexRequest index = new IndexRequest(elasticSearchIndex, elasticSearchType, part.getId().toString());
            index.source(part.toJson());
            bulk.add(index);
        }
        
        Client client = client();
        try {
            client.bulk(bulk).actionGet();
        } finally {
            client.close();
        }
        
        return parts.size();
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
