package org.openlearn.repository;

import org.openlearn.domain.Assignment;

import org.openlearn.domain.Course;
import org.openlearn.domain.Organization;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;

/**
 * Spring Data JPA repository for the Assignment entity.
 */
public interface AssignmentRepository extends JpaRepository<Assignment, Long> {

	@Query(value = "SELECT a FROM Assignment a WHERE a.course.session.program.organization = ?1")
	Page<Assignment> findAllByOrganization(Organization organization, Pageable pageable);

	Page<Assignment> findAllByCourse(Course course, Pageable pageable);
}
