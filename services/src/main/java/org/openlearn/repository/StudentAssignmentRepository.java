package org.openlearn.repository;

import org.openlearn.domain.Assignment;
import org.openlearn.domain.StudentAssignment;
import org.openlearn.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the StudentAssignment entity.
 */
public interface StudentAssignmentRepository extends JpaRepository<StudentAssignment, Long> {

	Page<StudentAssignment> findByStudent(User student, Pageable pageable);

	Page<StudentAssignment> findByAssignment(Assignment assignment, Pageable pageable);
}
