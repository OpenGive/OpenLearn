package org.openlearn.service;

import org.openlearn.domain.Course;
import org.openlearn.domain.Session;
import org.openlearn.domain.User;
import org.openlearn.dto.CourseDTO;
import org.openlearn.repository.AssignmentRepository;
import org.openlearn.repository.CourseRepository;
import org.openlearn.repository.SessionRepository;
import org.openlearn.repository.StudentCourseRepository;
import org.openlearn.security.AuthoritiesConstants;
import org.openlearn.security.SecurityUtils;
import org.openlearn.transformer.CourseTransformer;
import org.openlearn.web.rest.errors.ItemHasChildrenException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing Course.
 */
@Service
@Transactional
public class CourseService {

	private static final Logger log = LoggerFactory.getLogger(CourseService.class);

	private final CourseRepository courseRepository;

	private final CourseTransformer courseTransformer;

	private final SessionRepository sessionRepository;

	private final UserService userService;

	private final AssignmentRepository assignmentRepository;

	private final StudentCourseRepository studentCourseRepository;

	public CourseService(final CourseRepository courseRepository,
						 final CourseTransformer courseTransformer,
	                     final SessionRepository sessionRepository,
						 final UserService userService,
						 final AssignmentRepository assignmentRepository,
						 final StudentCourseRepository studentCourseRepository) {
		this.courseRepository = courseRepository;
		this.courseTransformer = courseTransformer;
		this.sessionRepository = sessionRepository;
		this.userService = userService;
		this.assignmentRepository = assignmentRepository;
		this.studentCourseRepository = studentCourseRepository;
	}

	/**
	 * Save a course.
	 *
	 * @param courseDTO the entity to save
	 * @return the persisted entity
	 */
	public CourseDTO save(final CourseDTO courseDTO) {
		log.debug("Request to save Course : {}", courseDTO);
		User user = userService.getCurrentUser();
		boolean instructorCheck = true;
		if (SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.INSTRUCTOR)) instructorCheck = user.getId() == courseDTO.getInstructorId();
		if ((SecurityUtils.isAdmin() || inOrgOfCurrentUser(courseDTO)) && instructorCheck) {
			return courseTransformer.transform(courseRepository.save(courseTransformer.transform(courseDTO)));
		}
		// TODO: Error handling / logging
		return null;
	}

	/**
	 * Get all the courses.
	 *
	 * @return the list of entities
	 */
	@Transactional(readOnly = true)
	public List<CourseDTO> findAll() {
		log.debug("Request to get all Courses");
		User user = userService.getCurrentUser();
		if (SecurityUtils.isAdmin()) {
			return courseRepository.findAll()
				.stream()
				.map(courseTransformer::transform)
				.collect(Collectors.toList());
		} else {
			return courseRepository.findByOrganization(user.getOrganization())
				.stream()
				.map(courseTransformer::transform)
				.collect(Collectors.toList());
		}
	}

	/**
	 * Get one course by id.
	 *
	 * @param id the id of the entity
	 * @return the entity
	 */
	@Transactional(readOnly = true)
	public CourseDTO findOne(final Long id) {
		log.debug("Request to get Course : {}", id);
		Course course = courseRepository.findOne(id);
		if (course != null && (SecurityUtils.isAdmin() || inOrgOfCurrentUser(course))) {
			return courseTransformer.transform(course);
		}
		// TODO: Error handling / logging
		return null;
	}

	/**
	 * Delete the course by id.
	 *
	 * @param id the id of the entity
	 */
	public void delete(final Long id) {
		log.debug("Request to delete Course : {}", id);
		User user = userService.getCurrentUser();
		Course course = courseRepository.findOne(id);
		boolean instructorCheck = true;
		if (SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.INSTRUCTOR)) instructorCheck = user.getId() == course.getInstructor().getId();
		if (course != null && (SecurityUtils.isAdmin() || inOrgOfCurrentUser(course)) && instructorCheck) {

			if (studentCourseRepository.countByCourse(course) > 0 || assignmentRepository.countByCourse(course) > 0) {
				throw new ItemHasChildrenException("Before you delete this course, please remove all students and assignments from the course.");
			}

			courseRepository.delete(id);
		} else {
			// TODO: Error handling / logging
		}
	}

	private boolean inOrgOfCurrentUser(final CourseDTO courseDTO) {
		User user = userService.getCurrentUser();
		Session session = sessionRepository.findOne(courseDTO.getSessionId());
		return session != null && user.getOrganization().equals(session.getProgram().getOrganization());
	}

	private boolean inOrgOfCurrentUser(final Course course) {
		User user = userService.getCurrentUser();
		return user.getOrganization().equals(course.getSession().getProgram().getOrganization());
	}
}
