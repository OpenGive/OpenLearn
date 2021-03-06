package org.openlearn.web.rest;

import org.openlearn.dto.InstructorDTO;
import org.openlearn.security.AuthoritiesConstants;
import org.openlearn.security.SecurityUtils;
import org.openlearn.service.InstructorService;
import org.openlearn.service.UserService;
import org.openlearn.web.rest.errors.AccessDeniedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/api/instructors")
public class InstructorResource {

	// TODO: Error handling / logging

	private static final String ENDPOINT = "/api/instructors/";

	private static final Logger log = LoggerFactory.getLogger(InstructorResource.class);

	private final InstructorService instructorService;

	private final UserService userService;

	public InstructorResource(final InstructorService instructorService,
							  final UserService userService) {
		this.instructorService = instructorService;
		this.userService = userService;
	}

	/**
	 * GET  /:id : get a single instructor user by ID
	 *
	 * @param id the ID of the instructor to get
	 * @return the ResponseEntity with status 200 (OK) and the instructor in the body
	 *      or with ... TODO: Error handling
	 */
	@GetMapping(path = "/{id}")
	@Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.ORG_ADMIN, AuthoritiesConstants.INSTRUCTOR,  AuthoritiesConstants.STUDENT})
	public ResponseEntity get(@PathVariable final Long id) {
		log.debug("GET request to get instructor : {}", id);
		InstructorDTO response = instructorService.findOne(id);
		return ResponseEntity.ok(response);
	}

	/**
	 * GET  / : get a list of all instructor users, filtered by organization
	 *
	 * @return the ResponseEntity with status 200 (OK) and a list of instructors in the body
	 *      or with ... TODO: Error handling
	 */
	@GetMapping
	@Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.ORG_ADMIN, AuthoritiesConstants.INSTRUCTOR})
	public ResponseEntity get() {
		log.debug("GET request for all instructors");
		List<InstructorDTO> response = instructorService.findAll();
		return ResponseEntity.ok(response);
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
	@Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.ORG_ADMIN})
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

		if (SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.INSTRUCTOR) &&
			!userService.getCurrentUser().getId().equals(instructorDTO.getId())) {

			log.error("Access denied for PUT request to update instructor: {}", instructorDTO.getId());
			throw new AccessDeniedException();
		}

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
	@DeleteMapping(path = "/{id}")
	@Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.ORG_ADMIN})
	public ResponseEntity delete(@PathVariable final Long id) {
		log.debug("DELETE request to delete instructor : {}", id);
		instructorService.delete(id);
		return ResponseEntity.ok().build();
	}
}
