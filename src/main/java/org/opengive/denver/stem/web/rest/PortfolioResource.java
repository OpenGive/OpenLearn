package org.opengive.denver.stem.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.opengive.denver.stem.domain.Portfolio;
import org.opengive.denver.stem.service.PortfolioService;
import org.opengive.denver.stem.web.rest.util.HeaderUtil;
import org.opengive.denver.stem.web.rest.util.PaginationUtil;
import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Portfolio.
 */
@RestController
@RequestMapping("/api")
public class PortfolioResource {

    private final Logger log = LoggerFactory.getLogger(PortfolioResource.class);

    private static final String ENTITY_NAME = "portfolio";
        
    private final PortfolioService portfolioService;

    public PortfolioResource(PortfolioService portfolioService) {
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
    public ResponseEntity<Portfolio> createPortfolio(@Valid @RequestBody Portfolio portfolio) throws URISyntaxException {
        log.debug("REST request to save Portfolio : {}", portfolio);
        if (portfolio.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new portfolio cannot already have an ID")).body(null);
        }
        Portfolio result = portfolioService.save(portfolio);
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
    public ResponseEntity<Portfolio> updatePortfolio(@Valid @RequestBody Portfolio portfolio) throws URISyntaxException {
        log.debug("REST request to update Portfolio : {}", portfolio);
        if (portfolio.getId() == null) {
            return createPortfolio(portfolio);
        }
        Portfolio result = portfolioService.save(portfolio);
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
    public ResponseEntity<List<Portfolio>> getAllPortfolios(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Portfolios");
        Page<Portfolio> page = portfolioService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/portfolios");
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
    public ResponseEntity<Portfolio> getPortfolio(@PathVariable Long id) {
        log.debug("REST request to get Portfolio : {}", id);
        Portfolio portfolio = portfolioService.findOne(id);
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
    public ResponseEntity<Void> deletePortfolio(@PathVariable Long id) {
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
    public ResponseEntity<List<Portfolio>> searchPortfolios(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of Portfolios for query {}", query);
        Page<Portfolio> page = portfolioService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/portfolios");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
