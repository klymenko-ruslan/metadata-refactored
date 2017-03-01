package com.turbointernational.metadata.entity;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Autowired;

import com.turbointernational.metadata.web.dto.SalesNoteSearchRequest;

/**
 *
 * @author jrodriguez
 */
public class SalesNoteRepositoryImpl implements SalesNoteRepositoryCustom {

    @Autowired(required=true)
    EntityManager entityManager;

    @Override
    public SalesNoteSearchResponse search(SalesNoteSearchRequest request) {
        long total = buildSearchQuery("SELECT COUNT(sn)", Number.class, request)
                .getSingleResult().longValue();

        List<SalesNote> results = buildSearchQuery("SELECT sn", SalesNote.class, request)
                .setFirstResult(request.getPage() * request.getPageSize())
                .setMaxResults(request.getPageSize())
                .getResultList();

        return new SalesNoteSearchResponse(total, request.getPageSize(), request.getPage(), results);
    }

    private <T> TypedQuery<T> buildSearchQuery(String selectClause, Class<T> resultClass, SalesNoteSearchRequest request) {
        if (request.getStates().isEmpty()) {
            throw new IllegalArgumentException("A SalesNoteState is required.");
        }

        return entityManager.createQuery(
            selectClause + "\n" +
            "FROM SalesNote sn\n" +
            "JOIN sn.parts snp\n" +
            "JOIN snp.pk.part p\n" +
            "WHERE\n" +
            "  sn.state IN :states\n" +
            "  AND (\n" +
            "    :primaryPartId IS NULL\n" +
            "    OR p.id = :primaryPartId\n" +
            "  ) AND (\n" +
            "    :query IS NULL\n" +
            "    OR LOWER(p.manufacturerPartNumber) LIKE :query\n" +
            "    OR LOWER(sn.comment) LIKE :query\n" +
            "  ) AND (\n" +
            "    (:includePrimary = true AND snp.primary = true)\n" +
            "    OR (:includeRelated = true AND snp.primary IS NULL)\n" +
            "  )", resultClass)
            .setParameter("states", request.getStates())
            .setParameter("primaryPartId", request.getPrimaryPartId())
            .setParameter("query", request.getFormattedQuery())
            .setParameter("includePrimary", request.isIncludePrimary())
            .setParameter("includeRelated", request.isIncludeRelated());
    }
}
