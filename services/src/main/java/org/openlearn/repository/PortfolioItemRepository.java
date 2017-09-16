package org.openlearn.repository;

import org.openlearn.domain.Organization;
import org.openlearn.domain.PortfolioItem;
import org.openlearn.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Spring Data JPA repository for the PortfolioItem entity.
 */
public interface PortfolioItemRepository extends JpaRepository<PortfolioItem, Long> {

	Page<PortfolioItem> findByOrganization(Organization organization, Pageable pageable);

	List<PortfolioItem> findByStudent(User student);
}
