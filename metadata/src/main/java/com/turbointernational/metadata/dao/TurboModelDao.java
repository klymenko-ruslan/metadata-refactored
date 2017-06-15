package com.turbointernational.metadata.dao;

import java.util.List;

import javax.persistence.NoResultException;

import com.turbointernational.metadata.entity.TurboModel;
import org.springframework.stereotype.Repository;

/**
 *
 * @author jrodriguez
 */
@Repository
public class TurboModelDao extends AbstractDao<TurboModel> {

  public TurboModelDao() {
    super(TurboModel.class);
  }

  public List<TurboModel> findTurboModelsByTurboTypeId(Long turboTypeId) {
    return em.createNamedQuery("findTurboModelsByTurboTypeId", TurboModel.class)
        .setParameter("turboTypeId", turboTypeId).getResultList();
  }

  public TurboModel findTurboModel(Long turboTypeId, String name) {
    try {
      return em.createNamedQuery("findTurboModelByTurboTypeIdAndName", TurboModel.class)
          .setParameter("turboTypeId", turboTypeId).setParameter("name", name).getSingleResult();
    } catch (NoResultException e) {
      return null;
    }
  }

}
