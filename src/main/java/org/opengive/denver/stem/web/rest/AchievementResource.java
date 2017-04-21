package org.opengive.denver.stem.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.opengive.denver.stem.domain.Achievement;
import org.opengive.denver.stem.service.AchievementService;
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
 * REST controller for managing Achievement.
 */
@RestController
@RequestMapping("/api")
public class AchievementResource {

	private final Logger log = LoggerFactory.getLogger(AchievementResource.class);

	private static final String ENTITY_NAME = "achievement";

	private final AchievementService achievementService;

	public AchievementResource(final AchievementService achievementService) {
		this.achievementService = achievementService;
	}

	/**
	 * POST  /achievements : Create a new achievement.
	 *
	 * @param achievement the achievement to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new achievement, or with status 400 (Bad Request) if the achievement has already an ID
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PostMapping("/achievements")
	@Timed
	public ResponseEntity<Achievement> createAchievement(@Valid @RequestBody final Achievement achievement) throws URISyntaxException {
		log.debug("REST request to save Achievement : {}", achievement);
		if (achievement.getId() != null)
			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new achievement cannot already have an ID")).body(null);
		final Achievement result = achievementService.save(achievement);
		return ResponseEntity.created(new URI("/api/achievements/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
				.body(result);
	}

	/**
	 * PUT  /achievements : Updates an existing achievement.
	 *
	 * @param achievement the achievement to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated achievement,
	 * or with status 400 (Bad Request) if the achievement is not valid,
	 * or with status 500 (Internal Server Error) if the achievement couldnt be updated
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PutMapping("/achievements")
	@Timed
	public ResponseEntity<Achievement> updateAchievement(@Valid @RequestBody final Achievement achievement) throws URISyntaxException {
		log.debug("REST request to update Achievement : {}", achievement);
		if (achievement.getId() == null)
			return createAchievement(achievement);
		final Achievement result = achievementService.save(achievement);
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, achievement.getId().toString()))
				.body(result);
	}

	/**
	 * GET  /achievements : get all the achievements.
	 *
	 * @param pageable the pagination information
	 * @return the ResponseEntity with status 200 (OK) and the list of achievements in body
	 */
	@GetMapping("/achievements")
	@Timed
	public ResponseEntity<List<Achievement>> getAllAchievements(@ApiParam final Pageable pageable) {
		log.debug("REST request to get a page of Achievements");
		final Page<Achievement> page = achievementService.findAll(pageable);
		final HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/achievements");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	/**
	 * GET  /achievements/:id : get the "id" achievement.
	 *
	 * @param id the id of the achievement to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the achievement, or with status 404 (Not Found)
	 */
	@GetMapping("/achievements/{id}")
	@Timed
	public ResponseEntity<Achievement> getAchievement(@PathVariable final Long id) {
		log.debug("REST request to get Achievement : {}", id);
		final Achievement achievement = achievementService.findOne(id);
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(achievement));
	}

	/**
	 * DELETE  /achievements/:id : delete the "id" achievement.
	 *
	 * @param id the id of the achievement to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/achievements/{id}")
	@Timed
	public ResponseEntity<Void> deleteAchievement(@PathVariable final Long id) {
		log.debug("REST request to delete Achievement : {}", id);
		achievementService.delete(id);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}

	/**
	 * SEARCH  /_search/achievements?query=:query : search for the achievement corresponding
	 * to the query.
	 *
	 * @param query the query of the achievement search
	 * @param pageable the pagination information
	 * @return the result of the search
	 */
	@GetMapping("/_search/achievements")
	@Timed
	public ResponseEntity<List<Achievement>> searchAchievements(@RequestParam final String query, @ApiParam final Pageable pageable) {
		log.debug("REST request to search for a page of Achievements for query {}", query);
		final Page<Achievement> page = achievementService.search(query, pageable);
		final HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/achievements");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}


}
