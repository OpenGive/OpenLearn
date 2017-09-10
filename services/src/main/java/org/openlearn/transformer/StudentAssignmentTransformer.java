package org.openlearn.transformer;

import org.openlearn.domain.Authority;
import org.openlearn.domain.StudentAssignment;
import org.openlearn.dto.StudentAssignmentDTO;
import org.openlearn.repository.AssignmentRepository;
import org.openlearn.repository.UserRepository;
import org.openlearn.security.AuthoritiesConstants;
import org.springframework.stereotype.Service;

@Service
public class StudentAssignmentTransformer {

	private static final Authority STUDENT = new Authority(AuthoritiesConstants.STUDENT);

	private final AssignmentRepository assignmentRepository;

	private final UserRepository userRepository;

	public StudentAssignmentTransformer(AssignmentRepository assignmentRepository, UserRepository userRepository) {
		this.assignmentRepository = assignmentRepository;
		this.userRepository = userRepository;
	}

	public StudentAssignmentDTO transform(StudentAssignment studentAssignment) {
		StudentAssignmentDTO studentAssignmentDTO = new StudentAssignmentDTO();
		studentAssignmentDTO.setId(studentAssignment.getId());
		studentAssignmentDTO.setStudentId(studentAssignment.getStudent().getId());
		studentAssignmentDTO.setAssignmentId(studentAssignment.getAssignment().getId());
		studentAssignmentDTO.setGrade(studentAssignment.getGrade());
		studentAssignmentDTO.setComplete(studentAssignment.getComplete());
		studentAssignmentDTO.setOnPortfolio(studentAssignment.getOnPortfolio());
		return studentAssignmentDTO;
	}

	public StudentAssignment transform(StudentAssignmentDTO studentAssignmentDTO) {
		StudentAssignment studentAssignment = new StudentAssignment();
		studentAssignment.setId(studentAssignmentDTO.getId());
		studentAssignment.setStudent(userRepository.findOneByIdAndAuthority(studentAssignmentDTO.getStudentId(), STUDENT));
		studentAssignment.setAssignment(assignmentRepository.findOne(studentAssignmentDTO.getAssignmentId()));
		studentAssignment.setGrade(studentAssignmentDTO.getGrade());
		studentAssignment.setComplete(studentAssignmentDTO.getComplete());
		studentAssignment.setOnPortfolio(studentAssignmentDTO.getOnPortfolio());
		return studentAssignment;
	}
}
