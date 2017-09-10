package org.openlearn.service;

import org.openlearn.domain.StudentAssignment;
import org.openlearn.repository.StudentAssignmentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing StudentAssignment.
 */
@Service
@Transactional
public class StudentAssignmentService {

	private final Logger log = LoggerFactory.getLogger(StudentAssignmentService.class);

	private final StudentAssignmentRepository studentAssignmentRepository;

	public StudentAssignmentService(StudentAssignmentRepository studentAssignmentRepository) {
		this.studentAssignmentRepository = studentAssignmentRepository;
	}

	/**
	 * Save a studentAssignment.
	 *
	 * @param studentAssignment the entity to save
	 * @return the persisted entity
	 */
	public StudentAssignment save(StudentAssignment studentAssignment) {
		log.debug("Request to save StudentAssignment : {}", studentAssignment);
		return studentAssignmentRepository.save(studentAssignment);
	}

	/**
	 * Get all the studentAssignments.
	 *
	 * @param pageable the pagination information
	 * @return the list of entities
	 */
	@Transactional(readOnly = true)
	public Page<StudentAssignment> findAll(Pageable pageable) {
		log.debug("Request to get all StudentAssignments");
		return studentAssignmentRepository.findAll(pageable);
	}

	/**
	 * Get one studentAssignment by id.
	 *
	 * @param id the id of the entity
	 * @return the entity
	 */
	@Transactional(readOnly = true)
	public StudentAssignment findOne(Long id) {
		log.debug("Request to get StudentAssignment : {}", id);
		return studentAssignmentRepository.findOne(id);
	}

	/**
	 * Delete the  studentAssignment by id.
	 *
	 * @param id the id of the entity
	 */
	public void delete(Long id) {
		log.debug("Request to delete StudentAssignment : {}", id);
		studentAssignmentRepository.delete(id);
	}
}
