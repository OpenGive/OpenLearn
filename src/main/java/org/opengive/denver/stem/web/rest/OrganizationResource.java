package org.opengive.denver.stem.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.opengive.denver.stem.domain.Organization;
import org.opengive.denver.stem.service.OrganizationService;
import org.opengive.denver.stem.web.rest.util.HeaderUtil;
import org.opengive.denver.stem.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;

import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiParam;

/**
 * REST controller for managing Organization.
 */
@RestController
@RequestMapping("/api")
public class OrganizationResource {

	private final Logger log = LoggerFactory.getLogger(OrganizationResource.class);

	private static final String ENTITY_NAME = "organization";

	private final OrganizationService organizationService;

	public OrganizationResource(final OrganizationService organizationService) {
		this.organizationService = organizationService;
	}

	/**
	 * POST  /organizations : Create a new organization.
	 *
	 * @param organization the organization to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new organization, or with status 400 (Bad Request) if the organization has already an ID
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PostMapping("/organizations")
	@Timed
	public ResponseEntity<Organization> createOrganization(@Valid @RequestBody final Organization organization) throws URISyntaxException {
		log.debug("REST request to save Organization : {}", organization);
		if (organization.getId() != null)
			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new organization cannot already have an ID")).body(null);
		final Organization result = organizationService.save(organization);
		return ResponseEntity.created(new URI("/api/organizations/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
				.body(result);
	}

	/**
	 * PUT  /organizations : Updates an existing organization.
	 *
	 * @param organization the organization to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated organization,
	 * or with status 400 (Bad Request) if the organization is not valid,
	 * or with status 500 (Internal Server Error) if the organization couldnt be updated
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PutMapping("/organizations")
	@Timed
	public ResponseEntity<Organization> updateOrganization(@Valid @RequestBody final Organization organization) throws URISyntaxException {
		log.debug("REST request to update Organization : {}", organization);
		if (organization.getId() == null)
			return createOrganization(organization);
		final Organization result = organizationService.save(organization);
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, organization.getId().toString()))
				.body(result);
	}

	/**
	 * GET  /organizations : get all the organizations.
	 *
	 * @param pageable the pagination information
	 * @return the ResponseEntity with status 200 (OK) and the list of organizations in body
	 */
	@GetMapping("/organizations")
	@Timed
	public ResponseEntity<List<Organization>> getAllOrganizations(@ApiParam final Pageable pageable) {
		log.debug("REST request to get a page of Organizations");
		final Page<Organization> page = organizationService.findAll(pageable);
		final HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/organizations");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	/**
	 * GET  /organizations/:id : get the "id" organization.
	 *
	 * @param id the id of the organization to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the organization, or with status 404 (Not Found)
	 */
	@GetMapping("/organizations/{id}")
	@Timed
	public ResponseEntity<Organization> getOrganization(@PathVariable final Long id) {
		log.debug("REST request to get Organization : {}", id);
		final Organization organization = organizationService.findOne(id);
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(organization));
	}

	/**
	 * DELETE  /organizations/:id : delete the "id" organization.
	 *
	 * @param id the id of the organization to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/organizations/{id}")
	@Timed
	public ResponseEntity<Void> deleteOrganization(@PathVariable final Long id) {
		log.debug("REST request to delete Organization : {}", id);
		organizationService.delete(id);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}

	/**
	 * SEARCH  /_search/organizations?query=:query : search for the organization corresponding
	 * to the query.
	 *
	 * @param query the query of the organization search
	 * @param pageable the pagination information
	 * @return the result of the search
	 */
	@GetMapping("/_search/organizations")
	@Timed
	public ResponseEntity<List<Organization>> searchOrganizations(@RequestParam final String query, @ApiParam final Pageable pageable) {
		log.debug("REST request to search for a page of Organizations for query {}", query);
		final Page<Organization> page = organizationService.search(query, pageable);
		final HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/organizations");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}


}
