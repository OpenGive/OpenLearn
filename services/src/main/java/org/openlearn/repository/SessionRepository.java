package org.openlearn.repository;

import org.openlearn.domain.Session;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

/**
 * Spring Data JPA repository for the Program entity.
 */
public interface SessionRepository extends JpaRepository<Session, Long> {

	@Query("select session from Session session where session.program.organization.id = :organizationId")
	Page<Session> findAllByOrganizationId(Pageable pageable, @Param("organizationId") Long organizationId);

	@Query("select session from Session session where program.id = :id and session.program.organization.id = :organizationId")
	Session findOneByOrganizationId(@Param("id") Long id, @Param("organizationId") Long organizationId);
}
