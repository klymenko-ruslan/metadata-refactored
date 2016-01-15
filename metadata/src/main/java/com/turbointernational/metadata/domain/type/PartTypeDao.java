package com.turbointernational.metadata.domain.type;

import com.turbointernational.metadata.domain.AbstractDao;
import com.turbointernational.metadata.domain.part.PartDao;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;

/**
 *
 * @author jrodriguez
 */
@Repository
public class PartTypeDao extends AbstractDao<PartType> {
    
    public PartTypeDao() {
        super(PartType.class);
    }

    public PartType findPartTypeByValue(String value) {
        try {
            return (PartType) em.createNamedQuery("findPartTypeByValue").setParameter("value", value).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
