package org.openlearn.web.rest;

import org.openlearn.domain.StudentAssignment;
import org.openlearn.dto.StudentAssignmentDTO;
import org.openlearn.security.AuthoritiesConstants;
import org.openlearn.service.StudentAssignmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequestMapping("/api/student-assignments")
public class StudentAssignmentResource {

	// TODO: Error handling / logging

	private static final String ENDPOINT = "/api/student-assignments/";

	private final Logger log = LoggerFactory.getLogger(StudentAssignmentResource.class);

	private final StudentAssignmentService studentAssignmentService;

	public StudentAssignmentResource(StudentAssignmentService studentAssignmentService) {
		this.studentAssignmentService = studentAssignmentService;
	}

	/**
	 * GET  /:id : get a single studentAssignment by ID
	 *
	 * @param id the ID of the studentAssignment to get
	 * @return the ResponseEntity with status 200 (OK) and the studentAssignment in the body
	 *      or with ... TODO: Error handling
	 */
	@GetMapping("/{id}")
	@Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.ORG_ADMIN, AuthoritiesConstants.INSTRUCTOR})
	public ResponseEntity get(@PathVariable Long id) {
		log.debug("GET request to get studentAssignment : {}", id);
		StudentAssignmentDTO response = studentAssignmentService.findOne(id);
		return ResponseEntity.ok(response);
	}

	/**
	 * POST  / : create a studentAssignment
	 *
	 * @param studentAssignmentDTO the studentAssignment to create
	 * @return the ResponseEntity with status 200 (OK) and the created studentAssignment in the body
	 *      or with ... TODO: Error handling
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PostMapping
	@Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.ORG_ADMIN})
	public ResponseEntity create(@RequestBody @Valid StudentAssignmentDTO studentAssignmentDTO) throws URISyntaxException {
		log.debug("POST request to create studentAssignment : {}", studentAssignmentDTO);
		StudentAssignmentDTO response = studentAssignmentService.save(studentAssignmentDTO);
		return ResponseEntity.created(new URI(ENDPOINT + response.getId())).body(response);
	}

	/**
	 * PUT  / : update a studentAssignment
	 *
	 * @param studentAssignmentDTO the studentAssignment to update
	 * @return the ResponseEntity with status 200 (OK) and the updated studentAssignment in the body
	 *      or with ... TODO: Error handling
	 */
	@PutMapping
	@Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.ORG_ADMIN})
	public ResponseEntity update(@RequestBody @Valid StudentAssignmentDTO studentAssignmentDTO) {
		log.debug("PUT request to update studentAssignment : {}", studentAssignmentDTO);
		StudentAssignmentDTO response = studentAssignmentService.save(studentAssignmentDTO);
		return ResponseEntity.ok(response);
	}

	/**
	 * DELETE  / : delete a studentAssignment
	 *
	 * @param id the ID of the studentAssignment to delete
	 * @return the ResponseEntity with status 200 (OK)
	 *      or with ... TODO: Error handling
	 */
	@DeleteMapping("/{id}")
	@Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.ORG_ADMIN})
	public ResponseEntity delete(@PathVariable Long id) {
		log.debug("DELETE request to delete studentAssignment : {}", id);
		studentAssignmentService.delete(id);
		return ResponseEntity.ok().build();
	}
}
