package org.openlearn.web.rest.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.NOT_FOUND, reason="No such Assignment")  // 404
public class AssignmentNotFoundException extends RuntimeException {
	public AssignmentNotFoundException(Long assignmentId) {
		super("Assignment with id = '" + assignmentId.toString() + "' not found.");
	}
}
