package org.openlearn.repository;

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
public interface SessionRepository extends JpaRepository<Session,Long> {

	@Query("select session from Session session where session.organization.id in (:organizationIds)")
	public Page<Session> findAllByOrganization(Pageable pageable, @Param("organizationIds") Set<Long> organizationIds);

    @Query("select distinct session from Session session left join fetch session.programs")
    List<Session> findAllWithEagerRelationships();

    @Query("select session from Session session left join fetch session.programs where session.id =:id")
    Session findOneWithEagerRelationships(@Param("id") Long id);

}
