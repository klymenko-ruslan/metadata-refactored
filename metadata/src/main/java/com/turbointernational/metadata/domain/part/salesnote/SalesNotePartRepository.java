package com.turbointernational.metadata.domain.part.salesnote;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.security.access.annotation.Secured;

/**
 *
 * @author jrodriguez
 */

@RepositoryRestResource
@Secured("ROLE_SALES_NOTE_READ")
public interface SalesNotePartRepository extends JpaRepository<SalesNotePart, Long> {
}