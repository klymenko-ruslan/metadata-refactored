package com.turbointernational.metadata.dao;

import static org.hibernate.LockMode.NONE;
import static org.hibernate.ScrollMode.FORWARD_ONLY;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author jrodriguez
 * @param <T>
 *            The entity class to operate on
 */
public abstract class AbstractDao<T extends Serializable> {

    protected final Class<T> clazz;

    @PersistenceContext(unitName = "metadata")
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

    public T getReference(Long id) {
        return em.getReference(this.clazz, id);
    }

    @SuppressWarnings("unchecked")
    public List<T> findAll() {
        return em.createQuery("FROM " + clazz.getName()).getResultList();
    }

    public List<T> findAll(int firstResult, int maxResults) {
        return em.createQuery("SELECT o FROM " + clazz.getName() + " o", clazz).setFirstResult(firstResult)
                .setMaxResults(maxResults).getResultList();
    }

    public ScrollableResults getScrollableResults(int fetchSize, boolean distinct, String orderProperty) {
        return getScrollableResults(em, clazz, fetchSize, distinct, orderProperty);
    }

    public static ScrollableResults getScrollableResults(EntityManager entityManager, Class<?> clazz, int fetchSize,
            boolean distinct, String orderProperty) {
        Session hibernateSession = (Session) entityManager.getDelegate();
        String sql = "SELECT ";
        if (distinct) {
            sql += " DISTINCT ";
        }
        sql += "o FROM " + clazz.getName() + " o";
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

    public int getTotal() {
        return getTotal(em, clazz);
    }

    public static int getTotal(EntityManager entityManager, Class<?> clazz) {
        return ((Number) entityManager.createQuery("SELECT count(o) FROM " + clazz.getName() + " o").getSingleResult())
                .intValue();
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
        return em.createQuery("SELECT COUNT(o) FROM " + clazz.getName() + " o", Long.class).getSingleResult();
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
