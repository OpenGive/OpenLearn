package org.openlearn.repository;

import org.openlearn.domain.Authority;

import org.springframework.data.jpa.repository.*;

/**
 * Spring Data JPA repository for the Authority entity.
 */
public interface AuthorityRepository extends JpaRepository<Authority, String> {
}
