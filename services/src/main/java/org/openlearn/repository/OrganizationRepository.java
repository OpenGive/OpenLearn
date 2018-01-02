package org.openlearn.repository;

import org.openlearn.domain.Organization;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Spring Data JPA repository for the Organization entity.
 */
public interface OrganizationRepository extends JpaRepository<Organization, Long> {

	Organization findOneById(Long id);

	List<Organization> findById(Long id);
}
