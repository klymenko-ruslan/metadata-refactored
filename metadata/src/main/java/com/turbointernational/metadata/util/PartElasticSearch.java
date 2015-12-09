package com.turbointernational.metadata.util;

import com.turbointernational.metadata.Application;
import com.turbointernational.metadata.domain.GenericDao;
import com.turbointernational.metadata.domain.part.Part;
import com.turbointernational.metadata.domain.part.PartDao;
import org.elasticsearch.ElasticSearchException;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class PartElasticSearch extends AbstractElasticSearch {

    private static final Logger log = LoggerFactory.getLogger(PartElasticSearch.class);
    
    public static PartElasticSearch instance() {
        return Application.getContext().getBean(PartElasticSearch.class);
    }
    
    @Autowired
    private PartDao partDao;

    @Value("${elasticsearch.type.part}")
    private String elasticSearchType = "part";

    @Transactional(readOnly = true)
    public void index(Part part) throws Exception {
        String document = part.toSearchJson();
        
        IndexRequest index = new IndexRequest(elasticSearchIndex, elasticSearchType, part.getId().toString());
        index.source(document);
        
        Client client = client();
        try {
            client.index(index).actionGet(timeout);
        } catch (ElasticSearchException e) {
            log.error("Could not index part " + document, e);
            throw e;
        } finally {
            client.close();
        }
    }

    @Async
    public void delete(Part part) throws Exception {
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

    @Override
    protected GenericDao<?> getDao() {
        return partDao;
    }

    @Override
    protected String getSearchId(Object o) {
        Part part = (Part) o;
        return part.getId().toString();
    }

    @Override
    protected String toSearchJson(Object o) {
        Part part = (Part) o;
        return part.toSearchJson();
    }

}
