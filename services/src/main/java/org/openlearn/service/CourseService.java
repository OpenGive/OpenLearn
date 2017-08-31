package org.openlearn.service;

import org.openlearn.domain.Course;
import org.openlearn.domain.User;
import org.openlearn.repository.CourseRepository;
import org.openlearn.repository.UserRepository;
import org.openlearn.security.AuthoritiesConstants;
import org.openlearn.security.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing Course.
 */
@Service
@Transactional
public class CourseService {

	private final Logger log = LoggerFactory.getLogger(CourseService.class);

	private final CourseRepository courseRepository;

	private final UserRepository userRepository;

	public CourseService(final CourseRepository courseRepository, UserRepository userRepository) {
		this.courseRepository = courseRepository;
		this.userRepository = userRepository;
	}

	/**
	 * Save a course.
	 *
	 * @param course the entity to save
	 * @return the persisted entity
	 */
	public Course save(final Course course) {
		log.debug("Request to save Course : {}", course);
		if (SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN)) {
			return courseRepository.save(course);
		}
		Optional<User> user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin());
		Course origCourse = findOne(course.getId());
		if (origCourse != null) {
			return courseRepository.save(course);
		}
		return null;
	}

	/**
	 * Get all the courses.
	 *
	 * @param pageable the pagination information
	 * @return the list of entities
	 */
	@Transactional(readOnly = true)
	public Page<Course> findAll(final Pageable pageable) {
		log.debug("Request to get all Courses");
		if (SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN)) {
			return courseRepository.findAll(pageable);
		}
		Optional<User> user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin());
		return courseRepository.findAllByOrganizationId(pageable, user.get().getOrganization().getId());
	}

	/**
	 * Get all courses that a Student is in
	 *
	 * @param pageable  the pagination info
	 * @param studentId the id of the student
	 * @return list of courses
	 */
	@Transactional(readOnly = true)
	public Page<Course> findAllByStudentId(final Pageable pageable, final Long studentId) {
		log.debug("Request to get Courses assigned to studentID: " + studentId);
		return courseRepository.findCoursesByStudent(pageable, studentId);
	}

	/**
	 * Get one course by id.
	 *
	 * @param id the id of the entity
	 * @return the entity
	 */
	@Transactional(readOnly = true)
	public Course findOne(final Long id) {
		log.debug("Request to get Course : {}", id);
		if (SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN)) {
			return courseRepository.findOne(id);
		}
		Optional<User> user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin());
		return courseRepository.findOneByIdAndOrganizationId(id, user.get().getOrganization().getId());
	}

	/**
	 * Delete the course by id.
	 *
	 * @param id the id of the entity
	 */
	public void delete(final Long id) {
		log.debug("Request to delete Course : {}", id);
		if (SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN)) {
			courseRepository.delete(id);
		}
		Optional<User> user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin());
		Course origCourse = findOne(id);
		if (origCourse != null) {
			courseRepository.delete(id);
		}
	}
}
