package org.openlearn.repository;

import org.openlearn.domain.Assignment;
import org.openlearn.domain.StudentAssignment;
import org.openlearn.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Spring Data JPA repository for the StudentAssignment entity.
 */
public interface StudentAssignmentRepository extends JpaRepository<StudentAssignment, Long> {

	List<StudentAssignment> findByStudent(User student);

	List<StudentAssignment> findByStudentAndAndOnPortfolio(User student, Boolean onPortfolio);

	List<StudentAssignment> findByAssignment(Assignment assignment);

	void deleteByAssignment(Assignment assignment);
}
