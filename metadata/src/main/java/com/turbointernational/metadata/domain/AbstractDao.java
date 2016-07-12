package com.turbointernational.metadata.domain;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.*;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.util.List;
import java.util.Observer;

import static org.hibernate.LockMode.NONE;
import static org.hibernate.ScrollMode.FORWARD_ONLY;

/**
 *
 * @author jrodriguez
 * @param <T> The entity class to operate on
 */
public abstract class AbstractDao<T extends Serializable> {

    protected final Class<T> clazz;

    @PersistenceContext
    protected EntityManager em;

    public EntityManager getEntityManager() {
        return em;
    }
    
    public AbstractDao(Class<T> clazz) {
        this.clazz = clazz;
    }

    public T findOne(Long id) {
        return em.find(clazz, id);
    }

    public List<T> findAll() {
        return em.createQuery("FROM " + clazz.getName())
                .getResultList();
    }

    public List<T> findAll(int firstResult, int maxResults) {
        return em.createQuery("SELECT o FROM " + clazz.getName() + " o", clazz)
                .setFirstResult(firstResult)
                .setMaxResults(maxResults)
                .getResultList();
    }

    public ScrollableResults getScrollableResults(int fetchSize, boolean distinct, String orderProperty) {
        Session hibernateSession = (Session) em.getDelegate();
        String sql ="SELECT ";
        if (distinct) {
            sql += " DISTINCT ";
        }
        sql ="o FROM " + clazz.getName() + " o";
        if (StringUtils.isNotBlank(orderProperty)) {
            sql += (" ORDER BY o." + orderProperty);
        }
        Query query = hibernateSession.createQuery(sql);
        query.setFetchSize(fetchSize);
        query.setReadOnly(true);
        query.setLockMode("o", NONE);
        ScrollableResults retVal = query.scroll(FORWARD_ONLY);
        return retVal;
    }

    /*
    public void mapAll(int fetchSize, boolean distinct, String orderProperty, Observer observer) {
       ScrollableResults results = getScrollableResult(fetchSize, distinct, orderProperty);
        try {
            while (results.next()) {
                Object entity = results.get(0);
                observer.update(null, entity);
            }
        } finally {
            results.close();
        }
        observer.update(null, null); // signal -- end of processing
    }
    */

    public int getTotal() {
        return ((Number) em.createQuery("SELECT count(o) FROM " +
                clazz.getName() + " o").getSingleResult()).intValue();
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

    public void delete(Long entityId) {
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
