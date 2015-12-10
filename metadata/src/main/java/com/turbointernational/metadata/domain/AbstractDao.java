package com.turbointernational.metadata.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author jrodriguez
 * @param <T> The entity class to operate on
 */
public abstract class AbstractDao<T extends Serializable> {

    protected final Class<T> clazz;

    @PersistenceContext
    protected EntityManager em;
    
    @Autowired(required=true)
    protected PlatformTransactionManager txManager;

    public EntityManager getEntityManager() {
        return em;
    }
    
    public AbstractDao(Class<T> clazz) {
        this.clazz = clazz;
    }

    public T findOne(long id) {
        return em.find(clazz, id);
    }

    public List< T> findAll() {
        return em.createQuery("FROM " + clazz.getName())
                .getResultList();
    }

    public List<T> findAll(int firstResult, int maxResults) {
        return em.createQuery("SELECT o FROM " + clazz.getName() + " o", clazz)
                .setFirstResult(firstResult)
                .setMaxResults(maxResults)
                .getResultList();
    }

    public void persist(T entity) {
        em.persist(entity);
    }

    public T merge(T entity) {
        return em.merge(entity);
    }

    public void remove(T entity) {
        em.remove(entity);
    }

    public void delete(long entityId) {
        T entity = findOne(entityId);
        AbstractDao.this.remove(entity);
    }
    
    public long count() {
        return em.createQuery("SELECT COUNT(o) FROM " + clazz.getName() + " o", Long.class)
                .getSingleResult();
    }
    
    @Transactional
    public void flush() {
        em.flush();
    }
    
    @Transactional
    public void clear() {
        em.clear();
    }
    
    public void refresh(T part) {
        em.refresh(part);
    }
}
