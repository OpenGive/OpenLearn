package org.opengive.denver.stem.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.opengive.denver.stem.domain.Milestone;
import org.opengive.denver.stem.service.MilestoneService;
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
 * REST controller for managing Milestone.
 */
@RestController
@RequestMapping("/api")
public class MilestoneResource {

    private final Logger log = LoggerFactory.getLogger(MilestoneResource.class);

    private static final String ENTITY_NAME = "milestone";
        
    private final MilestoneService milestoneService;

    public MilestoneResource(MilestoneService milestoneService) {
        this.milestoneService = milestoneService;
    }

    /**
     * POST  /milestones : Create a new milestone.
     *
     * @param milestone the milestone to create
     * @return the ResponseEntity with status 201 (Created) and with body the new milestone, or with status 400 (Bad Request) if the milestone has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/milestones")
    @Timed
    public ResponseEntity<Milestone> createMilestone(@Valid @RequestBody Milestone milestone) throws URISyntaxException {
        log.debug("REST request to save Milestone : {}", milestone);
        if (milestone.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new milestone cannot already have an ID")).body(null);
        }
        Milestone result = milestoneService.save(milestone);
        return ResponseEntity.created(new URI("/api/milestones/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /milestones : Updates an existing milestone.
     *
     * @param milestone the milestone to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated milestone,
     * or with status 400 (Bad Request) if the milestone is not valid,
     * or with status 500 (Internal Server Error) if the milestone couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/milestones")
    @Timed
    public ResponseEntity<Milestone> updateMilestone(@Valid @RequestBody Milestone milestone) throws URISyntaxException {
        log.debug("REST request to update Milestone : {}", milestone);
        if (milestone.getId() == null) {
            return createMilestone(milestone);
        }
        Milestone result = milestoneService.save(milestone);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, milestone.getId().toString()))
            .body(result);
    }

    /**
     * GET  /milestones : get all the milestones.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of milestones in body
     */
    @GetMapping("/milestones")
    @Timed
    public ResponseEntity<List<Milestone>> getAllMilestones(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Milestones");
        Page<Milestone> page = milestoneService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/milestones");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /milestones/:id : get the "id" milestone.
     *
     * @param id the id of the milestone to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the milestone, or with status 404 (Not Found)
     */
    @GetMapping("/milestones/{id}")
    @Timed
    public ResponseEntity<Milestone> getMilestone(@PathVariable Long id) {
        log.debug("REST request to get Milestone : {}", id);
        Milestone milestone = milestoneService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(milestone));
    }

    /**
     * DELETE  /milestones/:id : delete the "id" milestone.
     *
     * @param id the id of the milestone to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/milestones/{id}")
    @Timed
    public ResponseEntity<Void> deleteMilestone(@PathVariable Long id) {
        log.debug("REST request to delete Milestone : {}", id);
        milestoneService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/milestones?query=:query : search for the milestone corresponding
     * to the query.
     *
     * @param query the query of the milestone search 
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/milestones")
    @Timed
    public ResponseEntity<List<Milestone>> searchMilestones(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of Milestones for query {}", query);
        Page<Milestone> page = milestoneService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/milestones");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
