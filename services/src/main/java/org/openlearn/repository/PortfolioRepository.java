package org.openlearn.repository;

import org.openlearn.domain.Portfolio;

import org.springframework.data.jpa.repository.*;

/**
 * Spring Data JPA repository for the Portfolio entity.
 */
@SuppressWarnings("unused")
public interface PortfolioRepository extends JpaRepository<Portfolio,Long> {

}
