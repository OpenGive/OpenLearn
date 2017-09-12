package org.openlearn.repository;

import org.openlearn.domain.Organization;
import org.openlearn.domain.Session;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the Session entity.
 */
public interface SessionRepository extends JpaRepository<Session, Long> {

	Page<Session> findByOrganization(Organization organization, Pageable pageable);
}
