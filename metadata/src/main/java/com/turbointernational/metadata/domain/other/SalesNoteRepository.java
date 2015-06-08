package com.turbointernational.metadata.domain.other;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.security.access.annotation.Secured;

/**
 *
 * @author jrodriguez
 */
@RepositoryRestResource
@Secured("ROLE_READ")
public interface SalesNoteRepository extends JpaRepository<SalesNote, Long> {
    
    @Query("SELECT sn FROM SalesNote sn JOIN sn.parts snp WHERE snp.id = :partId")
    Page<SalesNote> findByPartId(Pageable pageable, @Param("partId") Long partId);
}