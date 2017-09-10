package org.openlearn.repository;

import org.openlearn.domain.Organization;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;

/**
 * Spring Data JPA repository for the Organization entity.
 */
public interface OrganizationRepository extends JpaRepository<Organization, Long> {

	Page<Organization> findAllById(Long id, Pageable pageable);
}
