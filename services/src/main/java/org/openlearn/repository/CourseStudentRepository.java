package org.openlearn.repository;

import org.openlearn.domain.CourseStudent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for StudentCourse entity.
 */
public interface CourseStudentRepository extends JpaRepository<CourseStudent, Long> {
	Page<CourseStudent> findAllByCourseId(Long courseId, Pageable pageable);
	CourseStudent findOneByCourseIdAndUserId(Long courseId, Long studentId);
}
