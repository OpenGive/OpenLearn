package org.openlearn.web.rest;

import io.swagger.annotations.ApiParam;
import org.openlearn.dto.OrganizationDTO;
import org.openlearn.security.AuthoritiesConstants;
import org.openlearn.service.OrganizationService;
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
@RequestMapping("/api/organizations")
public class OrganizationResource {

	// TODO: Error handling / logging

	private static final String ENDPOINT = "/api/organizations/";

	private static final Logger log = LoggerFactory.getLogger(OrganizationResource.class);

	private final OrganizationService organizationService;

	public OrganizationResource(final OrganizationService organizationService) {
		this.organizationService = organizationService;
	}

	/**
	 * GET  /:id : get a single organization by ID
	 *
	 * @param id the ID of the organization to get
	 * @return the ResponseEntity with status 200 (OK) and the organization in the body
	 *      or with ... TODO: Error handling
	 */
	@GetMapping(path = "/{id}")
	@Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.ORG_ADMIN})
	public ResponseEntity get(@PathVariable final Long id) {
		log.debug("GET request to get organization : {}", id);
		OrganizationDTO response = organizationService.findOne(id);
		return ResponseEntity.ok(response);
	}

	/**
	 * GET  / : get a list of all organization, filtered by organization
	 *
	 * @param pageable the pagination information
	 * @return the ResponseEntity with status 200 (OK) and a list of organizations in the body
	 *      or with ... TODO: Error handling
	 */
	@GetMapping
	@Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.ORG_ADMIN, AuthoritiesConstants.INSTRUCTOR})
	public ResponseEntity get(@ApiParam final Pageable pageable) {
		log.debug("GET request for all organizations");
		Page<OrganizationDTO> response = organizationService.findAll(pageable);
		return ResponseEntity.ok(response.getContent());
	}

	/**
	 * POST  / : create an organization
	 *
	 * @param organizationDTO the organization to create
	 * @return the ResponseEntity with status 200 (OK) and the created organization in the body
	 *      or with ... TODO: Error handling
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PostMapping
	@Secured({AuthoritiesConstants.ADMIN})
	public ResponseEntity create(@RequestBody @Valid final OrganizationDTO organizationDTO) throws URISyntaxException {
		log.debug("POST request to create organization : {}", organizationDTO);
		OrganizationDTO response = organizationService.save(organizationDTO);
		return ResponseEntity.created(new URI(ENDPOINT + response.getId())).body(response);
	}

	/**
	 * PUT  / : update an organization
	 *
	 * @param organizationDTO the organization to update
	 * @return the ResponseEntity with status 200 (OK) and the updated organization in the body
	 *      or with ... TODO: Error handling
	 */
	@PutMapping
	@Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.ORG_ADMIN})
	public ResponseEntity update(@RequestBody @Valid final OrganizationDTO organizationDTO) {
		log.debug("PUT request to update organization : {}", organizationDTO);
		OrganizationDTO response = organizationService.save(organizationDTO);
		return ResponseEntity.ok(response);
	}

	/**
	 * DELETE  / : delete an organization
	 *
	 * @param id the ID of the organization to delete
	 * @return the ResponseEntity with status 200 (OK)
	 *      or with ... TODO: Error handling
	 */
	@DeleteMapping(path = "/{id}")
	@Secured({AuthoritiesConstants.ADMIN})
	public ResponseEntity delete(@PathVariable final Long id) {
		log.debug("DELETE request to delete organization : {}", id);
		organizationService.delete(id);
		return ResponseEntity.ok().build();
	}
}
