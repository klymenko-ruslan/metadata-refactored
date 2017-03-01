package com.turbointernational.metadata.exception;

import java.io.Serializable;

import com.turbointernational.metadata.entity.SalesNote;

/**
 *
 * @author jrodriguez
 */
public class NoSuchEntityException extends RuntimeException {
    private static final long serialVersionUID = 1126855518895885981L;
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
