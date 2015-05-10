package com.turbointernational.metadata.domain.other;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 *
 * @author jrodriguez
 */
@RepositoryRestResource(path = "SalesNote")
public interface SalesNoteRepository extends PagingAndSortingRepository<SalesNote, Long> {
//    List<SalesNote> findByPartId(@Param("partId") Long partId);
}