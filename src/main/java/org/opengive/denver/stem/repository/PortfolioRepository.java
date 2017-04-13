package org.opengive.denver.stem.repository;

import org.opengive.denver.stem.domain.Portfolio;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Portfolio entity.
 */
@SuppressWarnings("unused")
public interface PortfolioRepository extends JpaRepository<Portfolio,Long> {

}
