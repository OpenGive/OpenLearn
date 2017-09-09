package org.openlearn.web.rest;

import io.swagger.annotations.ApiParam;
import org.openlearn.dto.PortfolioItemDTO;
import org.openlearn.security.AuthoritiesConstants;
import org.openlearn.service.PortfolioItemService;
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
@RequestMapping("/api/portfolio-items")
public class PortfolioItemResource {

	// TODO: Error handling / logging

	private static final String ENDPOINT = "/api/portfolio-items/";

	private final Logger log = LoggerFactory.getLogger(PortfolioItemResource.class);

	private final PortfolioItemService portfolioItemService;

	public PortfolioItemResource(PortfolioItemService portfolioItemService) {
		this.portfolioItemService = portfolioItemService;
	}

	/**
	 * GET  /:id : get a single portfolio item by ID
	 *
	 * @param id the ID of the portfolio item to get
	 * @return the ResponseEntity with status 200 (OK) and the portfolio item in the body
	 *      or with ... TODO: Error handling
	 */
	@GetMapping("/{id}")
	@Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.ORG_ADMIN, AuthoritiesConstants.INSTRUCTOR})
	public ResponseEntity get(@PathVariable Long id) {
		log.debug("GET request to get portfolio item : {}", id);
		PortfolioItemDTO response = portfolioItemService.findOne(id);
		return ResponseEntity.ok(response);
	}

	/**
	 * GET  / : get a list of all portfolio item, filtered by organization
	 *
	 * @param pageable the pagination information
	 * @return the ResponseEntity with status 200 (OK) and a list of portfolio items in the body
	 *      or with ... TODO: Error handling
	 */
	@GetMapping
	@Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.ORG_ADMIN, AuthoritiesConstants.INSTRUCTOR})
	public ResponseEntity get(@ApiParam Pageable pageable) {
		log.debug("GET request for all portfolio items");
		Page<PortfolioItemDTO> response = portfolioItemService.findAll(pageable);
		return ResponseEntity.ok(response);
	}

	/**
	 * POST  / : create a portfolio item
	 *
	 * @param portfolioItemDTO the portfolio item to create
	 * @return the ResponseEntity with status 200 (OK) and the created portfolio item in the body
	 *      or with ... TODO: Error handling
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PostMapping
	@Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.ORG_ADMIN, AuthoritiesConstants.INSTRUCTOR})
	public ResponseEntity create(@RequestBody @Valid PortfolioItemDTO portfolioItemDTO) throws URISyntaxException {
		log.debug("POST request to create portfolio item : {}", portfolioItemDTO);
		PortfolioItemDTO response = portfolioItemService.save(portfolioItemDTO);
		return ResponseEntity.created(new URI(ENDPOINT + response.getId())).body(response);
	}

	/**
	 * PUT  / : update a portfolio item
	 *
	 * @param portfolioItemDTO the portfolio item to update
	 * @return the ResponseEntity with status 200 (OK) and the updated portfolio item in the body
	 *      or with ... TODO: Error handling
	 */
	@PutMapping
	@Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.ORG_ADMIN, AuthoritiesConstants.INSTRUCTOR})
	public ResponseEntity update(@RequestBody @Valid PortfolioItemDTO portfolioItemDTO) {
		log.debug("PUT request to update portfolio item : {}", portfolioItemDTO);
		PortfolioItemDTO response = portfolioItemService.save(portfolioItemDTO);
		return ResponseEntity.ok(response);
	}

	/**
	 * DELETE  / : delete a portfolio item
	 *
	 * @param id the ID of the portfolio item to delete
	 * @return the ResponseEntity with status 200 (OK)
	 *      or with ... TODO: Error handling
	 */
	@DeleteMapping("/{id}")
	@Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.ORG_ADMIN, AuthoritiesConstants.INSTRUCTOR})
	public ResponseEntity delete(@PathVariable Long id) {
		log.debug("DELETE request to delete portfolio item : {}", id);
		portfolioItemService.delete(id);
		return ResponseEntity.ok().build();
	}
}
