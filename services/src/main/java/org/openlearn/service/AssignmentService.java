package org.openlearn.service;

import org.openlearn.domain.Assignment;
import org.openlearn.domain.Course;
import org.openlearn.domain.User;
import org.openlearn.dto.AssignmentDTO;
import org.openlearn.repository.AssignmentRepository;
import org.openlearn.repository.CourseRepository;
import org.openlearn.security.SecurityUtils;
import org.openlearn.transformer.AssignmentTransformer;
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

	private final UserService userService;

	public AssignmentService(final AssignmentRepository assignmentRepository,
	                         final AssignmentTransformer assignmentTransformer,
	                         final CourseRepository courseRepository, final UserService userService) {
		this.assignmentRepository = assignmentRepository;
		this.assignmentTransformer = assignmentTransformer;
		this.courseRepository = courseRepository;
		this.userService = userService;
	}

	/**
	 * Save an assignment.
	 *
	 * @param assignmentDTO the entity to save
	 * @return the persisted entity
	 */
	public AssignmentDTO save(final AssignmentDTO assignmentDTO) {
		log.debug("Request to save Assignment : {}", assignmentDTO);
		if (SecurityUtils.isAdmin() || inOrgOfCurrentUser(assignmentDTO)) {
			return assignmentTransformer.transform(assignmentRepository.save(assignmentTransformer.transform(assignmentDTO)));
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
			return assignmentRepository.findAllByOrganization(user.getOrganization(), pageable)
				.map(assignmentTransformer::transform);
		}
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
		if (assignment != null && (SecurityUtils.isAdmin() || inOrgOfCurrentUser(assignment))) {
			assignmentRepository.delete(id);
		} else {
			// TODO: Error handling / logging
		}
	}

	private boolean inOrgOfCurrentUser(final AssignmentDTO assignmentDTO) {
		User user = userService.getCurrentUser();
		Course course = courseRepository.findOne(assignmentDTO.getCourseId());
		return course != null && user.getOrganization().equals(course.getSession().getProgram().getOrganization());
	}

	private boolean inOrgOfCurrentUser(final Assignment assignment) {
		User user = userService.getCurrentUser();
		return user.getOrganization().equals(assignment.getCourse().getSession().getProgram().getOrganization());
	}
}
