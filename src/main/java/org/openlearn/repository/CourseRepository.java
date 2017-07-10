package org.openlearn.repository;

import org.openlearn.domain.Course;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;

/**
 * Spring Data JPA repository for the Course entity.
 */
@SuppressWarnings("unused")
public interface CourseRepository extends JpaRepository<Course,Long> {
	public Page<Course> findAllByInstructorId(Long id, Pageable pageable);

}
