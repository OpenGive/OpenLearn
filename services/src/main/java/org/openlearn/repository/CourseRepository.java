package org.openlearn.repository;

import org.openlearn.domain.Course;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

/**
 * Spring Data JPA repository for the Course entity.
 */
public interface CourseRepository extends JpaRepository<Course, Long> {
	@Query("select course from Course course where course.instructor.id = :id")
	Page<Course> findAllByInstructorId(@Param("id") Long id, Pageable pageable);

	@Query("select course from Course course where course.session.program.organization.id = :organizationId")
	Page<Course> findAllByOrganizationId(Pageable pageable, @Param("organizationId") Long organizationId);

	@Query("select course from Course course where course.id = :id and course.session.program.organization.id = :organizationId")
	Course findOneByIdAndOrganizationId(@Param("id") Long id, @Param("organizationId") Long organiztionId);

	@Query("select c from Course c join StudentCourse sc on sc.courseId = c.id where user_id = :id")
	Page<Course> findCoursesByStudent(Pageable pageable, @Param("id") Long id);
}
