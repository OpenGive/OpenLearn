package org.openlearn.repository;

import org.openlearn.domain.Organization;
import org.openlearn.domain.Session;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;

/**
 * Spring Data JPA repository for the Session entity.
 */
public interface SessionRepository extends JpaRepository<Session, Long> {

	@Query(value = "SELECT s FROM Session s WHERE s.id = ?1 AND s.program.organization = ?2")
	Session findOneByIdAndOrganization(Long id, Organization organization);

	@Query(value = "SELECT s FROM Session s WHERE s.program.organization = ?1")
	Page<Session> findAllByOrganization(Organization organization, Pageable pageable);
}
