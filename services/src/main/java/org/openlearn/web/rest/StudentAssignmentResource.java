package org.openlearn.web.rest;

import io.swagger.annotations.ApiParam;
import org.openlearn.dto.StudentAssignmentDTO;
import org.openlearn.security.AuthoritiesConstants;
import org.openlearn.service.StudentAssignmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

	private static final Logger log = LoggerFactory.getLogger(StudentAssignmentResource.class);

	private final StudentAssignmentService studentAssignmentService;

	public StudentAssignmentResource(final StudentAssignmentService studentAssignmentService) {
		this.studentAssignmentService = studentAssignmentService;
	}

	/**
	 * GET  /:id : get a single studentAssignment by ID
	 *
	 * @param id the ID of the studentAssignment to get
	 * @return the ResponseEntity with status 200 (OK) and the studentAssignment in the body
	 *      or with ... TODO: Error handling
	 */
	@GetMapping(path = "/{id}")
	@Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.ORG_ADMIN, AuthoritiesConstants.INSTRUCTOR})
	public ResponseEntity get(@PathVariable final Long id) {
		log.debug("GET request to get studentAssignment : {}", id);
		StudentAssignmentDTO response = studentAssignmentService.findOne(id);
		return ResponseEntity.ok(response);
	}

	/**
	 * GET  /student/:id : get a list of studentAssignment by student
	 *
	 * @param id the ID of the student
	 * @return the ResponseEntity with status 200 (OK) and a list of studentAssignment with assignment objects in the body
	 *      or with ... TODO: Error handling
	 */
	@GetMapping(path = "/student/{id}")
	@Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.ORG_ADMIN, AuthoritiesConstants.INSTRUCTOR})
	public ResponseEntity getByStudent(@PathVariable final Long id, @ApiParam final Pageable pageable) {
		log.debug("GET request to get studentAssignments by student : {}", id);
		Page<StudentAssignmentDTO> response = studentAssignmentService.findByStudent(id, pageable);
		return ResponseEntity.ok(response.getContent());
	}

	/**
	 * GET  /course/:id : get a list of studentAssignment by assignment
	 *
	 * @param id the ID of the assignment
	 * @return the ResponseEntity with status 200 (OK) and a list of studentAssignment with student objects in the body
	 *      or with ... TODO: Error handling
	 */
	@GetMapping(path = "/assignment/{id}")
	@Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.ORG_ADMIN, AuthoritiesConstants.INSTRUCTOR})
	public ResponseEntity getByAssignment(@PathVariable final Long id, @ApiParam final Pageable pageable) {
		log.debug("GET request to get studentAssignments by assignment : {}", id);
		Page<StudentAssignmentDTO> response = studentAssignmentService.findByAssignment(id, pageable);
		return ResponseEntity.ok(response.getContent());
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
	public ResponseEntity create(@RequestBody @Valid final StudentAssignmentDTO studentAssignmentDTO) throws URISyntaxException {
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
	public ResponseEntity update(@RequestBody @Valid final StudentAssignmentDTO studentAssignmentDTO) {
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
	@DeleteMapping(path = "/{id}")
	@Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.ORG_ADMIN})
	public ResponseEntity delete(@PathVariable final Long id) {
		log.debug("DELETE request to delete studentAssignment : {}", id);
		studentAssignmentService.delete(id);
		return ResponseEntity.ok().build();
	}
}
