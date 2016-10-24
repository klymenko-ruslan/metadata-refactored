package com.turbointernational.metadata.domain;

import com.turbointernational.metadata.domain.criticaldimension.CriticalDimension;
import com.turbointernational.metadata.domain.part.bom.BOMItemDao;
import com.turbointernational.metadata.domain.part.types.TurboCarModelEngineYearDao;
import com.turbointernational.metadata.services.SearchServiceEsImpl;

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

    String toSearchJson(List<CriticalDimension> criticalDimensions, TurboCarModelEngineYearDao tcmeyDao,
                        BOMItemDao bomItemDao);

    /**
     * Get ID for a document in an ElasticSearch index.
     *
     * @return
     * @see SearchServiceEsImpl#indexDoc(SearchableEntity, String)
     * @see SearchServiceEsImpl#indexAllDocs(AbstractDao, String)
     */
    String getSearchId();

}
