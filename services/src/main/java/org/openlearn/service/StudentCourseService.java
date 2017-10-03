package org.openlearn.service;

import org.openlearn.domain.*;
import org.openlearn.dto.StudentCourseDTO;
import org.openlearn.repository.*;
import org.openlearn.security.AuthoritiesConstants;
import org.openlearn.security.SecurityUtils;
import org.openlearn.transformer.StudentCourseTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.PageRequest;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing StudentCourse.
 */
@Service
@Transactional
public class StudentCourseService {

	private static final Authority STUDENT = new Authority(AuthoritiesConstants.STUDENT);

	private static final Logger log = LoggerFactory.getLogger(StudentCourseService.class);

	private final StudentAssignmentRepository studentAssignmentRepository;

	private final AssignmentRepository assignmentRepository;

	private final CourseRepository courseRepository;

	private final StudentCourseRepository studentCourseRepository;

	private final StudentCourseTransformer studentCourseTransformer;

	private final UserRepository userRepository;

	private final UserService userService;

	public StudentCourseService(final CourseRepository courseRepository,
	                            final AssignmentRepository assignmentRepository,
								final StudentAssignmentRepository studentAssignmentRepository,
								final StudentCourseRepository studentCourseRepository,
	                            final StudentCourseTransformer studentCourseTransformer,
	                            final UserRepository userRepository,
								final UserService userService) {
		this.assignmentRepository = assignmentRepository;
		this.studentAssignmentRepository = studentAssignmentRepository;
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
	public StudentCourseDTO create(final StudentCourseDTO studentCourseDTO) {
		log.debug("Request to save StudentCourse : {}", studentCourseDTO);
		if (SecurityUtils.isAdmin() || inOrgOfCurrentUser(studentCourseDTO)) {
			StudentCourse studentCourse = studentCourseTransformer.transform(studentCourseDTO);
			studentCourse.setGrade("-");
			studentCourse.setEnrollDate(ZonedDateTime.now());
			studentCourse.setComplete(false);
			studentCourse.setOnPortfolio(false);

			studentCourse = studentCourseRepository.save(studentCourse);

			for (Assignment assignment : assignmentRepository.findByCourse(studentCourse.getCourse(), new PageRequest(0,100))) {
				StudentAssignment studentAssignment = new StudentAssignment();
				studentAssignment.setAssignment(assignment);
				studentAssignment.setComplete(false);
				studentAssignment.setOnPortfolio(false);
				studentAssignment.setStudent(studentCourse.getStudent());
				studentAssignmentRepository.save(studentAssignment);
			}

			return studentCourseTransformer.transform(studentCourse);
		}
		// TODO: Error handling / logging
		return null;
	}

	public StudentCourseDTO update(final StudentCourseDTO studentCourseDTO) {
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
	 * @return the list of student courses
	 */
	@Transactional(readOnly = true)
	public List<StudentCourseDTO> findByStudent(final Long id) {
		log.debug("Request to get StudentCourses by Student : {}", id);
		User student = userRepository.findOneByIdAndAuthority(id, STUDENT);
		if (student != null && (SecurityUtils.isAdmin() || inOrgOfCurrentUser(student))) {
			return studentCourseRepository.findByStudent(student).stream()
				.map((StudentCourse studentCourse) ->
					studentCourseTransformer.transform(studentCourse, false, true))
				.collect(Collectors.toList());
		}
		// TODO: Error handling / logging
		return null;
	}

	/**
	 * Get a list studentCourse by student that are on a student's portfolio.
	 *
	 * @param id the id of the student
	 * @return the list of student courses
	 */
	@Transactional(readOnly = true)
	public List<StudentCourse> findFlaggedByStudent(final Long id) {
		log.debug("Request to get portfolio StudentCourses by Student : {}", id);
		User student = userRepository.findOneByIdAndAuthority(id, STUDENT);
		if (student != null && (SecurityUtils.isAdmin() || inOrgOfCurrentUser(student))) {
			return studentCourseRepository.findByStudentAndOnPortfolio(student, true);
		}
		// TODO: Error handling / logging
		return null;
	}

	/**
	 * Get a list studentCourse by course.
	 *
	 * @param id the id of the course
	 * @return the list of student courses
	 */
	@Transactional(readOnly = true)
	public List<StudentCourseDTO> findByCourse(final Long id) {
		log.debug("Request to get StudentCourses by Course : {}", id);
		Course course = courseRepository.findOne(id);
		if (course != null && (SecurityUtils.isAdmin() || inOrgOfCurrentUser(course))) {
			return studentCourseRepository.findByCourse(course).stream()
				.map((StudentCourse studentCourse) ->
					studentCourseTransformer.transform(studentCourse, true, false))
				.collect(Collectors.toList());
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
