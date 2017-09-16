package org.openlearn.transformer;

import org.openlearn.domain.Authority;
import org.openlearn.domain.StudentAssignment;
import org.openlearn.dto.StudentAssignmentDTO;
import org.openlearn.repository.AssignmentRepository;
import org.openlearn.repository.StudentAssignmentRepository;
import org.openlearn.repository.UserRepository;
import org.openlearn.security.AuthoritiesConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class StudentAssignmentTransformer {

	private static final Authority STUDENT = new Authority(AuthoritiesConstants.STUDENT);

	private static final Logger log = LoggerFactory.getLogger(StudentAssignmentTransformer.class);

	private final AssignmentRepository assignmentRepository;

	private final AssignmentTransformer assignmentTransformer;

	private final StudentTransformer studentTransformer;

	private final StudentAssignmentRepository studentAssignmentRepository;

	private final UserRepository userRepository;

	public StudentAssignmentTransformer(final AssignmentRepository assignmentRepository,
	                                    final AssignmentTransformer assignmentTransformer,
	                                    final StudentAssignmentRepository studentAssignmentRepository,
	                                    final StudentTransformer studentTransformer,
	                                    final UserRepository userRepository) {
		this.assignmentRepository = assignmentRepository;
		this.assignmentTransformer = assignmentTransformer;
		this.studentAssignmentRepository = studentAssignmentRepository;
		this.studentTransformer = studentTransformer;
		this.userRepository = userRepository;
	}

	/**
	 * Transforms an entity into a DTO
	 *
	 * @param studentAssignment entity to transform
	 * @return the new DTO with respective student and assignment DTOs
	 */
	public StudentAssignmentDTO transform(final StudentAssignment studentAssignment) {
		return transform(studentAssignment, true, true);
	}

	/**
	 * Transforms an entity into a DTO
	 *
	 * @param studentAssignment entity to transform
	 * @param withStudent flag indicating whether to include the student DTO
	 * @param withAssignment flag indicating whether to include the assignment DTO
	 * @return the new DTO
	 */
	public StudentAssignmentDTO transform(final StudentAssignment studentAssignment,final boolean withStudent,
	                                      final boolean withAssignment) {
		log.debug("Transforming student assignment to student assignment DTO with"
			+ (withStudent ? "" : "out") + " student and with"
			+ (withAssignment ? "" : "out") + " assignment : {}", studentAssignment);
		StudentAssignmentDTO studentAssignmentDTO = new StudentAssignmentDTO();
		studentAssignmentDTO.setId(studentAssignment.getId());
		studentAssignmentDTO.setStudentId(studentAssignment.getStudent().getId());
		studentAssignmentDTO.setAssignmentId(studentAssignment.getAssignment().getId());
		if (withStudent) {
			studentAssignmentDTO.setStudent(studentTransformer.transform(studentAssignment.getStudent()));
		}
		if (withAssignment) {
			studentAssignmentDTO.setAssignment(assignmentTransformer.transform(studentAssignment.getAssignment()));
		}
		studentAssignmentDTO.setGrade(studentAssignment.getGrade());
		studentAssignmentDTO.setComplete(studentAssignment.getComplete());
		studentAssignmentDTO.setOnPortfolio(studentAssignment.getOnPortfolio());
		return studentAssignmentDTO;
	}

	/**
	 * Transforms a DTO into an entity
	 *
	 * @param studentAssignmentDTO DTO to transform
	 * @return the new entity
	 */
	public StudentAssignment transform(final StudentAssignmentDTO studentAssignmentDTO) {
		log.debug("Transforming student assignment DTO to student assignment : {}", studentAssignmentDTO);
		StudentAssignment studentAssignment = studentAssignmentDTO.getId() == null ? new StudentAssignment() : studentAssignmentRepository.findOne(studentAssignmentDTO.getId());
		// TODO: Error handling
		studentAssignment.setStudent(userRepository.findOneByIdAndAuthority(studentAssignmentDTO.getStudentId(), STUDENT));
		studentAssignment.setAssignment(assignmentRepository.findOne(studentAssignmentDTO.getAssignmentId()));
		studentAssignment.setGrade(studentAssignmentDTO.getGrade());
		studentAssignment.setComplete(studentAssignmentDTO.getComplete());
		studentAssignment.setOnPortfolio(studentAssignmentDTO.getOnPortfolio());
		return studentAssignment;
	}
}
