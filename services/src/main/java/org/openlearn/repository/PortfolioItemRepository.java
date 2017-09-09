package org.openlearn.repository;

import org.openlearn.domain.Organization;
import org.openlearn.domain.PortfolioItem;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;

/**
 * Spring Data JPA repository for the PortfolioItem entity.
 */
public interface PortfolioItemRepository extends JpaRepository<PortfolioItem, Long> {

	@Query(value = "SELECT pi FROM PortfolioItem pi WHERE pi.student.organization = ?1")
	Page<PortfolioItem> findAllByOrganization(Organization organization, Pageable pageable);
}
