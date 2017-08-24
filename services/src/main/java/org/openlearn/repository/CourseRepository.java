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
	@Query("select course from Course course where course.instructor.id = :id")
	public Page<Course> findAllByInstructorId(@Param("id") Long id, Pageable pageable);

	@Query("select course from Course course where course.session.program.organization.id in (:organizationIds)")
	Page<Course> findAllByOrganizationId(Pageable pageable, @Param("organizationIds") Set<Long> organizationIds);

	@Query("select course from Course course where course.id = :id and course.session.program.organization.id in (:organizationIds)")
	Course findOneByIdAndOrganizationId(@Param("id") Long id, @Param("organizationIds") Set<Long> organiztionIds);

	@Query("select c from Course c join StudentCourse sc on sc.courseId = c.id where user_id = :id")
	Page<Course> findCoursesByStudent(Pageable pageable, @Param("id") Long id);
}
