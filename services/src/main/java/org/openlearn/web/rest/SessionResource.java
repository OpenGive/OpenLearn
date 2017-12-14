package org.openlearn.web.rest;

import org.openlearn.dto.SessionDTO;
import org.openlearn.security.AuthoritiesConstants;
import org.openlearn.service.SessionService;
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
@RequestMapping("/api/sessions")
public class SessionResource {

	// TODO: Error handling / logging

	private static final String ENDPOINT = "/api/sessions/";

	private static final Logger log = LoggerFactory.getLogger(SessionResource.class);

	private final SessionService sessionService;

	public SessionResource(final SessionService sessionService) {
		this.sessionService = sessionService;
	}

	/**
	 * GET  /:id : get a single session by ID
	 *
	 * @param id the ID of the session to get
	 * @return the ResponseEntity with status 200 (OK) and the session in the body
	 *      or with ... TODO: Error handling
	 */
	@GetMapping(path = "/{id}")
	@Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.ORG_ADMIN, AuthoritiesConstants.INSTRUCTOR,  AuthoritiesConstants.STUDENT})
	public ResponseEntity get(@PathVariable final Long id) {
		log.debug("GET request to get session : {}", id);
		SessionDTO response = sessionService.findOne(id);
		return ResponseEntity.ok(response);
	}

	/**
	 * GET  / : get a list of all session, filtered by organization
	 *
	 * @return the ResponseEntity with status 200 (OK) and a list of sessions in the body
	 *      or with ... TODO: Error handling
	 */
	@GetMapping
	@Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.ORG_ADMIN, AuthoritiesConstants.INSTRUCTOR})
	public ResponseEntity get() {
		log.debug("GET request for all sessions");
		List<SessionDTO> response = sessionService.findAll();
		return ResponseEntity.ok(response);
	}

	/**
	 * POST  / : create a session
	 *
	 * @param sessionDTO the session to create
	 * @return the ResponseEntity with status 200 (OK) and the created session in the body
	 *      or with ... TODO: Error handling
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PostMapping
	@Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.ORG_ADMIN})
	public ResponseEntity create(@RequestBody @Valid final SessionDTO sessionDTO) throws URISyntaxException {
		log.debug("POST request to create session : {}", sessionDTO);
		SessionDTO response = sessionService.save(sessionDTO);
		return ResponseEntity.created(new URI(ENDPOINT + response.getId())).body(response);
	}

	/**
	 * PUT  / : update a session
	 *
	 * @param sessionDTO the session to update
	 * @return the ResponseEntity with status 200 (OK) and the updated session in the body
	 *      or with ... TODO: Error handling
	 */
	@PutMapping
	@Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.ORG_ADMIN})
	public ResponseEntity update(@RequestBody @Valid final SessionDTO sessionDTO) {
		log.debug("PUT request to update session : {}", sessionDTO);
		SessionDTO response = sessionService.save(sessionDTO);
		return ResponseEntity.ok(response);
	}

	/**
	 * DELETE  / : delete a session
	 *
	 * @param id the ID of the session to delete
	 * @return the ResponseEntity with status 200 (OK)
	 *      or with ... TODO: Error handling
	 */
	@DeleteMapping(path = "/{id}")
	@Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.ORG_ADMIN})
	public ResponseEntity delete(@PathVariable final Long id) {
		log.debug("DELETE request to delete session : {}", id);
		sessionService.delete(id);
		return ResponseEntity.ok().build();
	}
}
