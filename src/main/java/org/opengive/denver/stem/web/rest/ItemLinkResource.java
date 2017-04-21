package org.opengive.denver.stem.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.opengive.denver.stem.domain.ItemLink;
import org.opengive.denver.stem.service.ItemLinkService;
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
 * REST controller for managing ItemLink.
 */
@RestController
@RequestMapping("/api")
public class ItemLinkResource {

	private final Logger log = LoggerFactory.getLogger(ItemLinkResource.class);

	private static final String ENTITY_NAME = "itemLink";

	private final ItemLinkService itemLinkService;

	public ItemLinkResource(final ItemLinkService itemLinkService) {
		this.itemLinkService = itemLinkService;
	}

	/**
	 * POST  /item-links : Create a new itemLink.
	 *
	 * @param itemLink the itemLink to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new itemLink, or with status 400 (Bad Request) if the itemLink has already an ID
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PostMapping("/item-links")
	@Timed
	public ResponseEntity<ItemLink> createItemLink(@Valid @RequestBody final ItemLink itemLink) throws URISyntaxException {
		log.debug("REST request to save ItemLink : {}", itemLink);
		if (itemLink.getId() != null)
			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new itemLink cannot already have an ID")).body(null);
		final ItemLink result = itemLinkService.save(itemLink);
		return ResponseEntity.created(new URI("/api/item-links/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
				.body(result);
	}

	/**
	 * PUT  /item-links : Updates an existing itemLink.
	 *
	 * @param itemLink the itemLink to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated itemLink,
	 * or with status 400 (Bad Request) if the itemLink is not valid,
	 * or with status 500 (Internal Server Error) if the itemLink couldnt be updated
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PutMapping("/item-links")
	@Timed
	public ResponseEntity<ItemLink> updateItemLink(@Valid @RequestBody final ItemLink itemLink) throws URISyntaxException {
		log.debug("REST request to update ItemLink : {}", itemLink);
		if (itemLink.getId() == null)
			return createItemLink(itemLink);
		final ItemLink result = itemLinkService.save(itemLink);
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, itemLink.getId().toString()))
				.body(result);
	}

	/**
	 * GET  /item-links : get all the itemLinks.
	 *
	 * @param pageable the pagination information
	 * @return the ResponseEntity with status 200 (OK) and the list of itemLinks in body
	 */
	@GetMapping("/item-links")
	@Timed
	public ResponseEntity<List<ItemLink>> getAllItemLinks(@ApiParam final Pageable pageable) {
		log.debug("REST request to get a page of ItemLinks");
		final Page<ItemLink> page = itemLinkService.findAll(pageable);
		final HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/item-links");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	/**
	 * GET  /item-links/:id : get the "id" itemLink.
	 *
	 * @param id the id of the itemLink to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the itemLink, or with status 404 (Not Found)
	 */
	@GetMapping("/item-links/{id}")
	@Timed
	public ResponseEntity<ItemLink> getItemLink(@PathVariable final Long id) {
		log.debug("REST request to get ItemLink : {}", id);
		final ItemLink itemLink = itemLinkService.findOne(id);
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(itemLink));
	}

	/**
	 * DELETE  /item-links/:id : delete the "id" itemLink.
	 *
	 * @param id the id of the itemLink to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/item-links/{id}")
	@Timed
	public ResponseEntity<Void> deleteItemLink(@PathVariable final Long id) {
		log.debug("REST request to delete ItemLink : {}", id);
		itemLinkService.delete(id);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}

	/**
	 * SEARCH  /_search/item-links?query=:query : search for the itemLink corresponding
	 * to the query.
	 *
	 * @param query the query of the itemLink search
	 * @param pageable the pagination information
	 * @return the result of the search
	 */
	@GetMapping("/_search/item-links")
	@Timed
	public ResponseEntity<List<ItemLink>> searchItemLinks(@RequestParam final String query, @ApiParam final Pageable pageable) {
		log.debug("REST request to search for a page of ItemLinks for query {}", query);
		final Page<ItemLink> page = itemLinkService.search(query, pageable);
		final HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/item-links");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}


}
