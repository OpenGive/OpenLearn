package org.openlearn.repository;

import org.openlearn.domain.Program;

import org.openlearn.domain.Session;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

/**
 * Spring Data JPA repository for the Program entity.
 */
@SuppressWarnings("unused")
public interface SessionRepository extends JpaRepository<Session,Long> {

	@Query("select session from Session session where session.program.organization.id in (:organizationIds)")
	Page<Session> findAllByOrganizationIds(Pageable pageable, @Param("organizationIds") Set<Long> organizationIds);

	@Query("select session from Session session where program.id = :id and session.program.organization.id in (:organizationIds)")
	Session findOneByOrganizationIds(@Param("id")Long id, @Param("organizationIds") Set<Long> organizationIds);
}
