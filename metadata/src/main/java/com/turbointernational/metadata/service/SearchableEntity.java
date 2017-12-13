package com.turbointernational.metadata.service;

import com.turbointernational.metadata.dao.AbstractDao;
import com.turbointernational.metadata.entity.CriticalDimension;

import java.util.List;

/**
 * Created by dmytro.trunykov@zorallabs.com on 12/14/15.
 */
public interface SearchableEntity {

    // Don't forget to annotate this method in an inherited class by this annotation:
    //@PostRemove
    void removeSearchIndex() throws Exception;

    // Don't forget to annotate this method in an inherited class by these annotations:
    //@PostUpdate
    //@PostPersist
    void updateSearchIndex() throws Exception;

    /**
     * Callback method that is called before call to {@link #toSearchJson(List)}.
     *
     * This method is useful to initialize transient attributes of a entity before indexing.
     */
    void beforeIndexing(InterchangeService interchangeService);

    String toSearchJson(List<CriticalDimension> criticalDimensions);

    /**
     * Get ID for a document in an ElasticSearch index.
     *
     * @return
     * @see SearchServiceEsImpl#indexDoc(SearchableEntity, String)
     * @see SearchServiceEsImpl#indexAllDocs(AbstractDao, String)
     */
    String getSearchId();

}
