package org.openlearn.repository;

import org.openlearn.domain.Course;

import org.openlearn.domain.Organization;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;

/**
 * Spring Data JPA repository for the Course entity.
 */
public interface CourseRepository extends JpaRepository<Course, Long> {

	@Query(value = "SELECT c FROM Course c WHERE c.id = ?1 AND c.session.program.organization = ?2")
	Course findOneByIdAndOrganization(Long id, Organization organization);

	@Query(value = "SELECT c FROM Course c WHERE c.session.program.organization = ?1")
	Page<Course> findAllByOrganization(Organization organization, Pageable pageable);
}
