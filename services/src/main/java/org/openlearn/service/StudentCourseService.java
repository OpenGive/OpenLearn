package org.openlearn.service;

import org.openlearn.domain.Authority;
import org.openlearn.domain.Course;
import org.openlearn.domain.StudentCourse;
import org.openlearn.domain.User;
import org.openlearn.dto.StudentCourseDTO;
import org.openlearn.repository.CourseRepository;
import org.openlearn.repository.StudentCourseRepository;
import org.openlearn.repository.UserRepository;
import org.openlearn.security.AuthoritiesConstants;
import org.openlearn.security.SecurityUtils;
import org.openlearn.transformer.StudentCourseTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing StudentCourse.
 */
@Service
@Transactional
public class StudentCourseService {

	private static final Authority STUDENT = new Authority(AuthoritiesConstants.STUDENT);

	private static final Logger log = LoggerFactory.getLogger(StudentCourseService.class);

	private final CourseRepository courseRepository;

	private final StudentCourseRepository studentCourseRepository;

	private final StudentCourseTransformer studentCourseTransformer;

	private final UserRepository userRepository;

	private final UserService userService;

	public StudentCourseService(final CourseRepository courseRepository,
	                            final StudentCourseRepository studentCourseRepository,
	                            final StudentCourseTransformer studentCourseTransformer,
	                            final UserRepository userRepository, final UserService userService) {
		this.courseRepository = courseRepository;
		this.studentCourseRepository = studentCourseRepository;
		this.studentCourseTransformer = studentCourseTransformer;
		this.userRepository = userRepository;
		this.userService = userService;
	}

	/**
	 * Save a studentCourse.
	 *
	 * @param studentCourseDTO the entity to save
	 * @return the persisted entity
	 */
	public StudentCourseDTO save(final StudentCourseDTO studentCourseDTO) {
		log.debug("Request to save StudentCourse : {}", studentCourseDTO);
		if (SecurityUtils.isAdmin() || inOrgOfCurrentUser(studentCourseDTO)) {
			return studentCourseTransformer.transform(studentCourseRepository.save(studentCourseTransformer.transform(studentCourseDTO)));
		}
		// TODO: Error handling / logging
		return null;
	}

	/**
	 * Get one studentCourse by id.
	 *
	 * @param id the id of the entity
	 * @return the entity
	 */
	@Transactional(readOnly = true)
	public StudentCourseDTO findOne(final Long id) {
		log.debug("Request to get StudentCourse : {}", id);
		StudentCourse studentCourse = studentCourseRepository.findOne(id);
		if (studentCourse != null && (SecurityUtils.isAdmin() || inOrgOfCurrentUser(studentCourse))) {
			return studentCourseTransformer.transform(studentCourse);
		}
		// TODO: Error handling / logging
		return null;
	}

	/**
	 * Get a list studentCourse by student.
	 *
	 * @param id the id of the student
	 * @param pageable the pagination information
	 * @return the list of student courses
	 */
	@Transactional(readOnly = true)
	public Page<StudentCourseDTO> findByStudent(final Long id, final Pageable pageable) {
		log.debug("Request to get StudentCourses by Student : {}", id);
		User student = userRepository.findOneByIdAndAuthority(id, STUDENT);
		if (student != null && (SecurityUtils.isAdmin() || inOrgOfCurrentUser(student))) {
			return studentCourseRepository.findByStudent(student, pageable)
				.map((StudentCourse studentCourse) -> studentCourseTransformer.transform(studentCourse, false, true));
		}
		// TODO: Error handling / logging
		return null;
	}

	/**
	 * Get a list studentCourse by course.
	 *
	 * @param id the id of the course
	 * @param pageable the pagination information
	 * @return the list of student courses
	 */
	@Transactional(readOnly = true)
	public Page<StudentCourseDTO> findByCourse(final Long id, final Pageable pageable) {
		log.debug("Request to get StudentCourses by Course : {}", id);
		Course course = courseRepository.findOne(id);
		if (course != null && (SecurityUtils.isAdmin() || inOrgOfCurrentUser(course))) {
			return studentCourseRepository.findByCourse(course, pageable)
				.map((StudentCourse studentCourse) -> studentCourseTransformer.transform(studentCourse, true, false));
		}
		// TODO: Error handling / logging
		return null;
	}

	/**
	 * Delete the studentCourse by id.
	 *
	 * @param id the id of the entity
	 */
	public void delete(final Long id) {
		log.debug("Request to delete StudentCourse : {}", id);
		StudentCourse studentCourse = studentCourseRepository.findOne(id);
		if (studentCourse != null && (SecurityUtils.isAdmin() || inOrgOfCurrentUser(studentCourse))) {
			studentCourseRepository.delete(id);
		} else {
			// TODO: Error handling / logging
		}
	}

	private boolean inOrgOfCurrentUser(final StudentCourseDTO studentCourseDTO) {
		User user = userService.getCurrentUser();
		User student = userRepository.findOneByIdAndAuthority(studentCourseDTO.getStudentId(), STUDENT);
		Course course = courseRepository.findOne(studentCourseDTO.getCourseId());
		return user.getOrganization().getId().equals(student.getOrganization().getId())
			&& user.getOrganization().getId().equals(course.getOrganization().getId());
	}

	private boolean inOrgOfCurrentUser(final StudentCourse studentCourse) {
		User user = userService.getCurrentUser();
		return user.getOrganization().equals(studentCourse.getStudent().getOrganization())
			&& user.getOrganization().equals(studentCourse.getCourse().getOrganization());
	}

	private boolean inOrgOfCurrentUser(final User student) {
		User user = userService.getCurrentUser();
		return user.getOrganization().equals(student.getOrganization());
	}

	private boolean inOrgOfCurrentUser(final Course course) {
		User user = userService.getCurrentUser();
		return user.getOrganization().equals(course.getOrganization());
	}
}
