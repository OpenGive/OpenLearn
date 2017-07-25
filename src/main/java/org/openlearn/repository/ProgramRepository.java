package org.openlearn.repository;

import org.openlearn.domain.Program;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

/**
 * Spring Data JPA repository for the Program entity.
 */
@SuppressWarnings("unused")
public interface ProgramRepository extends JpaRepository<Program,Long> {

	@Query("select program from Program program where program.session.organization.id in (:organizationIds)")
	List<Program> findAllByOrganizationIds(@Param("organizationIds") Set<Long> organizationIds);

	@Query("select program from Program program where program.id = :id and program.session.organization.id in (:organizationIds)")
	Program findOneByOrganizationIds(@Param("id")Long id, @Param("organizationIds") Set<Long> organizationIds);
}
