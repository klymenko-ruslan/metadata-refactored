package com.turbointernational.metadata.domain.part.salesnote;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 *
 * @author jrodriguez
 */
@RepositoryRestResource
public interface SalesNoteRepository extends JpaRepository<SalesNote, Long>, SalesNoteRepositoryCustom {
    
//    @Query("SELECT sn FROM SalesNote sn JOIN sn.parts snp WHERE snp.id = :partId")
//    Page<SalesNote> findByPartId(Pageable pageable, @Param("partId") Long partId);
}