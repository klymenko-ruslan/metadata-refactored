package com.turbointernational.metadata.domain;

/**
 * Created by trunikov on 12/14/15.
 */
public interface SearchableEntity {

    // Don't forget to annotate this method in an inherited class by this annotation:
    //@PreRemove
    public void removeSearchIndex() throws Exception;

    // Don't forget to annotate this method in an inherited class by these annotations:
    //@PostUpdate
    //@PostPersist
    public void updateSearchIndex() throws Exception;

}
