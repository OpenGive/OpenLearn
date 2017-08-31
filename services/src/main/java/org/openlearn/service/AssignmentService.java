package org.openlearn.service;

import org.openlearn.repository.AssignmentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing Assignment.
 */
@Service
@Transactional
public class AssignmentService {

	private final Logger log = LoggerFactory.getLogger(AssignmentService.class);

	private final AssignmentRepository assignmentRepository;

	public AssignmentService(AssignmentRepository assignmentRepository) {
		this.assignmentRepository = assignmentRepository;
	}
}
