package org.openlearn.web.rest;

import io.swagger.annotations.ApiParam;
import org.openlearn.dto.AssignmentDTO;
import org.openlearn.security.AuthoritiesConstants;
import org.openlearn.service.AssignmentService;
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
@RequestMapping("/api/assignments")
public class AssignmentResource {

	// TODO: Error handling / logging

	private static final String ENDPOINT = "/api/assignments/";

	private static final Logger log = LoggerFactory.getLogger(AssignmentResource.class);

	private final AssignmentService assignmentService;

	public AssignmentResource(final AssignmentService assignmentService) {
		this.assignmentService = assignmentService;
	}

	/**
	 * GET  /:id : get a single assignment by ID
	 *
	 * @param id the ID of the assignment to get
	 * @return the ResponseEntity with status 200 (OK) and the assignment in the body
	 *      or with ... TODO: Error handling
	 */
	@GetMapping(path = "/{id}")
	@Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.ORG_ADMIN, AuthoritiesConstants.INSTRUCTOR})
	public ResponseEntity get(@PathVariable final Long id) {
		log.debug("GET request to get assignment : {}", id);
		AssignmentDTO response = assignmentService.findOne(id);
		return ResponseEntity.ok(response);
	}

	/**
	 * GET  / : get a list of all assignment, filtered by organization
	 *
	 * @param pageable the pagination information
	 * @return the ResponseEntity with status 200 (OK) and a list of assignments in the body
	 *      or with ... TODO: Error handling
	 */
	@GetMapping
	@Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.ORG_ADMIN, AuthoritiesConstants.INSTRUCTOR})
	public ResponseEntity get(@ApiParam final Pageable pageable) {
		log.debug("GET request for all assignments");
		Page<AssignmentDTO> response = assignmentService.findAll(pageable);
		return ResponseEntity.ok(response.getContent());
	}

	/**
	 * GET  / : get a list of all assignments for a course
	 *
	 * @param pageable the pagination information
	 * @return the ResponseEntity with status 200 (OK) and a list of assignments in the body
	 *      or with ... TODO: Error handling
	 */
	@GetMapping(path = "/course/{id}")
	@Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.ORG_ADMIN, AuthoritiesConstants.INSTRUCTOR})
	public ResponseEntity getByCourse(@PathVariable final Long id, @ApiParam final Pageable pageable) {
		log.debug("GET request for all assignments by course: {}", id);
		Page<AssignmentDTO> response = assignmentService.findByCourse(id, pageable);
		return ResponseEntity.ok(response.getContent());
	}

	/**
	 * POST  / : create an assignment
	 *
	 * @param assignmentDTO the assignment to create
	 * @return the ResponseEntity with status 200 (OK) and the created assignment in the body
	 *      or with ... TODO: Error handling
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PostMapping
	@Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.ORG_ADMIN, AuthoritiesConstants.INSTRUCTOR})
	public ResponseEntity create(@RequestBody @Valid final AssignmentDTO assignmentDTO) throws URISyntaxException {
		log.debug("POST request to create assignment : {}", assignmentDTO);
		AssignmentDTO response = assignmentService.save(assignmentDTO);
		return ResponseEntity.created(new URI(ENDPOINT + response.getId())).body(response);
	}

	/**
	 * PUT  / : update an assignment
	 *
	 * @param assignmentDTO the assignment to update
	 * @return the ResponseEntity with status 200 (OK) and the updated assignment in the body
	 *      or with ... TODO: Error handling
	 */
	@PutMapping
	@Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.ORG_ADMIN, AuthoritiesConstants.INSTRUCTOR})
	public ResponseEntity update(@RequestBody @Valid final AssignmentDTO assignmentDTO) {
		log.debug("PUT request to update assignment : {}", assignmentDTO);
		AssignmentDTO response = assignmentService.save(assignmentDTO);
		return ResponseEntity.ok(response);
	}

	/**
	 * DELETE  / : delete an assignment
	 *
	 * @param id the ID of the assignment to delete
	 * @return the ResponseEntity with status 200 (OK)
	 *      or with ... TODO: Error handling
	 */
	@DeleteMapping(path = "/{id}")
	@Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.ORG_ADMIN, AuthoritiesConstants.INSTRUCTOR})
	public ResponseEntity delete(@PathVariable final Long id) {
		log.debug("DELETE request to delete assignment : {}", id);
		assignmentService.delete(id);
		return ResponseEntity.ok().build();
	}
}
