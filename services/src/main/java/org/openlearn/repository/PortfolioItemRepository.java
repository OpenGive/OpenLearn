package org.openlearn.repository;

import org.openlearn.domain.PortfolioItem;

import org.springframework.data.jpa.repository.*;

/**
 * Spring Data JPA repository for the PortfolioItem entity.
 */
public interface PortfolioItemRepository extends JpaRepository<PortfolioItem,Long> {

}
