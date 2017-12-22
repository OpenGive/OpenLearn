package org.openlearn.repository;

import org.openlearn.domain.Organization;
import org.openlearn.domain.Program;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Spring Data JPA repository for the Program entity.
 */
public interface ProgramRepository extends JpaRepository<Program, Long> {

	List<Program> findByOrganization(Organization organization);

	int countByOrganization(Organization organization);

}
