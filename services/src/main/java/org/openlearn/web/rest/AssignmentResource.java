package org.openlearn.web.rest;

import org.openlearn.service.AssignmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing Assignment.
 */
@RestController
@RequestMapping("/api/assignment")
public class AssignmentResource {

	private final Logger log = LoggerFactory.getLogger(AssignmentResource.class);

	private static final String ENTITY_NAME = "assignment";

	private final AssignmentService assignmentService;

	public AssignmentResource(AssignmentService assignmentService) {
		this.assignmentService = assignmentService;
	}
}
