package com.turbointernational.metadata.dao;

import java.util.List;

import javax.persistence.NoResultException;

import org.springframework.stereotype.Repository;

import com.turbointernational.metadata.entity.PartType;

/**
 *
 * @author jrodriguez
 */
@Repository
public class PartTypeDao extends AbstractDao<PartType> {

    public PartTypeDao() {
        super(PartType.class);
    }

    @Override
    public List<PartType> findAll() {
        return em.createNamedQuery("findAllPartTypes", PartType.class).getResultList();
    }

    public PartType findPartTypeByValue(String value) {
        try {
            return (PartType) em.createNamedQuery("findPartTypeByValue").setParameter("value", value).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
