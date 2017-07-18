package org.openlearn.repository;

import org.openlearn.domain.Organization;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;

import java.util.Set;

/**
 * Spring Data JPA repository for the Organization entity.
 */
@SuppressWarnings("unused")
public interface OrganizationRepository extends JpaRepository<Organization,Long> {
	public Set<Organization> findAllByUserIds(Long userId);
	public Page<Organization> findAllByIdIn(Pageable page, Set<Long> organizationIds);
}
