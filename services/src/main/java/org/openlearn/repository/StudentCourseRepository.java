package org.openlearn.repository;

import org.openlearn.domain.Course;
import org.openlearn.domain.StudentCourse;
import org.openlearn.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Spring Data JPA repository for the StudentCourse entity.
 */
public interface StudentCourseRepository extends JpaRepository<StudentCourse, Long> {

	List<StudentCourse> findByStudent(User student);

	List<StudentCourse> findByStudentAndOnPortfolio(User student, Boolean onPortfolio);

	List<StudentCourse> findByCourse(Course course);
}
