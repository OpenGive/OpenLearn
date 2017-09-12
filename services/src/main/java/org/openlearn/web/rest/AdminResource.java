package org.openlearn.web.rest;

import io.swagger.annotations.ApiParam;
import org.openlearn.dto.AdminDTO;
import org.openlearn.security.AuthoritiesConstants;
import org.openlearn.service.AdminService;
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
@RequestMapping("/api/administrators")
public class AdminResource {

	// TODO: Error handling / logging

	private static final String ENDPOINT = "/api/administrators/";

	private static final Logger log = LoggerFactory.getLogger(AdminResource.class);

	private final AdminService adminService;

	public AdminResource(final AdminService adminService) {
		this.adminService = adminService;
	}

	/**
	 * GET  /:id : get a single admin user by ID
	 *
	 * @param id the ID of the admin to get
	 * @return the ResponseEntity with status 200 (OK) and the admin in the body
	 *      or with ... TODO: Error handling
	 */
	@GetMapping(path = "/{id}")
	@Secured({AuthoritiesConstants.ADMIN})
	public ResponseEntity get(@PathVariable final Long id) {
		log.debug("GET request to get admin : {}", id);
		AdminDTO response = adminService.findOne(id);
		return ResponseEntity.ok(response);
	}

	/**
	 * GET  / : get a list of all admin users
	 *
	 * @param pageable the pagination information
	 * @return the ResponseEntity with status 200 (OK) and a list of admins in the body
	 *      or with ... TODO: Error handling
	 */
	@GetMapping
	@Secured({AuthoritiesConstants.ADMIN})
	public ResponseEntity get(@ApiParam final Pageable pageable) {
		log.debug("GET request for all admins");
		Page<AdminDTO> response = adminService.findAll(pageable);
		return ResponseEntity.ok(response.getContent());
	}

	/**
	 * POST  / : create an admin user
	 *
	 * @param adminDTO the admin to create
	 * @return the ResponseEntity with status 200 (OK) and the created admin in the body
	 *      or with ... TODO: Error handling
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PostMapping
	@Secured({AuthoritiesConstants.ADMIN})
	public ResponseEntity create(@RequestBody @Valid final AdminDTO adminDTO) throws URISyntaxException {
		log.debug("POST request to create admin : {}", adminDTO);
		AdminDTO response = adminService.save(adminDTO);
		return ResponseEntity.created(new URI(ENDPOINT + response.getId())).body(response);
	}

	/**
	 * PUT  / : update an admin user
	 *
	 * @param adminDTO the admin to update
	 * @return the ResponseEntity with status 200 (OK) and the updated admin in the body
	 *      or with ... TODO: Error handling
	 */
	@PutMapping
	@Secured({AuthoritiesConstants.ADMIN})
	public ResponseEntity update(@RequestBody @Valid final AdminDTO adminDTO) {
		log.debug("PUT request to update admin : {}", adminDTO);
		AdminDTO response = adminService.save(adminDTO);
		return ResponseEntity.ok(response);
	}

	/**
	 * DELETE  / : delete an admin user
	 *
	 * @param id the ID of the admin to delete
	 * @return the ResponseEntity with status 200 (OK)
	 *      or with ... TODO: Error handling
	 */
	@DeleteMapping(path = "/{id}")
	@Secured({AuthoritiesConstants.ADMIN})
	public ResponseEntity delete(@PathVariable final Long id) {
		log.debug("DELETE request to delete admin : {}", id);
		adminService.delete(id);
		return ResponseEntity.ok().build();
	}
}
