package com.turbointernational.metadata.domain;

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

    String toSearchJson();

    String getSearchId();

}
