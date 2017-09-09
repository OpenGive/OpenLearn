package org.openlearn.service;

import org.openlearn.domain.StudentCourse;
import org.openlearn.repository.StudentCourseRepository;
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

	private final Logger log = LoggerFactory.getLogger(StudentCourseService.class);

	private final StudentCourseRepository studentCourseRepository;

	public StudentCourseService(StudentCourseRepository studentCourseRepository) {
		this.studentCourseRepository = studentCourseRepository;
	}

	/**
	 * Save a studentCourse.
	 *
	 * @param studentCourse the entity to save
	 * @return the persisted entity
	 */
	public StudentCourse save(StudentCourse studentCourse) {
		log.debug("Request to save StudentCourse : {}", studentCourse);
		return studentCourseRepository.save(studentCourse);
	}

	/**
	 * Get all the studentCourses.
	 *
	 * @param pageable the pagination information
	 * @return the list of entities
	 */
	@Transactional(readOnly = true)
	public Page<StudentCourse> findAll(Pageable pageable) {
		log.debug("Request to get all StudentCourses");
		return studentCourseRepository.findAll(pageable);
	}

	/**
	 * Get one studentCourse by id.
	 *
	 * @param id the id of the entity
	 * @return the entity
	 */
	@Transactional(readOnly = true)
	public StudentCourse findOne(Long id) {
		log.debug("Request to get StudentCourse : {}", id);
		return studentCourseRepository.findOne(id);
	}

	/**
	 * Delete the  studentCourse by id.
	 *
	 * @param id the id of the entity
	 */
	public void delete(Long id) {
		log.debug("Request to delete StudentCourse : {}", id);
		studentCourseRepository.delete(id);
	}
}
