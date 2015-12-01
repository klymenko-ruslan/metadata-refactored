package com.turbointernational.metadata.util;

import com.turbointernational.metadata.domain.part.Part;
import org.elasticsearch.ElasticSearchException;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.Client;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jrodriguez
 */
@Service
public class PartElasticSearch extends AbstractElasticSearch {

    private static final Logger log = Logger.getLogger(PartElasticSearch.class.toString());

    @Value("${elasticsearch.type}")
    String elasticSearchType;

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

    @Transactional(readOnly = true)
    public int indexParts(int firstResult, int maxResults) throws Exception {
        
        BulkRequest bulk = new BulkRequest();
        
        List<Part> parts = Part.findPartEntries(firstResult, maxResults);
        
        for (Part part : parts) {
            String searchId = part.getId().toString();
            IndexRequest index = new IndexRequest(elasticSearchIndex, elasticSearchType, searchId);
            String asJson =  part.toSearchJson();
            index.source(asJson);
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

    @Override
    protected String getElasticSearchType() {
        return elasticSearchType;
    }

}
