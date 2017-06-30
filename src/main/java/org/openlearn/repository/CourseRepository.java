package org.openlearn.repository;

import org.openlearn.domain.Course;

import org.springframework.data.jpa.repository.*;

/**
 * Spring Data JPA repository for the Course entity.
 */
@SuppressWarnings("unused")
public interface CourseRepository extends JpaRepository<Course,Long> {

}
