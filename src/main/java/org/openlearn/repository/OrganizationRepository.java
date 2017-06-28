package org.openlearn.repository;

import org.openlearn.domain.Organization;

import org.springframework.data.jpa.repository.*;

/**
 * Spring Data JPA repository for the Organization entity.
 */
@SuppressWarnings("unused")
public interface OrganizationRepository extends JpaRepository<Organization,Long> {

}
