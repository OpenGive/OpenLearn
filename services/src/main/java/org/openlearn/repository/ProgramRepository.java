package org.openlearn.repository;

import org.openlearn.domain.Program;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Session entity.
 */
public interface ProgramRepository extends JpaRepository<Program, Long> {

	@Query("select program from Program program where program.organization.id = :organizationId")
	List<Program> findAllByOrganization(@Param("organizationId") Long organizationId);

	@Query("select program from Program program where program.id =:id and program.organization.id = :organizationIds")
	Program findOneByIdAndOrganization(@Param("id") Long id, @Param("organizationId") Long organizationId);
}
