package org.openlearn.repository;

import org.openlearn.domain.Assignment;
import org.openlearn.domain.Course;
import org.openlearn.domain.Organization;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Spring Data JPA repository for the Assignment entity.
 */
public interface AssignmentRepository extends JpaRepository<Assignment, Long> {

	List<Assignment> findByCourse(Course course);

	boolean existsByCourse(Course course);

	List<Assignment> findByOrganization(Organization organization);
}
