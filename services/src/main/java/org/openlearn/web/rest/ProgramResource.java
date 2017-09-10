package org.openlearn.web.rest;

import io.swagger.annotations.ApiParam;
import org.openlearn.dto.ProgramDTO;
import org.openlearn.security.AuthoritiesConstants;
import org.openlearn.service.ProgramService;
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
@RequestMapping("/api/programs")
public class ProgramResource {

	// TODO: Error handling / logging

	private static final String ENDPOINT = "/api/programs/";

	private final Logger log = LoggerFactory.getLogger(ProgramResource.class);

	private final ProgramService programService;

	public ProgramResource(ProgramService programService) {
		this.programService = programService;
	}

	/**
	 * GET  /:id : get a single program by ID
	 *
	 * @param id the ID of the program to get
	 * @return the ResponseEntity with status 200 (OK) and the program in the body
	 *      or with ... TODO: Error handling
	 */
	@GetMapping("/{id}")
	@Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.ORG_ADMIN, AuthoritiesConstants.INSTRUCTOR})
	public ResponseEntity get(@PathVariable Long id) {
		log.debug("GET request to get program : {}", id);
		ProgramDTO response = programService.findOne(id);
		return ResponseEntity.ok(response);
	}

	/**
	 * GET  / : get a list of all program, filtered by organization
	 *
	 * @param pageable the pagination information
	 * @return the ResponseEntity with status 200 (OK) and a list of programs in the body
	 *      or with ... TODO: Error handling
	 */
	@GetMapping
	@Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.ORG_ADMIN, AuthoritiesConstants.INSTRUCTOR})
	public ResponseEntity get(@ApiParam Pageable pageable) {
		log.debug("GET request for all programs");
		Page<ProgramDTO> response = programService.findAll(pageable);
		return ResponseEntity.ok(response.getContent());
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
	public ResponseEntity create(@RequestBody @Valid ProgramDTO programDTO) throws URISyntaxException {
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
	public ResponseEntity update(@RequestBody @Valid ProgramDTO programDTO) {
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
	@DeleteMapping("/{id}")
	@Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.ORG_ADMIN})
	public ResponseEntity delete(@PathVariable Long id) {
		log.debug("DELETE request to delete program : {}", id);
		programService.delete(id);
		return ResponseEntity.ok().build();
	}
}
