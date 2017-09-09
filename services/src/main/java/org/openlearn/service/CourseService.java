package org.openlearn.service;

import org.openlearn.domain.Course;
import org.openlearn.domain.Session;
import org.openlearn.domain.User;
import org.openlearn.dto.CourseDTO;
import org.openlearn.repository.CourseRepository;
import org.openlearn.repository.SessionRepository;
import org.openlearn.security.SecurityUtils;
import org.openlearn.transformer.CourseTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing Course.
 */
@Service
@Transactional
public class CourseService {

	private final Logger log = LoggerFactory.getLogger(CourseService.class);

	private final CourseRepository courseRepository;

	private final CourseTransformer courseTransformer;

	private final SessionRepository sessionRepository;

	private final UserService userService;

	public CourseService(CourseRepository courseRepository, CourseTransformer courseTransformer,
	                     SessionRepository sessionRepository, UserService userService) {
		this.courseRepository = courseRepository;
		this.courseTransformer = courseTransformer;
		this.sessionRepository = sessionRepository;
		this.userService = userService;
	}

	/**
	 * Save a course.
	 *
	 * @param courseDTO the entity to save
	 * @return the persisted entity
	 */
	public CourseDTO save(CourseDTO courseDTO) {
		log.debug("Request to save Course : {}", courseDTO);
		if (SecurityUtils.isAdmin() || inOrgOfCurrentUser(courseDTO)) {
			return courseTransformer.transform(courseRepository.save(courseTransformer.transform(courseDTO)));
		}
		// TODO: Error handling / logging
		return null;
	}

	/**
	 * Get all the courses.
	 *
	 * @param pageable the pagination information
	 * @return the list of entities
	 */
	@Transactional(readOnly = true)
	public Page<CourseDTO> findAll(Pageable pageable) {
		log.debug("Request to get all Courses");
		User user = userService.getCurrentUser();
		if (SecurityUtils.isAdmin()) {
			return courseRepository.findAll(pageable).map(courseTransformer::transform);
		} else {
			return courseRepository.findAllByOrganization(user.getOrganization(), pageable)
				.map(courseTransformer::transform);
		}
	}

	/**
	 * Get one course by id.
	 *
	 * @param id the id of the entity
	 * @return the entity
	 */
	@Transactional(readOnly = true)
	public CourseDTO findOne(Long id) {
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
	public void delete(Long id) {
		log.debug("Request to delete Course : {}", id);
		Course course = courseRepository.findOne(id);
		if (course != null && (SecurityUtils.isAdmin() || inOrgOfCurrentUser(course))) {
			courseRepository.delete(id);
		} else {
			// TODO: Error handling / logging
		}
	}

	/**
	 * Determines if a course is in the organization of current user
	 *
	 * @param courseDTO the course
	 * @return true if course and current user are in the same org
	 */
	private boolean inOrgOfCurrentUser(CourseDTO courseDTO) {
		User user = userService.getCurrentUser();
		Session session = sessionRepository.findOneByIdAndOrganization(courseDTO.getSessionId(),
			user.getOrganization());
		return session != null && user.getOrganization().equals(session.getProgram().getOrganization());
	}

	/**
	 * Determines if a course is in the organization of current user
	 *
	 * @param course the course
	 * @return true if course and current user are in the same org
	 */
	private boolean inOrgOfCurrentUser(Course course) {
		User user = userService.getCurrentUser();
		return user.getOrganization().equals(course.getSession().getProgram().getOrganization());
	}
}
