package org.openlearn.repository;

import org.openlearn.domain.Organization;
import org.openlearn.domain.Program;
import org.openlearn.domain.Session;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Spring Data JPA repository for the Session entity.
 */
public interface SessionRepository extends JpaRepository<Session, Long> {

	List<Session> findByOrganization(Organization organization);

	int countByProgram(Program program);
}
