package com.turbointernational.metadata.exception;

import com.turbointernational.metadata.entity.SalesNote;

import java.io.Serializable;

/**
 *
 * @author jrodriguez
 */
public class NoSuchEntityException extends RuntimeException {
    private final Class<? extends Serializable> entityClass;
    private final long entityId;

    public NoSuchEntityException(Class<SalesNote> entityClass, long entityId) {
        super("No " + entityClass.getSimpleName() + " entity with id " + entityId);
        
        this.entityClass = entityClass;
        this.entityId = entityId;
    }

    public Class<? extends Serializable> getEntityClass() {
        return entityClass;
    }

    public long getEntityId() {
        return entityId;
    }
    
}
