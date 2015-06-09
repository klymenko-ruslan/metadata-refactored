package com.turbointernational.metadata.domain.part.salesnote;

import javax.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

/**
 *
 * @author jrodriguez
 */
public class SalesNoteRepositoryImpl implements SalesNoteRepositoryCustom {
    
    @Autowired(required=true)
    private EntityManager entityManager;

    @Override
    public Page<SalesNote> search(SalesNoteSearchRequest request) {
        /*
        SELECT sn
        FROM SalesNote sn
        JOIN sn.parts snp
        WHERE
          sn.state IN :states
          AND (:primaryPartId IS NULL
               OR snp.primary)
          AND (snp.id = :partId OR snp.id = :query)
          AND sn.

        */
        return null;
    }
    
}
