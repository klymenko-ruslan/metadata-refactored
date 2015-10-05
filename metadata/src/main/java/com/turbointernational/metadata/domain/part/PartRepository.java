package com.turbointernational.metadata.domain.part;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.security.access.annotation.Secured;

/**
 *
 * @author jrodriguez
 */
@RepositoryRestResource
@Secured("ROLE_READ")
public interface PartRepository extends JpaRepository<Part, Long> {
}