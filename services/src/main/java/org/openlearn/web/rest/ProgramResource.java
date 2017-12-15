package org.openlearn.web.rest;

import org.openlearn.dto.ProgramDTO;
import org.openlearn.security.AuthoritiesConstants;
import org.openlearn.service.ProgramService;
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
@RequestMapping("/api/programs")
public class ProgramResource {

	// TODO: Error handling / logging

	private static final String ENDPOINT = "/api/programs/";

	private static final Logger log = LoggerFactory.getLogger(ProgramResource.class);

	private final ProgramService programService;

	public ProgramResource(final ProgramService programService) {
		this.programService = programService;
	}

	/**
	 * GET  /:id : get a single program by ID
	 *
	 * @param id the ID of the program to get
	 * @return the ResponseEntity with status 200 (OK) and the program in the body
	 *      or with ... TODO: Error handling
	 */
	@GetMapping(path = "/{id}")
	@Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.ORG_ADMIN, AuthoritiesConstants.INSTRUCTOR})
	public ResponseEntity get(@PathVariable final Long id) {
		log.debug("GET request to get program : {}", id);
		ProgramDTO response = programService.findOne(id);
		return ResponseEntity.ok(response);
	}

	/**
	 * GET  / : get a list of all program, filtered by organization
	 *
	 * @return the ResponseEntity with status 200 (OK) and a list of programs in the body
	 *      or with ... TODO: Error handling
	 */
	@GetMapping
	@Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.ORG_ADMIN, AuthoritiesConstants.INSTRUCTOR})
	public ResponseEntity get() {
		log.debug("GET request for all programs");
		List<ProgramDTO> response = programService.findAll();
		return ResponseEntity.ok(response);
	}

	/**
	 * POST  / : create a program
	 *
	 * @param programDTO the program to create
	 * @return the ResponseEntity with status 200 (OK) and the created program in the body
	 *      or with ... TODO: Error handling
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PostMapping
	@Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.ORG_ADMIN})
	public ResponseEntity create(@RequestBody @Valid final ProgramDTO programDTO) throws URISyntaxException {
		log.debug("POST request to create program : {}", programDTO);
		ProgramDTO response = programService.save(programDTO);
		return ResponseEntity.created(new URI(ENDPOINT + response.getId())).body(response);
	}

	/**
	 * PUT  / : update a program
	 *
	 * @param programDTO the program to update
	 * @return the ResponseEntity with status 200 (OK) and the updated program in the body
	 *      or with ... TODO: Error handling
	 */
	@PutMapping
	@Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.ORG_ADMIN})
	public ResponseEntity update(@RequestBody @Valid final ProgramDTO programDTO) {
		log.debug("PUT request to update program : {}", programDTO);
		ProgramDTO response = programService.save(programDTO);
		return ResponseEntity.ok(response);
	}

	/**
	 * DELETE  / : delete a program
	 *
	 * @param id the ID of the program to delete
	 * @return the ResponseEntity with status 200 (OK)
	 *      or with ... TODO: Error handling
	 */
	@DeleteMapping(path = "/{id}")
	@Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.ORG_ADMIN})
	public ResponseEntity delete(@PathVariable final Long id) {
		log.debug("DELETE request to delete program : {}", id);
		programService.delete(id);
		return ResponseEntity.ok().build();
	}
}
