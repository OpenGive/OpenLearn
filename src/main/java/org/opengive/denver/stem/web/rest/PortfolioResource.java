package org.opengive.denver.stem.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.opengive.denver.stem.domain.Portfolio;
import org.opengive.denver.stem.service.PortfolioService;
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
 * REST controller for managing Portfolio.
 */
@RestController
@RequestMapping("/api")
public class PortfolioResource {

	private final Logger log = LoggerFactory.getLogger(PortfolioResource.class);

	private static final String ENTITY_NAME = "portfolio";

	private final PortfolioService portfolioService;

	public PortfolioResource(final PortfolioService portfolioService) {
		this.portfolioService = portfolioService;
	}

	/**
	 * POST  /portfolios : Create a new portfolio.
	 *
	 * @param portfolio the portfolio to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new portfolio, or with status 400 (Bad Request) if the portfolio has already an ID
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PostMapping("/portfolios")
	@Timed
	public ResponseEntity<Portfolio> createPortfolio(@Valid @RequestBody final Portfolio portfolio) throws URISyntaxException {
		log.debug("REST request to save Portfolio : {}", portfolio);
		if (portfolio.getId() != null)
			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new portfolio cannot already have an ID")).body(null);
		final Portfolio result = portfolioService.save(portfolio);
		return ResponseEntity.created(new URI("/api/portfolios/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
				.body(result);
	}

	/**
	 * PUT  /portfolios : Updates an existing portfolio.
	 *
	 * @param portfolio the portfolio to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated portfolio,
	 * or with status 400 (Bad Request) if the portfolio is not valid,
	 * or with status 500 (Internal Server Error) if the portfolio couldnt be updated
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PutMapping("/portfolios")
	@Timed
	public ResponseEntity<Portfolio> updatePortfolio(@Valid @RequestBody final Portfolio portfolio) throws URISyntaxException {
		log.debug("REST request to update Portfolio : {}", portfolio);
		if (portfolio.getId() == null)
			return createPortfolio(portfolio);
		final Portfolio result = portfolioService.save(portfolio);
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, portfolio.getId().toString()))
				.body(result);
	}

	/**
	 * GET  /portfolios : get all the portfolios.
	 *
	 * @param pageable the pagination information
	 * @return the ResponseEntity with status 200 (OK) and the list of portfolios in body
	 */
	@GetMapping("/portfolios")
	@Timed
	public ResponseEntity<List<Portfolio>> getAllPortfolios(@ApiParam final Pageable pageable) {
		log.debug("REST request to get a page of Portfolios");
		final Page<Portfolio> page = portfolioService.findAll(pageable);
		final HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/portfolios");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	/**
	 * GET  /portfolios/:id : get the "id" portfolio.
	 *
	 * @param id the id of the portfolio to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the portfolio, or with status 404 (Not Found)
	 */
	@GetMapping("/portfolios/{id}")
	@Timed
	public ResponseEntity<Portfolio> getPortfolio(@PathVariable final Long id) {
		log.debug("REST request to get Portfolio : {}", id);
		final Portfolio portfolio = portfolioService.findOne(id);
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(portfolio));
	}

	/**
	 * DELETE  /portfolios/:id : delete the "id" portfolio.
	 *
	 * @param id the id of the portfolio to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/portfolios/{id}")
	@Timed
	public ResponseEntity<Void> deletePortfolio(@PathVariable final Long id) {
		log.debug("REST request to delete Portfolio : {}", id);
		portfolioService.delete(id);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}

	/**
	 * SEARCH  /_search/portfolios?query=:query : search for the portfolio corresponding
	 * to the query.
	 *
	 * @param query the query of the portfolio search
	 * @param pageable the pagination information
	 * @return the result of the search
	 */
	@GetMapping("/_search/portfolios")
	@Timed
	public ResponseEntity<List<Portfolio>> searchPortfolios(@RequestParam final String query, @ApiParam final Pageable pageable) {
		log.debug("REST request to search for a page of Portfolios for query {}", query);
		final Page<Portfolio> page = portfolioService.search(query, pageable);
		final HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/portfolios");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}


}
