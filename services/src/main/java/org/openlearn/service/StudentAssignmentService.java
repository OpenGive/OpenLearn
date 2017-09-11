package org.openlearn.service;

import org.openlearn.domain.Authority;
import org.openlearn.domain.Assignment;
import org.openlearn.domain.StudentAssignment;
import org.openlearn.domain.User;
import org.openlearn.dto.StudentAssignmentDTO;
import org.openlearn.repository.AssignmentRepository;
import org.openlearn.repository.StudentAssignmentRepository;
import org.openlearn.repository.UserRepository;
import org.openlearn.security.AuthoritiesConstants;
import org.openlearn.security.SecurityUtils;
import org.openlearn.transformer.StudentAssignmentTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing StudentAssignment.
 */
@Service
@Transactional
public class StudentAssignmentService {

	private static final Authority STUDENT = new Authority(AuthoritiesConstants.STUDENT);

	private final Logger log = LoggerFactory.getLogger(StudentAssignmentService.class);

	private final AssignmentRepository assignmentRepository;

	private final StudentAssignmentRepository studentAssignmentRepository;

	private final StudentAssignmentTransformer studentAssignmentTransformer;

	private final UserRepository userRepository;

	private final UserService userService;

	public StudentAssignmentService(AssignmentRepository assignmentRepository,
	                                StudentAssignmentRepository studentAssignmentRepository,
	                                StudentAssignmentTransformer studentAssignmentTransformer,
	                                UserRepository userRepository, UserService userService) {
		this.assignmentRepository = assignmentRepository;
		this.studentAssignmentRepository = studentAssignmentRepository;
		this.studentAssignmentTransformer = studentAssignmentTransformer;
		this.userRepository = userRepository;
		this.userService = userService;
	}

	/**
	 * Save a studentAssignment.
	 *
	 * @param studentAssignmentDTO the entity to save
	 * @return the persisted entity
	 */
	public StudentAssignmentDTO save(StudentAssignmentDTO studentAssignmentDTO) {
		log.debug("Request to save StudentAssignment : {}", studentAssignmentDTO);
		if (SecurityUtils.isAdmin() || inOrgOfCurrentUser(studentAssignmentDTO)) {
			return studentAssignmentTransformer.transform(studentAssignmentRepository
				.save(studentAssignmentTransformer.transform(studentAssignmentDTO)));
		}
		// TODO: Error handling / logging
		return null;
	}

	/**
	 * Get one studentAssignment by id.
	 *
	 * @param id the id of the entity
	 * @return the entity
	 */
	@Transactional(readOnly = true)
	public StudentAssignmentDTO findOne(Long id) {
		log.debug("Request to get StudentAssignment : {}", id);
		StudentAssignment studentAssignment = studentAssignmentRepository.findOne(id);
		if (studentAssignment != null && (SecurityUtils.isAdmin() || inOrgOfCurrentUser(studentAssignment))) {
			return studentAssignmentTransformer.transform(studentAssignment);
		}
		// TODO: Error handling / logging
		return null;
	}

	/**
	 * Get a list studentAssignment by student.
	 *
	 * @param id the id of the student
	 * @param pageable the pagination information
	 * @return the list of student assignments
	 */
	@Transactional(readOnly = true)
	public Page<StudentAssignmentDTO> findByStudent(Long id, Pageable pageable) {
		log.debug("Request to get StudentAssignments by Student : {}", id);
		User student = userRepository.findOneByIdAndAuthority(id, STUDENT);
		if (student != null && (SecurityUtils.isAdmin() || inOrgOfCurrentUser(student))) {
			return studentAssignmentRepository.findByStudent(student, pageable)
				.map((StudentAssignment studentAssignment) -> studentAssignmentTransformer.transform(studentAssignment, false, true));
		}
		// TODO: Error handling / logging
		return null;
	}

	/**
	 * Get a list studentAssignment by assignment.
	 *
	 * @param id the id of the assignment
	 * @param pageable the pagination information
	 * @return the list of student assignments
	 */
	@Transactional(readOnly = true)
	public Page<StudentAssignmentDTO> findByAssignment(Long id, Pageable pageable) {
		log.debug("Request to get StudentAssignments by Assignment : {}", id);
		Assignment assignment = assignmentRepository.findOne(id);
		if (assignment != null && (SecurityUtils.isAdmin() || inOrgOfCurrentUser(assignment))) {
			return studentAssignmentRepository.findByAssignment(assignment, pageable)
				.map((StudentAssignment studentAssignment) -> studentAssignmentTransformer.transform(studentAssignment, true, false));
		}
		// TODO: Error handling / logging
		return null;
	}

	/**
	 * Delete the studentAssignment by id.
	 *
	 * @param id the id of the entity
	 */
	public void delete(Long id) {
		log.debug("Request to delete StudentAssignment : {}", id);
		StudentAssignment studentAssignment = studentAssignmentRepository.findOne(id);
		if (studentAssignment != null && (SecurityUtils.isAdmin() || inOrgOfCurrentUser(studentAssignment))) {
			studentAssignmentRepository.delete(id);
		} else {
			// TODO: Error handling / logging
		}
	}

	/**
	 * Determines if a studentAssignment is in the organization of current user
	 *
	 * @param studentAssignmentDTO the studentAssignment
	 * @return true if studentAssignment and current user are in the same org
	 */
	private boolean inOrgOfCurrentUser(StudentAssignmentDTO studentAssignmentDTO) {
		User user = userService.getCurrentUser();
		User student = userRepository.findOneByIdAndAuthority(studentAssignmentDTO.getStudentId(), STUDENT);
		Assignment assignment = assignmentRepository.findOne(studentAssignmentDTO.getAssignmentId());
		return user.getOrganization().getId().equals(student.getOrganization().getId())
			&& user.getOrganization().getId().equals(assignment.getOrganization().getId());
	}

	/**
	 * Determines if a studentAssignment is in the organization of current user
	 *
	 * @param studentAssignment the studentAssignment
	 * @return true if studentAssignment and current user are in the same org
	 */
	private boolean inOrgOfCurrentUser(StudentAssignment studentAssignment) {
		User user = userService.getCurrentUser();
		return user.getOrganization().equals(studentAssignment.getStudent().getOrganization())
			&& user.getOrganization().equals(studentAssignment.getAssignment().getOrganization());
	}

	/**
	 * Determines if a student is in the organization of current user
	 *
	 * @param student the student
	 * @return true if assignment and current user are in the same org
	 */
	private boolean inOrgOfCurrentUser(User student) {
		User user = userService.getCurrentUser();
		return user.getOrganization().equals(student.getOrganization());
	}

	/**
	 * Determines if a assignment is in the organization of current user
	 *
	 * @param assignment the assignment
	 * @return true if assignment and current user are in the same org
	 */
	private boolean inOrgOfCurrentUser(Assignment assignment) {
		User user = userService.getCurrentUser();
		return user.getOrganization().equals(assignment.getOrganization());
	}
}
