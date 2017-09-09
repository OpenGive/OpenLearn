package org.openlearn.repository;

import org.openlearn.domain.StudentAssignment;

import org.springframework.data.jpa.repository.*;

/**
 * Spring Data JPA repository for the StudentAssignment entity.
 */
public interface StudentAssignmentRepository extends JpaRepository<StudentAssignment, Long> {
}
