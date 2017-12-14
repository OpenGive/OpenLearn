package org.openlearn.repository;

import org.openlearn.domain.Organization;
import org.openlearn.domain.PortfolioItem;
import org.openlearn.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Spring Data JPA repository for the PortfolioItem entity.
 */
public interface PortfolioItemRepository extends JpaRepository<PortfolioItem, Long> {

	List<PortfolioItem> findByOrganization(Organization organization);

	List<PortfolioItem> findByStudent(User student);
}
