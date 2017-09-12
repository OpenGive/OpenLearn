package org.openlearn.repository;

import org.openlearn.domain.Organization;
import org.openlearn.domain.Program;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;

/**
 * Spring Data JPA repository for the Program entity.
 */
public interface ProgramRepository extends JpaRepository<Program, Long> {

	Page<Program> findByOrganization(Organization organization, Pageable pageable);
}
