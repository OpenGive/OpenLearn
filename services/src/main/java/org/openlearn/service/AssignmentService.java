package org.openlearn.service;

import org.openlearn.domain.*;
import org.openlearn.dto.AssignmentDTO;
import org.openlearn.dto.StudentCourseDTO;
import org.openlearn.repository.AssignmentRepository;
import org.openlearn.repository.CourseRepository;
import org.openlearn.repository.StudentAssignmentRepository;
import org.openlearn.repository.StudentCourseRepository;
import org.openlearn.security.SecurityUtils;
import org.openlearn.security.AuthoritiesConstants;
import org.openlearn.transformer.AssignmentTransformer;
import org.openlearn.web.rest.errors.AccessDeniedException;
import org.openlearn.web.rest.errors.AssignmentNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing Assignment.
 */
@Service
@Transactional
public class AssignmentService {

	private static final Logger log = LoggerFactory.getLogger(AssignmentService.class);

	private final AssignmentRepository assignmentRepository;

	private final AssignmentTransformer assignmentTransformer;

	private final CourseRepository courseRepository;

	private final StudentAssignmentRepository studentAssignmentRepository;

	private final StudentCourseRepository studentCourseRepository;

	private final UserService userService;

	private final FileInformationService fileInformationService;

	public AssignmentService(final AssignmentRepository assignmentRepository,
	                         final AssignmentTransformer assignmentTransformer,
	                         final CourseRepository courseRepository,
							 final StudentAssignmentRepository studentAssignmentRepository,
							 final StudentCourseRepository studentCourseRepository,
							 final UserService userService,
							 final FileInformationService fileInformationService) {
		this.assignmentRepository = assignmentRepository;
		this.assignmentTransformer = assignmentTransformer;
		this.courseRepository = courseRepository;
		this.studentAssignmentRepository = studentAssignmentRepository;
		this.studentCourseRepository = studentCourseRepository;
		this.userService = userService;
		this.fileInformationService = fileInformationService;
	}

	/**
	 * Save an assignment.
	 *
	 * @param assignmentDTO the entity to save
	 * @return the persisted entity
	 */
	public AssignmentDTO save(final AssignmentDTO assignmentDTO) {
		log.debug("Request to save Assignment : {}", assignmentDTO);
		User user = userService.getCurrentUser();
		boolean instructorCheck = true;
		if (SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.INSTRUCTOR)) {
			Course course = courseRepository.findOne(assignmentDTO.getCourseId());
			instructorCheck = user.getId() == course.getInstructor().getId();
		}

		if (instructorCheck && (SecurityUtils.isAdmin() || inOrgOfCurrentUser(assignmentDTO))) {
			Assignment assignment = assignmentRepository.save(assignmentTransformer.transform(assignmentDTO));

			for (StudentCourse studentCourse : studentCourseRepository.findByCourse(assignment.getCourse())) {
				StudentAssignment studentAssignment = new StudentAssignment();
				studentAssignment.setAssignment(assignment);
				studentAssignment.setComplete(false);
				studentAssignment.setOnPortfolio(false);
				studentAssignment.setStudent(studentCourse.getStudent());
				studentAssignmentRepository.save(studentAssignment);
			}

			return assignmentTransformer.transform(assignment);
		}
		// TODO: Error handling / logging
		return null;
	}

	/**
	 * Get all the assignments.
	 *
	 * @param pageable the pagination information
	 * @return the list of entities
	 */
	@Transactional(readOnly = true)
	public Page<AssignmentDTO> findAll(final Pageable pageable) {
		log.debug("Request to get all Assignments");
		User user = userService.getCurrentUser();
		if (SecurityUtils.isAdmin()) {
			return assignmentRepository.findAll(pageable).map(assignmentTransformer::transform);
		} else {
			return assignmentRepository.findByOrganization(user.getOrganization(), pageable)
				.map(assignmentTransformer::transform);
		}
	}

	/**
	 * Get all the assignments for a course
	 *
	 * @param id the course id
	 * @param pageable the pagination information
	 * @return the list of entities
	 */
	@Transactional(readOnly = true)
	public Page<AssignmentDTO> findByCourse(final Long id, final Pageable pageable) {
		log.debug("Request to get StudentCourses by Course : {}", id);
		Course course = courseRepository.findOne(id);
		if (course != null && (SecurityUtils.isAdmin() || inOrgOfCurrentUser(course))) {
			return assignmentRepository.findByCourse(course, pageable).map(assignmentTransformer::transform);
		}
		// TODO: Error handling / logging
		return null;
	}

	/**
	 * Get one assignment by id.
	 *
	 * @param id the id of the entity
	 * @return the entity
	 */
	@Transactional(readOnly = true)
	public AssignmentDTO findOne(final Long id) {
		log.debug("Request to get Assignment : {}", id);
		Assignment assignment = assignmentRepository.findOne(id);
		if (assignment != null && (SecurityUtils.isAdmin() || inOrgOfCurrentUser(assignment))) {
			return assignmentTransformer.transform(assignment);
		}
		// TODO: Error handling / logging
		return null;
	}

	/**
	 * Delete the assignment by id.
	 *
	 * @param id the id of the entity
	 */
	public void delete(final Long id) {
		log.debug("Request to delete Assignment : {}", id);
		Assignment assignment = assignmentRepository.findOne(id);
		User user = userService.getCurrentUser();

		if (assignment == null) throw new AssignmentNotFoundException(id);

		boolean instructorCheck = true;
		if (SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.INSTRUCTOR)) {
			Course course = assignment.getCourse();
			instructorCheck = user.getId() == course.getInstructor().getId();
		}

		if (instructorCheck && (SecurityUtils.isAdmin() || inOrgOfCurrentUser(assignment))) {
			studentAssignmentRepository.deleteByAssignment(assignment);
			fileInformationService.deleteByAssignment(assignment);
			assignmentRepository.delete(id);
		} else {
			throw new AccessDeniedException();
		}
	}

	public boolean inOrgOfCurrentUser(final AssignmentDTO assignmentDTO) {
		User user = userService.getCurrentUser();
		Course course = courseRepository.findOne(assignmentDTO.getCourseId());
		return course != null && user.getOrganization().equals(course.getSession().getProgram().getOrganization());
	}

	public boolean inOrgOfCurrentUser(final Assignment assignment) {
		User user = userService.getCurrentUser();
		return user.getOrganization().equals(assignment.getCourse().getSession().getProgram().getOrganization());
	}

	public boolean inOrgOfCurrentUser(final Course course) {
		User user = userService.getCurrentUser();
		return user.getOrganization().equals(course.getOrganization());
	}

	public boolean currentUserIsCourseInstructor(final AssignmentDTO assignmentDTO) {
		User user = userService.getCurrentUser();
		Course course = courseRepository.findOne(assignmentDTO.getCourseId());

		return user.equals(course.getInstructor());
	}

	public boolean currentUserIsEnrolledIn(final AssignmentDTO assignmentDTO) {
		User user = userService.getCurrentUser();
		Course course = courseRepository.findOne(assignmentDTO.getCourseId());
		StudentCourse studentCourse = studentCourseRepository.findByStudentAndCourse(user, course);

		return studentCourse != null;
	}
}
