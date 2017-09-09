package org.openlearn.repository;

import org.openlearn.domain.StudentCourse;

import org.springframework.data.jpa.repository.*;

/**
 * Spring Data JPA repository for the StudentCourse entity.
 */
public interface StudentCourseRepository extends JpaRepository<StudentCourse, Long> {
}
