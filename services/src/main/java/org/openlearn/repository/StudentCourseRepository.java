package org.openlearn.repository;

import org.openlearn.domain.Course;
import org.openlearn.domain.StudentCourse;

import org.openlearn.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;

/**
 * Spring Data JPA repository for the StudentCourse entity.
 */
public interface StudentCourseRepository extends JpaRepository<StudentCourse, Long> {

	Page<StudentCourse> findByStudent(User student, Pageable pageable);

	Page<StudentCourse> findByCourse(Course course, Pageable pageable);
}
