package org.openlearn.service;


import org.openlearn.domain.Course;
import org.openlearn.domain.CourseStudent;
import org.openlearn.domain.StudentCourse;
import org.openlearn.domain.User;
import org.openlearn.service.dto.CourseStudentDTO;
import org.openlearn.repository.CourseRepository;
import org.openlearn.repository.CourseStudentRepository;
import org.openlearn.repository.StudentCourseRepostory;
import org.openlearn.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing StudentCourse.
 */
@Service
@Transactional
public class StudentCourseService {
	private final Logger log = LoggerFactory.getLogger(StudentCourseService.class);

	private final UserRepository userRepository;

	private final CourseRepository courseRepository;

	private final CourseStudentRepository courseStudentRepository;

	private final StudentCourseRepostory studentCourseRepostory;

	public StudentCourseService(final CourseStudentRepository courseStudentRepository, final UserRepository userRepository, final CourseRepository courseRepository, final StudentCourseRepostory studentCourseRepostory) {
		this.courseStudentRepository = courseStudentRepository;
		this.studentCourseRepostory = studentCourseRepostory;
		this.userRepository = userRepository;
		this.courseRepository = courseRepository;
	}

	@Transactional(readOnly = true)
	public Page<CourseStudentDTO> findByCourseId(final Long courseId, final Pageable pageable) {
		log.debug("Request to get students for course id : {}", courseId);
		final Page<CourseStudentDTO> courseStudents = courseStudentRepository.findAllByCourseId(courseId, pageable).map(CourseStudentDTO::new);
		return courseStudents;
	}

	@Transactional(readOnly = true)
	public List<User> findByCourseIdNot(final Long courseId) {
		log.debug("Request to get students not in course id : {}", courseId);
		return userRepository.findAllByCourseIdNot(courseId);
	}

	@Transactional(readOnly = true)
	public Page<StudentCourse> findByLogin(final String login, final Pageable pageable) {
		log.debug("Request to get courses for user with login : {}", login);
		Optional<User> user = userRepository.findOneWithAuthoritiesByLogin(login);
		final Page<StudentCourse> studentCourses = studentCourseRepostory.findAllByUserId(user.get().getId(), pageable);
		return studentCourses;
	}

	@Transactional
	public CourseStudent addStudentToCourse(final Long courseId, final Long studentId) {
		log.debug("Request to create StudentCourse with student id of {} and course id of {}", studentId, courseId);
		Course course = courseRepository.findOne(courseId);
		log.debug("Found course : {}", course);
		User student = userRepository.findOne(studentId);
		log.debug("Found \"student\"(user) : {}", student);
		CourseStudent studentCourse = new CourseStudent(course, student);
		log.debug("Creating StudentCourse with courseId : {}, grade : null, studentId : {}", studentCourse.getCourseId(), studentCourse.getUserId());
		courseStudentRepository.save(studentCourse);
		return studentCourse;
	}

	@Transactional
	public CourseStudent removeStudentFromCourse(final Long courseId, final Long studentId) {
		log.debug("Request to remove StudentCourse with student id of {} and course id of {}", studentId, courseId);
		CourseStudent studentCourse = courseStudentRepository.findOneByCourseIdAndUserId(courseId, studentId);
		courseStudentRepository.delete(studentCourse);
		return studentCourse;
	}

	@Transactional
	public CourseStudent addGradeToStudentCourse(final Long courseId, final Long studentId, final String grade) {
		log.debug("Request to set StudentCourse with student id of {} and course id of {} to grade of {}", studentId, courseId, grade);
		CourseStudent studentCourse = courseStudentRepository.findOneByCourseIdAndUserId(courseId, studentId);
		studentCourse.setGrade(grade);
		courseStudentRepository.save(studentCourse);
		return studentCourse;
	}

}
