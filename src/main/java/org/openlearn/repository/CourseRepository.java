package org.openlearn.repository;

import org.openlearn.domain.Course;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.Set;

/**
 * Spring Data JPA repository for the Course entity.
 */
@SuppressWarnings("unused")
public interface CourseRepository extends JpaRepository<Course,Long> {
	public Page<Course> findAllByInstructorId(Long id, Pageable pageable);

	@Query("select course from Course course where course.program.session.organization.id in (:organizationIds)")
	Page<Course> findAllByOrganizationId(Pageable pageable, @Param("organizationIds") Set<Long> organizationIds);
}
