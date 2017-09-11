package org.openlearn.web.rest;

import io.swagger.annotations.ApiParam;
import org.openlearn.dto.InstructorDTO;
import org.openlearn.security.AuthoritiesConstants;
import org.openlearn.service.InstructorService;
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
@RequestMapping("/api/instructors")
public class InstructorResource {

	// TODO: Error handling / logging

	private static final String ENDPOINT = "/api/instructors/";

	private static final Logger log = LoggerFactory.getLogger(InstructorResource.class);

	private final InstructorService instructorService;

	public InstructorResource(final InstructorService instructorService) {
		this.instructorService = instructorService;
	}

	/**
	 * GET  /:id : get a single instructor user by ID
	 *
	 * @param id the ID of the instructor to get
	 * @return the ResponseEntity with status 200 (OK) and the instructor in the body
	 *      or with ... TODO: Error handling
	 */
	@GetMapping("/{id}")
	@Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.ORG_ADMIN, AuthoritiesConstants.INSTRUCTOR})
	public ResponseEntity get(@PathVariable final Long id) {
		log.debug("GET request to get instructor : {}", id);
		InstructorDTO response = instructorService.findOne(id);
		return ResponseEntity.ok(response);
	}

	/**
	 * GET  / : get a list of all instructor users, filtered by organization
	 *
	 * @param pageable the pagination information
	 * @return the ResponseEntity with status 200 (OK) and a list of instructors in the body
	 *      or with ... TODO: Error handling
	 */
	@GetMapping
	@Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.ORG_ADMIN, AuthoritiesConstants.INSTRUCTOR})
	public ResponseEntity get(@ApiParam final Pageable pageable) {
		log.debug("GET request for all instructors");
		Page<InstructorDTO> response = instructorService.findAll(pageable);
		return ResponseEntity.ok(response.getContent());
	}

	/**
	 * POST  / : create an instructor user
	 *
	 * @param instructorDTO the instructor to create
	 * @return the ResponseEntity with status 200 (OK) and the created instructor in the body
	 *      or with ... TODO: Error handling
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PostMapping
	@Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.ORG_ADMIN, AuthoritiesConstants.INSTRUCTOR})
	public ResponseEntity create(@RequestBody @Valid final InstructorDTO instructorDTO) throws URISyntaxException {
		log.debug("POST request to create instructor : {}", instructorDTO);
		InstructorDTO response = instructorService.save(instructorDTO);
		return ResponseEntity.created(new URI(ENDPOINT + response.getId())).body(response);
	}

	/**
	 * PUT  / : update an instructor user
	 *
	 * @param instructorDTO the instructor to update
	 * @return the ResponseEntity with status 200 (OK) and the updated instructor in the body
	 *      or with ... TODO: Error handling
	 */
	@PutMapping
	@Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.ORG_ADMIN, AuthoritiesConstants.INSTRUCTOR})
	public ResponseEntity update(@RequestBody @Valid final InstructorDTO instructorDTO) {
		log.debug("PUT request to update instructor : {}", instructorDTO);
		InstructorDTO response = instructorService.save(instructorDTO);
		return ResponseEntity.ok(response);
	}

	/**
	 * DELETE  / : delete an instructor user
	 *
	 * @param id the ID of the instructor to delete
	 * @return the ResponseEntity with status 200 (OK)
	 *      or with ... TODO: Error handling
	 */
	@DeleteMapping("/{id}")
	@Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.ORG_ADMIN, AuthoritiesConstants.INSTRUCTOR})
	public ResponseEntity delete(@PathVariable final Long id) {
		log.debug("DELETE request to delete instructor : {}", id);
		instructorService.delete(id);
		return ResponseEntity.ok().build();
	}
}
