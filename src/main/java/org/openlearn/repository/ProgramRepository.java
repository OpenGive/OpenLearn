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
 * Spring Data JPA repository for the Session entity.
 */
@SuppressWarnings("unused")
public interface ProgramRepository extends JpaRepository<Program,Long> {

	@Query("select Program from Program program where program.organization.id in (:organizationIds)")
	public List<Program> findAllByOrganization(@Param("organizationIds") Set<Long> organizationIds);

    @Query("select distinct program from Program program left join fetch session.programs")
    List<Program> findAllWithEagerRelationships();

    @Query("select program from Program program left join fetch program.sessions where program.id =:id")
	Program findOneWithEagerRelationships(@Param("id") Long id);

	@Query("select Program from Program program left join fetch program.sessions where program.id =:id and program.organization.id in (:organizationIds)")
	Program findOneByIdAndOrgIdsWithEagerRelationships(@Param("id") Long id, @Param("organizationIds") Set<Long> organizationIds);
}
