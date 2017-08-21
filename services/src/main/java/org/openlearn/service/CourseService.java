package org.openlearn.service;

import org.openlearn.domain.Course;
import org.openlearn.domain.ItemLink;
import org.openlearn.domain.User;
import org.openlearn.repository.CourseRepository;
import org.openlearn.repository.ItemLinkRepository;
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
import java.util.Set;
import java.util.function.Predicate;

/**
 * Service Implementation for managing Course.
 */
@Service
@Transactional
public class CourseService {

	private final Logger log = LoggerFactory.getLogger(CourseService.class);

	private final CourseRepository courseRepository;

	private final ItemLinkRepository itemLinkRepository;

	private final UserRepository userRepository;

	public CourseService(final CourseRepository courseRepository, final ItemLinkRepository itemLinkRepository, UserRepository userRepository) {
		this.courseRepository = courseRepository;
		this.itemLinkRepository = itemLinkRepository;
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
		if(SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN)) {
			return courseRepository.save(course);
		}
		Optional<User> user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin());
		Course origCourse = findOne(course.getId());
		if(origCourse != null){
			return courseRepository.save(course);
		}
		return null;
	}

	/**
	 *  Get all the courses.
	 *
	 *  @param pageable the pagination information
	 *  @return the list of entities
	 */
	@Transactional(readOnly = true)
	public Page<Course> findAll(final Pageable pageable) {
		log.debug("Request to get all Courses");
		if(SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN)){
			final Page<Course> result = courseRepository.findAll(pageable);
			return result;
		}
		Optional<User> user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin());
		return courseRepository.findAllByOrganizationId(pageable, user.get().getOrganizationIds());
	}

	/**
	 * Get all courses that a Student is in
	 *
	 * @param pageable the pagination info
	 * @param studentId the id of the student
	 * @return list of courses
	 */
	@Transactional(readOnly = true)
	public Page<Course> findAllByStudentId(final Pageable pageable, final Long studentId){
		log.debug("Request to get Courses assigned to studentID: " + studentId);
		return courseRepository.findCoursesByStudent(pageable ,studentId);
	}

	/**
	 *  Get one course by id.
	 *
	 *  @param id the id of the entity
	 *  @return the entity
	 */
	@Transactional(readOnly = true)
	public Course findOne(final Long id) {
		log.debug("Request to get Course : {}", id);
		if(SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN)){
			return courseRepository.findOne(id);
		}
		Optional<User> user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin());
		return courseRepository.findOneByIdAndOrganizationId(id, user.get().organizationIds);
	}

	/**
	 *  Delete the  course by id.
	 *
	 *  @param id the id of the entity
	 */
	public void delete(final Long id) {
		log.debug("Request to delete Course : {}", id);
		if(SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN)) {
			courseRepository.delete(id);
		}
		Optional<User> user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin());
		Course origCourse = findOne(id);
		if(origCourse != null){
			courseRepository.delete(id);
		}
	}

	/**
	 * Get the item links associatd with a course
	 *
	 *  @param id the course id to find item links with
	 *  @return the set of item links associated with the course id
	 */
	@Transactional(readOnly =  true)
	public Set<ItemLink> getItemLinksForCourse(final Long id) {
		log.debug("Request to get item links for course id : {}", id);
		Course course = courseRepository.findOne(id);
		final Set<ItemLink> result = course.getResources();
		return result;
	}

	/**
	 * Add an item link to a course
	 *
	 *  @param courseId the course id associate the item link with
	 *  @param itemLinkId the id of the item link to associate with the course
	 *  @return the set of item links associated with the course id after adding the item link
	 */
	@Transactional
	public Set<ItemLink> addItemLinkToCourse(final Long courseId, final Long itemLinkId){
		log.debug("Request to add item link id {} to course id {}", itemLinkId, courseId);
		Course course = courseRepository.findOne(courseId);
		if(course == null){
			return null;
		}
		ItemLink itemLink = itemLinkRepository.findOne(itemLinkId);
		if(itemLink == null){
			return null;
		}
		course.getResources().add(itemLink);
		courseRepository.save(course);
		return course.getResources();
	}

	/**
	 * Remove an item link from a course
	 *
	 *  @param courseId the course id to remove the item link from
	 *  @param itemLinkId the id of the item link to remove
	 *  @return the set of item links associated with the course id after removing the item link
	 */
	@Transactional
	public Set<ItemLink> removeItemLinkFromCourse(Long courseId, Long itemLinkId) {
		log.debug("Request to remove item link id {} from course id {}", itemLinkId, courseId);
		Course course = courseRepository.findOne(courseId);
		if(course == null){
			return null;
		}
		Predicate<ItemLink> itemLinkPredicate = p-> p.getId() == itemLinkId;
		course.getResources().removeIf(itemLinkPredicate);
		courseRepository.save(course);
		return course.getResources();
	}
}
