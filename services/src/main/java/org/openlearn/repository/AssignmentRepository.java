package org.openlearn.repository;

import org.openlearn.domain.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the Assignment entity.
 */
public interface AssignmentRepository extends JpaRepository<Assignment, Long> {
}
