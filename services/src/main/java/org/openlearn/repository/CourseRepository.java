package org.openlearn.repository;

import org.openlearn.domain.Course;
import org.openlearn.domain.Organization;
import org.openlearn.domain.Session;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Spring Data JPA repository for the Course entity.
 */
public interface CourseRepository extends JpaRepository<Course, Long> {

	List<Course> findByOrganization(Organization organization);

	boolean existsBySession(Session session);
}
